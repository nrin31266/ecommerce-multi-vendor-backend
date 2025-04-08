package com.vanrin05.app.service;

import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.dto.request.CreateOrderRequest;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.Payment;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, Address shippingAddress, Cart cart,  CreateOrderRequest createOrderRequest);
    Order findOrderById(Long orderId);
    List<Order> userOrdersHistory(Long userId);
//    List<Order> sellerOrders(Long sellerId, ORDER_STATUS orderStatus);
//    Order updateOrderStatus(Long orderId, ORDER_STATUS orderStatus);
    Order cancelOrder(Order order, User user, String cancelReason);
    OrderItem findOrderItemById(Long orderItemId);

    Order proceedPayment(Order order, User user);
}
