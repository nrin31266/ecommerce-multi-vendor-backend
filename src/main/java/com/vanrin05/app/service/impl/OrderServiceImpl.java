package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.dto.request.CreateOrderRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.OrderItemMapper;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.cart.CartItem;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.Payment;
import com.vanrin05.app.model.orderpayment.PaymentDetails;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import com.vanrin05.app.repository.*;
import com.vanrin05.app.service.OrderService;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    AddressRepository addressRepository;
    OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;
    SubProductRepository subProductRepository;
    ProductService productService;
    OrderItemMapper orderItemMapper;


    @Override
    public Order createOrder(User user, Address shippingAddress, Cart cart, CreateOrderRequest createOrderRequest) {
        List<CartItem> cartItems = cart.getCartItems();

        List<OrderItem> orderItems = new ArrayList<>();
        List<SubProduct> stockProducts = new ArrayList<>();
        long totalMrpPrice = 0L;
        long totalSellingPrice = 0L;
        int totalQuantity = 0;
        int totalItem = 0;
        for (CartItem cartItem : cartItems) {
            // Handle stock
            Product product = cartItem.getProduct();
            SubProduct subProduct = cartItem.getSubProduct();
            if(subProduct.getQuantity() < cartItem.getQuantity()){
                throw new AppException("The product " + ((product.getTitle().length() < 20) ?
                        product.getTitle() : product.getTitle().substring(20) + "...")  +" is currently out of stock");
            }
            subProduct.setQuantity(subProduct.getQuantity() - cartItem.getQuantity());
            product.setTotalOrder(product.getTotalOrder() + subProduct.getQuantity());
            subProduct.setProduct(product);
            stockProducts.add(subProduct);

            // Total
            totalMrpPrice += cartItem.getQuantity() * subProduct.getMrpPrice();
            totalSellingPrice += cartItem.getQuantity() * subProduct.getSellingPrice();
            totalQuantity += cartItem.getQuantity();
            totalItem += 1;

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setSubProduct(subProduct);
            orderItem.setPaymentDetails(
                    PaymentDetails.builder()
                            .paymentStatus(PAYMENT_STATUS.PENDING)
                            .build()
            );
            orderItem.setSellerId(cartItem.getProduct().getSeller().getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setMrpPrice( cartItem.getSubProduct().getMrpPrice() * cartItem.getQuantity());
            orderItem.setSellingPrice(cartItem.getSubProduct().getSellingPrice() * cartItem.getQuantity());
            orderItem.setDeliveryDate(LocalDateTime.now().plusDays(7));
            orderItem.setStatus(createOrderRequest.getPaymentMethod()
                    .equals(PAYMENT_METHOD.CASH_ON_DELIVERY)?
                    ORDER_ITEM_STATUS.PENDING:ORDER_ITEM_STATUS.PENDING_PAYMENT);
            orderItems.add(orderItem);
        }
        subProductRepository.saveAll(stockProducts);

        // Clear cart
        cart.getCartItems().clear();
        cart.setCouponCode(null);
        cartRepository.save(cart);

        // Update seller report: Not update soon

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setOrderItems(orderItems);
        order.setTotalMrpPrice(totalMrpPrice);
        order.setTotalSellingPrice(totalSellingPrice);
        order.setDiscountPercentage(discountPercentage(totalMrpPrice, totalSellingPrice));
        order.setTotalItem(totalItem);
        order.setTotalQuantity(totalQuantity);
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());
        order = orderRepository.save(order);

        return order;

    }




    private int discountPercentage(double mrpPrice, double sellingPrice) {
        log.info("Mrp: {}; Selling: {}", mrpPrice, sellingPrice);
        if (mrpPrice < sellingPrice || mrpPrice <= 0) {
            throw new AppException("Mrp price is invalid. Mrp: " + mrpPrice + ", Selling price: " + sellingPrice);
        }
        double discount = (mrpPrice - sellingPrice);
        double percentage = discount / mrpPrice * 100;
        return (int) percentage;
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->new AppException("Order not found"));
    }

    @Override
    public List<Order> userOrdersHistory(Long userId) {
//        return orderRepository.customFindByUserId(userId, PAYMENT_STATUS.COMPLETED);
//            return  orderRepository.findByUserIdAndPaymentDetails_PaymentStatus(userId, PAYMENT_STATUS.COMPLETED);
    return null;

    }



    @Override
    public List<OrderItem> sellerOrders(Seller seller, ORDER_ITEM_STATUS orderItemStatus) {

        Specification<OrderItem> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("sellerId"), seller.getId()));
            predicates.add(criteriaBuilder.equal(root.get("status"), orderItemStatus));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return orderItemRepository.findAll(specification);
    }

    @Override
    public OrderItem updateOrderItemStatus(Long orderId, ORDER_ITEM_STATUS orderItemStatus, Long orderItemId,  Seller seller) {
        OrderItem orderItem = findOrderItemById(orderItemId);
        if(!seller.getId().equals(orderItem.getSellerId())) {
            throw new AppException("Seller is not the seller");
        }

        OrderItem orderStatus = findOrderItemById(orderItemId);
        orderStatus.setStatus(orderItemStatus);

        return orderItemRepository.save(orderStatus);
    }



    @Override
    public Order cancelOrder(Order order, User user, String cancelReason) {
        if(!order.getUser().getId().equals(user.getId())){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        //Product
        productService.restoreStock(order);

        // Handle order items
        List<OrderItem> orderItems = order.getOrderItems();
        for(OrderItem orderItem : orderItems){
            orderItem.setCancelReason(cancelReason);
            orderItem.setStatus(ORDER_ITEM_STATUS.CANCELLED);
        }


        return orderRepository.save(order);

    }

    @Override
    public OrderItem sellerCancelOrder(Order order, Seller seller, String cancelReason, OrderItem orderItem) {
        if(!orderItem.getSellerId().equals(seller.getId())){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        //Product
        productService.restoreStock(orderItem);

        // Handle order items
        orderItem.setCancelReason(cancelReason);
        orderItem.setStatus(ORDER_ITEM_STATUS.CANCELLED);



        return orderItemRepository.save(orderItem);

    }

    @Override
    public OrderItem findOrderItemById(Long orderItemId) {

        return orderItemRepository.findById(orderItemId).orElseThrow(()->new AppException("Order item not found"));
    }

    @Override
    public Order proceedPayment(Order order, User user) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setStatus(ORDER_ITEM_STATUS.PENDING);
            orderItem.getPaymentDetails().setPaymentStatus(PAYMENT_STATUS.COMPLETED);
            orderItem.getPaymentDetails().setPaymentDate(LocalDateTime.now());
        }

        return orderRepository.save(order);
    }

    @Override
    public OrderItem approveOrderItem(Long orderId, Long orderItemId,  Seller seller) {
        OrderItem orderItem = findOrderItemById(orderItemId);
        if(orderItem.getIsApproved()){
            throw new AppException("Order item is already approved");
        }
        orderItem.setIsApproved(true);
        orderItem.setStatus(ORDER_ITEM_STATUS.CONFIRMED);

        return orderItemRepository.save(orderItem);
    }
}
