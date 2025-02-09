package com.vanrin05.service;

import com.vanrin05.domain.ORDER_STATUS;
import com.vanrin05.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {
    Set<Order> createOrders(User user, Address shippingAddress, Cart cart);
    Order findOrderById(Long orderId);
    List<Order> userOrdersHistory(Long userId);
    List<Order> sellerOrders(Long sellerId);
    Order updateOrderStatus(Long orderId, ORDER_STATUS orderStatus);
    Order cancelOrder(Long orderId, User user);
    OrderItem findOrderItemById(Long orderItemId);
}
