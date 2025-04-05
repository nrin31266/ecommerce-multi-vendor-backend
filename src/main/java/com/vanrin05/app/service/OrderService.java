package com.vanrin05.app.service;

import com.vanrin05.app.domain.ORDER_STATUS;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.model.*;

import java.util.List;

public interface OrderService {
    List<Order> createOrders(User user, Address shippingAddress, Cart cart, PAYMENT_METHOD paymentMethod);
    Order findOrderById(Long orderId);
    List<Order> userOrdersHistory(Long userId);
    List<Order> sellerOrders(Long sellerId, ORDER_STATUS orderStatus);
    Order updateOrderStatus(Long orderId, ORDER_STATUS orderStatus);
    Order cancelOrder(Long orderId, User user);
    OrderItem findOrderItemById(Long orderItemId);

//    Order cancelOrder
}
