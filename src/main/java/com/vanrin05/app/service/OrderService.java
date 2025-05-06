package com.vanrin05.app.service;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.dto.request.CreateOrderRequest;
import com.vanrin05.app.dto.response.UserOrderHistoryResponse;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.SellerOrder;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, Address shippingAddress, Cart cart,  CreateOrderRequest createOrderRequest);
    Order findOrderById(Long orderId);
    List<Order> userOrdersHistory(Long userId);


    Order cancelOrder(Order order, User user, String cancelReason);
    OrderItem findOrderItemById(Long orderItemId);


    void proceedPayment(Order order, User user);

    SellerOrder approveSellerOrder(Long sellerOrderId, Seller seller);
    SellerOrder sellerCancelOrder(Seller seller, String cancelReason, Long sellerOrderId);
    SellerOrder userCancelSellerOrder(User user, String cancelReason, Long sellerOrderId);
    SellerOrder userConfirmSellerOrder(User user, Long sellerOrderId);
    SellerOrder updateSellerOrderStatus(SELLER_ORDER_STATUS status, Long sellerOrderId, Seller seller);
    SellerOrder getSellerOrderById(Long sellerOrderId);
    List<UserOrderHistoryResponse> sellerOrders(Seller seller, SELLER_ORDER_STATUS status);
    List<SellerOrder> userOrders(User user, SELLER_ORDER_STATUS status);

    void saveOrderItem(OrderItem orderItem);
}
