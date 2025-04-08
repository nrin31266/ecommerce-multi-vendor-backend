package com.vanrin05.app.service;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.dto.request.CreateOrderRequest;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.Payment;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, Address shippingAddress, Cart cart,  CreateOrderRequest createOrderRequest);
    Order findOrderById(Long orderId);
    List<Order> userOrdersHistory(Long userId);
    List<OrderItem> sellerOrders(Seller seller, ORDER_ITEM_STATUS orderItemStatus);
    OrderItem updateOrderItemStatus(Long orderId, ORDER_ITEM_STATUS orderItemStatus, Long orderItemId);
    Order cancelOrder(Order order, User user, String cancelReason);
    OrderItem findOrderItemById(Long orderItemId);
    OrderItem sellerCancelOrder(Order order, Seller seller, String cancelReason, OrderItem orderItem);
    Order proceedPayment(Order order, User user);
    OrderItem approveOrderItem(Long orderId, Long orderItemId, Seller seller);
}
