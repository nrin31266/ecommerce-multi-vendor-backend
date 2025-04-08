package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.dto.request.CreateOrderRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
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
        cart.setDiscount(0);
        cart.setCouponCode(null);
        cart.setTotalSellingPrice(0L);
        cart.setTotalMrpPrice(0L);
        cartRepository.save(cart);

        // Update seller report: Not update soon

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setOrderItems(orderItems);
        order.setTotalMrpPrice(totalMrpPrice);
        order.setTotalSellingPrice(totalSellingPrice);
        order.setDiscount(totalMrpPrice - totalSellingPrice);
        order.setTotalItem(totalItem);
        order.setTotalQuantity(totalQuantity);
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());
        order = orderRepository.save(order);

        return order;
//        shippingAddress = addressRepository.save(shippingAddress);
//        user.getAddresses().add(shippingAddress);
//        Map<Long, List<CartItem>> items =  cart.getCartItems().stream().collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
//        List<Order> orders = new ArrayList<>();
//        List<Product> products = new ArrayList<>();
//        for(Map.Entry<Long, List<CartItem>> entry : items.entrySet()) {
//            Long sellerId = entry.getKey();
//            List<CartItem> cartItems = entry.getValue();
//            int totalOrderPrice = cartItems.stream().mapToInt(
//                    CartItem::getSellingPrice
//            ).sum();
//            int totalMrpPrice = cartItems.stream().mapToInt(
//                    CartItem::getMrpPrice
//            ).sum();
//            int totalItem = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
//            Order createdOrder = new Order();
//            createdOrder.getPaymentDetails().setPaymentMethod(paymentMethod);
//            createdOrder.setUser(user);
//            createdOrder.setSellerId(sellerId);
//            createdOrder.setTotalMrpPrice(totalMrpPrice);
//            createdOrder.setTotalSellingPrice(totalOrderPrice);
//            createdOrder.setTotalItem(totalItem);
//            createdOrder.getPaymentDetails().setPaymentStatus(PAYMENT_STATUS.PENDING);
//            createdOrder.setShippingAddress(shippingAddress);
//            createdOrder.setOrderStatus((paymentMethod == PAYMENT_METHOD.CASH_ON_DELIVERY)? ORDER_STATUS.PLACED : ORDER_STATUS.PENDING);
//            createdOrder.setDiscount(discountPercentage(totalMrpPrice, totalOrderPrice));
//            createdOrder = orderRepository.save(createdOrder);
//            List<OrderItem> orderItems = new ArrayList<>();
//            for (CartItem cartItem : cartItems) {
//                if(cartItem.getQuantity() > cartItem.getProduct().getQuantity()){
//                    throw new AppException(cartItem.getProduct().getTitle() + ": stock unavailable");
//
//                }
//                Product product = cartItem.getProduct();
//                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
//                products.add(product);
//                OrderItem orderItem = new OrderItem();
//                orderItem.setProduct(cartItem.getProduct());
//                orderItem.setQuantity(cartItem.getQuantity());
//                orderItem.setSize(cartItem.getSize());
//                orderItem.setUserId(user.getId());
//                orderItem.setSellingPrice(cartItem.getSellingPrice());
//                orderItem.setMrpPrice(cartItem.getMrpPrice());
//                orderItem.setOrder(createdOrder);
//                orderItems.add(orderItem);
//            }
//            cart.setDiscount(0);
//            cart.getCartItems().clear();
//            cart.setTotalMrpPrice(0);
//            cart.setTotalSellingPrice(0);
//            cart.setTotalItems(0);
//            cart.setCouponCode(null);
//            cartRepository.save(cart);
//            productRepository.saveAll(products);
//            orderItemRepository.saveAll(orderItems);
//            createdOrder.setOrderItems(orderItems);
//            orders.add(createdOrder);
//        }
//        return orders;

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



//    @Override
//    public List<Order> sellerOrders(Long sellerId, ORDER_STATUS orderStatus) {
//
//        Specification<Order> specification = (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//
//            predicates.add(criteriaBuilder.equal(root.get("sellerId"), sellerId));
//            predicates.add(criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
//
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        };
//
//        return orderRepository.findAll(specification);
//    }
//
//    @Override
//    public Order updateOrderStatus(Long orderId, ORDER_STATUS orderStatus) {
//        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException("Order not found"));
//        order.setOrderStatus(orderStatus);
//        return orderRepository.save(order);
//    }

    @Override
    public Order cancelOrder(Order order, User user, String cancelReason) {
        if(!order.getUser().getId().equals(user.getId())){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        //Product
        productService.restoreStock(order, user);

        // Handle order items
        List<OrderItem> orderItems = order.getOrderItems();
        for(OrderItem orderItem : orderItems){
            orderItem.setCancelReason(cancelReason);
            orderItem.setStatus(ORDER_ITEM_STATUS.CANCELLED);
        }


        return orderRepository.save(order);

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
}
