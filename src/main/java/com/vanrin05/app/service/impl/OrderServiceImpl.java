package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.dto.request.CreateOrderRequest;
import com.vanrin05.app.dto.response.UserOrderHistoryResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.OrderItemMapper;
import com.vanrin05.app.mapper.SellerOrderMapper;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.cart.CartItem;
import com.vanrin05.app.model.orderpayment.*;
import com.vanrin05.app.model.product.SubProduct;
import com.vanrin05.app.repository.*;
import com.vanrin05.app.service.OrderService;
import com.vanrin05.app.service.SellerReportService;
import com.vanrin05.app.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    CartRepository cartRepository;
    SubProductRepository subProductRepository;
    ProductService productService;
    SellerReportService sellerReportService;
    TransactionService transactionService;


    @Override
    public Order createOrder(User user, Address shippingAddress, Cart cart, CreateOrderRequest createOrderRequest) {
        // 1. Tạo Order mới
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);

        // 2. Nhóm CartItem theo Seller
        List<CartItem> cartItems = cart.getCartItems();
        Map<Seller, List<CartItem>> groupedBySeller = cartItems.stream()
                .collect(Collectors.groupingBy(ci -> ci.getProduct().getSeller()));

        // 3. Dùng for loop để build SellerOrder
        List<SellerOrder> sellerOrders = new ArrayList<>();
        List<SubProduct> stockProducts = new ArrayList<>();

        for (Map.Entry<Seller, List<CartItem>> entry : groupedBySeller.entrySet()) {
            Seller seller = entry.getKey();
            List<CartItem> cartItemList = entry.getValue();

            SellerOrder sellerOrder = new SellerOrder();
            sellerOrder.setOrder(order);           // gắn hai chiều ngay
            sellerOrder.setSeller(seller);
            sellerOrder.setUserId(user.getId());
            sellerOrder.setIsApproved(false);
            sellerOrder.setDeliveryDate(LocalDateTime.now().plusDays(7));
            sellerOrder.setPaymentDetails(
                    PaymentDetails.builder()
                            .paymentStatus(PAYMENT_STATUS.PENDING)
                            .build()
            );
            sellerOrder.setStatus(
                    createOrderRequest.getPaymentMethod() == PAYMENT_METHOD.CASH_ON_DELIVERY
                            ? SELLER_ORDER_STATUS.PENDING
                            : SELLER_ORDER_STATUS.PENDING_PAYMENT
            );

            int totalItem = 0;
            long totalPrice = 0L;
            List<OrderItem> orderItems = new ArrayList<>();

            for (CartItem cartItem : cartItemList) {
                // 3.1 Kiểm tra và cập nhật stock
                SubProduct sub = cartItem.getSubProduct();
                if (sub.getQuantity() < cartItem.getQuantity()) {
                    String name = sub.getProduct().getTitle();
                    if (name.length() > 20) {
                        name = name.substring(0, 30) + "...";
                    }
                    throw new AppException("Product out of stock: " + name);
                }
                sub.setQuantity(sub.getQuantity() - cartItem.getQuantity());
                stockProducts.add(sub);

                // 3.2 Tạo OrderItem
                OrderItem oi = new OrderItem();
                oi.setSellerOrder(sellerOrder);
                oi.setProduct(cartItem.getProduct());
                oi.setSubProduct(sub);
                oi.setQuantity(cartItem.getQuantity());
                oi.setMrpPrice(sub.getMrpPrice() * cartItem.getQuantity());
                oi.setSellingPrice(sub.getSellingPrice() * cartItem.getQuantity());
                oi.setUserId(user.getId());

                orderItems.add(oi);

                // 3.3 Cộng dồn
                totalItem += cartItem.getQuantity();
                totalPrice += oi.getSellingPrice();
            }

            // 4. Thiết lập tổng và discount
            sellerOrder.setTotalItem(totalItem);
            sellerOrder.setTotalPrice(totalPrice);
            sellerOrder.setDiscountShop(0L);
            sellerOrder.setDiscountShipping(0L);
            sellerOrder.setShippingCost(30000L);

            long finalPrice = totalPrice
                              + sellerOrder.getDiscountShop()
                              + sellerOrder.getDiscountShipping()
                              + sellerOrder.getShippingCost();
            ;
            sellerOrder.setFinalPrice(Math.max(finalPrice, 0L));

            // 5. Gắn list OrderItem và collect SellerOrder
            sellerOrder.setOrderItems(orderItems);
            sellerOrders.add(sellerOrder);
        }

        // 6. Lưu stock, clear cart
        subProductRepository.saveAll(stockProducts);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // 7. Tính tổng lên Order cha
        order.setSellerOrders(sellerOrders);
        var totalItemsPrice = sellerOrders.stream().mapToLong(SellerOrder::getTotalPrice).sum();
        order.setTotalItemsPrice(
                totalItemsPrice
        );
        var originalPrice = sellerOrders.stream().mapToLong(SellerOrder::getFinalPrice).sum();
        var finalPrice = originalPrice;
        order.setOriginalPrice(originalPrice);
        order.setFinalPrice(finalPrice);
        order.setTotalItem(
                sellerOrders.stream().mapToInt(SellerOrder::getTotalItem).sum()
        );
        order.setDiscountPercentage(
                discountPercentage(originalPrice, finalPrice)
        );
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());

        // 8. Cuối cùng save cả cây
        return orderRepository.save(order);
    }

    @Scheduled(fixedRate = 180_000)
    @Transactional
    public void confirmExpiredDeliveredSellerOrder() {
        List<SellerOrder> sellerOrders = sellerOrderRepository.findDeliveredOrdersOlderThan(
                SELLER_ORDER_STATUS.DELIVERED,
                LocalDateTime.now().minusDays(7)
        );
        log.info("Found {} delivered orders older than 7 days to complete", sellerOrders.size());

        for (SellerOrder sellerOrder : sellerOrders) {
            sellerOrder.setStatus(SELLER_ORDER_STATUS.COMPLETED);
//            sellerOrder.setCompletedDate(LocalDateTime.now()); // nếu có trường này
        }
        sellerOrderRepository.saveAll(sellerOrders);
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
        return orderRepository.findById(orderId).orElseThrow(() -> new AppException("Order not found"));
    }

    @Override
    public List<Order> userOrdersHistory(Long userId) {
//        return orderRepository.customFindByUserId(userId, PAYMENT_STATUS.COMPLETED);
//            return  orderRepository.findByUserIdAndPaymentDetails_PaymentStatus(userId, PAYMENT_STATUS.COMPLETED);
        return null;

    }

    SellerOrderRepository sellerOrderRepository;
    SellerOrderMapper sellerOrderMapper;

    @Override
    public List<UserOrderHistoryResponse> sellerOrders(Seller seller, SELLER_ORDER_STATUS status) {
        return sellerOrderRepository.findBySellerAndStatus(seller, status).stream().map(
                sellerOrderMapper::toUserOrderHistoryResponse
        ).toList();
    }

    @Override
    public List<SellerOrder> userOrders(User user, SELLER_ORDER_STATUS status) {
        return sellerOrderRepository.findByUserIdAndStatus(user.getId(), status);
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public SellerOrder getSellerOrderById(Long sellerOrderId) {
        return sellerOrderRepository.findById(sellerOrderId).orElseThrow(() -> new AppException("Seller order not found"));
    }

    @Override
    public SellerOrder updateSellerOrderStatus(SELLER_ORDER_STATUS status, Long sellerOrderId, Seller seller) {
        SellerOrder sellerOrder = getSellerOrderById(sellerOrderId);
        if (!seller.getId().equals(sellerOrder.getSeller().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        sellerOrder.setStatus(status);
        if(status == SELLER_ORDER_STATUS.DELIVERED){
            sellerOrder.setDeliveredDate(LocalDateTime.now());
        }

        return sellerOrderRepository.save(sellerOrder);
    }


    @Override
    public Order cancelOrder(Order order, User user, String cancelReason) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        //Product
        productService.restoreStock(order);

        // Handle order items
        List<SellerOrder> sellerOrders = order.getSellerOrders();
        for (SellerOrder sellerOrder : sellerOrders) {
            sellerOrder.setCancelReason(cancelReason);
            sellerOrder.setStatus(SELLER_ORDER_STATUS.CANCELLED);
        }
        return orderRepository.save(order);
    }

    @Override
    public SellerOrder sellerCancelOrder(Seller seller, String cancelReason, Long sellerOrderId) {
        SellerOrder sellerOrder = getSellerOrderById(sellerOrderId);
        if (!sellerOrder.getSeller().getId().equals(seller.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        //Product
        sellerOrder.getOrderItems().forEach(productService::restoreStock);

        // Handle order items
        sellerOrder.setCancelReason(cancelReason);
        sellerOrder.setStatus(SELLER_ORDER_STATUS.CANCELLED);


        return sellerOrderRepository.save(sellerOrder);

    }

    @Override
    public SellerOrder userCancelSellerOrder(User user, String cancelReason, Long sellerOrderId) {
        SellerOrder sellerOrder = getSellerOrderById(sellerOrderId);
        if (!sellerOrder.getUserId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (sellerOrder.getStatus() != SELLER_ORDER_STATUS.PENDING) {
            throw new AppException("Can't cancel this order");
        }
        //Product
        sellerOrder.getOrderItems().forEach(productService::restoreStock);

        // Handle order items
        sellerOrder.setCancelReason(cancelReason);
        sellerOrder.setStatus(SELLER_ORDER_STATUS.CANCELLED);


        return sellerOrderRepository.save(sellerOrder);
    }

    @Transactional
    @Override
    public SellerOrder userConfirmSellerOrder(User user, Long sellerOrderId) {
        SellerOrder sellerOrder = getSellerOrderById(sellerOrderId);
        if (!sellerOrder.getUserId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (sellerOrder.getStatus() != SELLER_ORDER_STATUS.DELIVERED) {
            throw new AppException("Can't confirm this order");
        }
        sellerOrder.setStatus(SELLER_ORDER_STATUS.COMPLETED);
        productService.deliveredOrder(sellerOrder);
        sellerReportService.deliveredOrder(sellerOrder);
        transactionService.createTransaction(sellerOrder);

        return sellerOrderRepository.save(sellerOrder);
    }

    @Override
    public OrderItem findOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElseThrow(() -> new AppException("Order item not found"));
    }

    @Override
    public void proceedPayment(Order order, User user) {
        for (SellerOrder sellerOrder : order.getSellerOrders()) {
            sellerOrder.setStatus(SELLER_ORDER_STATUS.PENDING);
            sellerOrder.getPaymentDetails().setPaymentStatus(PAYMENT_STATUS.COMPLETED);
            sellerOrder.getPaymentDetails().setPaymentDate(LocalDateTime.now());
        }
        orderRepository.save(order);
    }

    @Override
    public SellerOrder approveSellerOrder(Long sellerOrderId, Seller seller) {
        SellerOrder sellerOrder = getSellerOrderById(sellerOrderId);
        if (sellerOrder.getIsApproved()) {
            throw new AppException("Order item is already approved");
        }
        sellerOrder.setIsApproved(true);
        sellerOrder.setStatus(SELLER_ORDER_STATUS.CONFIRMED);

        return sellerOrderRepository.save(sellerOrder);
    }
}
