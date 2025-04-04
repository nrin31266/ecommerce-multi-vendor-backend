package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.ORDER_STATUS;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.*;
import com.vanrin05.app.repository.*;
import com.vanrin05.app.service.OrderService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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


    @Override
    public List<Order> createOrders(User user, Address shippingAddress, Cart cart, PAYMENT_METHOD paymentMethod) {
        shippingAddress = addressRepository.save(shippingAddress);
        user.getAddresses().add(shippingAddress);

        Map<Long, List<CartItem>> items =  cart.getCartItems().stream().collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));



        List<Order> orders = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for(Map.Entry<Long, List<CartItem>> entry : items.entrySet()) {



            Long sellerId = entry.getKey();

            List<CartItem> cartItems = entry.getValue();



            int totalOrderPrice = cartItems.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();

            int totalMrpPrice = cartItems.stream().mapToInt(
                    CartItem::getMrpPrice
            ).sum();

            int totalItem = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.getPaymentDetails().setPaymentMethod(paymentMethod);
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalMrpPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.getPaymentDetails().setPaymentStatus(PAYMENT_STATUS.PENDING);
            createdOrder.setShippingAddress(shippingAddress);
            createdOrder.setOrderStatus((paymentMethod == PAYMENT_METHOD.CASH_ON_DELIVERY)? ORDER_STATUS.PLACED : ORDER_STATUS.PENDING);
            createdOrder.setDiscount(discountPercentage(totalMrpPrice, totalOrderPrice));
            createdOrder = orderRepository.save(createdOrder);
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cartItems) {

                if(cartItem.getQuantity() > cartItem.getProduct().getQuantity()){
                    throw new AppException(cartItem.getProduct().getTitle() + ": stock unavailable");

                }


                Product product = cartItem.getProduct();
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                products.add(product);



                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSize(cartItem.getSize());
                orderItem.setUserId(user.getId());
                orderItem.setSellingPrice(cartItem.getSellingPrice());
                orderItem.setMrpPrice(cartItem.getMrpPrice());
                orderItem.setOrder(createdOrder);
                orderItems.add(orderItem);
            }

            cart.setDiscount(0);
            cart.getCartItems().clear();
            cart.setTotalMrpPrice(0);
            cart.setTotalSellingPrice(0);
            cart.setTotalItems(0);
            cart.setCouponCode(null);
            cartRepository.save(cart);


            productRepository.saveAll(products);
            orderItemRepository.saveAll(orderItems);
            createdOrder.setOrderItems(orderItems);
            orders.add(createdOrder);
        }



        return orders;
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
            return  orderRepository.findByUserIdAndPaymentDetails_PaymentStatus(userId, PAYMENT_STATUS.COMPLETED);


    }



    @Override
    public List<Order> sellerOrders(Long sellerId, ORDER_STATUS orderStatus) {

        Specification<Order> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("sellerId"), sellerId));
            predicates.add(criteriaBuilder.equal(root.get("orderStatus"), orderStatus));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return orderRepository.findAll(specification);
    }

    @Override
    public Order updateOrderStatus(Long orderId, ORDER_STATUS orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException("Order not found"));
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new AppException("Order not found"));
        if(!user.getId().equals(order.getUser().getId())) {
            throw new AppException("User is not the user");
        }
        order.setOrderStatus(ORDER_STATUS.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem findOrderItemById(Long orderItemId) {
        log.info("OrderItemId: {}", orderItemId);
        return orderItemRepository.findById(orderItemId).orElseThrow(()->new AppException("Order item not found"));
    }
}
