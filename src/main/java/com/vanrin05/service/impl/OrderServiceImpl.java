package com.vanrin05.service.impl;

import com.vanrin05.domain.ORDER_STATUS;
import com.vanrin05.domain.PAYMENT_STATUS;
import com.vanrin05.exception.AppException;
import com.vanrin05.model.*;
import com.vanrin05.repository.AddressRepository;
import com.vanrin05.repository.OrderItemRepository;
import com.vanrin05.repository.OrderRepository;
import com.vanrin05.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    AddressRepository addressRepository;
    OrderItemRepository orderItemRepository;



    @Transactional
    @Override
    public Set<Order> createOrders(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddresses().contains(shippingAddress)) {
            user.getAddresses().add(shippingAddress);
        }

        Address address = addressRepository.save(shippingAddress);

        Map<Long, List<CartItem>> items = new HashMap<>();
        items = cart.getCartItems().stream().collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for(Map.Entry<Long, List<CartItem>> entry : items.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> cartItems = entry.getValue();

            int totalOrderPrice = cartItems.stream().mapToInt(
                    CartItem::getSellingPrice
            ).sum();

            int totalItem = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);

            createdOrder.setOrderStatus(ORDER_STATUS.PENDING);
            createdOrder.getPaymentDetails().setStatus(PAYMENT_STATUS.PENDING);
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

                createdOrder.getOrderItems().add(orderItem);
                orderItems.add(orderItem);
            }

            orderItemRepository.saveAll(orderItems);

            createdOrder.setOrderItems(orderItems);
            orders.add(createdOrder);

        }

        return orders;
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

        return orderItemRepository.findById(orderItemId).orElseThrow(()->new AppException("Order item not found"));
    }
}
