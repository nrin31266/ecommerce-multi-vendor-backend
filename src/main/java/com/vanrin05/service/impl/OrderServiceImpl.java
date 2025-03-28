package com.vanrin05.service.impl;

import com.vanrin05.domain.ORDER_STATUS;
import com.vanrin05.domain.PAYMENT_STATUS;
import com.vanrin05.exception.AppException;
import com.vanrin05.model.*;
import com.vanrin05.repository.AddressRepository;
import com.vanrin05.repository.OrderItemRepository;
import com.vanrin05.repository.OrderRepository;
import com.vanrin05.repository.UserRepository;
import com.vanrin05.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Order> createOrders(User user, Address shippingAddress, Cart cart) {
        shippingAddress = addressRepository.save(shippingAddress);
        user.getAddresses().add(shippingAddress);



        Map<Long, List<CartItem>> items =  cart.getCartItems().stream().collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        List<Order> orders = new ArrayList<>();

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
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalMrpPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.getPaymentDetails().setPaymentStatus(PAYMENT_STATUS.PENDING);
            createdOrder.setShippingAddress(shippingAddress);
            createdOrder.setOrderStatus(ORDER_STATUS.PENDING);
            createdOrder.setDiscount(discountPercentage(totalMrpPrice, totalOrderPrice));
            createdOrder = orderRepository.save(createdOrder);
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
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
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrders(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
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
