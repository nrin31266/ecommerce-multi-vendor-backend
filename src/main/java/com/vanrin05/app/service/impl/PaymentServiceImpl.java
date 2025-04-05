package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.ORDER_STATUS;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.PaymentOrder;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.repository.*;
import com.vanrin05.app.service.SellerReportService;
import com.vanrin05.app.service.TransactionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.*;
import com.vanrin05.app.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentOrderRepository paymentOrderRepository;
    OrderRepository orderRepository;
    VNPayService vnPayService;
    private String stripeSecretKey = "sk_test_51R52WvPvjKDrWoaF1R2WPy4ivyRAlWcAYsPLO0t0A7ma3bTIPN8HQFbwpPyRwhR848Sem8oggdCyojGfLWgQSiMV00X4eCr53r";
    KafkaTemplate<String, Object> kafkaTemplate;
    ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    SellerReportRepository sellerReportRepository;
    TransactionService transactionService;
    SellerReportService sellerReportService;


    @Override
    public PaymentOrder createPaymentOrder(User user, List<Order> orders, PAYMENT_METHOD paymentMethod) {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentExpiry(LocalDateTime.now().plusMinutes(30));
        paymentOrder.setUser(user);
        paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.PENDING);
        paymentOrder.setAmount(amount);
        for (Order order : orders) {
            order.setPaymentOrder(paymentOrder);
        }
        paymentOrder.setOrders(orders);
        paymentOrder.setPaymentMethod(paymentMethod);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    @Transactional
    public PaymentOrder cancelPaymentOrder  (Long paymentId, User user) {
//        PaymentOrder paymentOrder = findById(paymentId);
//        if(!user.getId().equals(paymentOrder.getUser().getId())) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
//        paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.FAILED);
//        List<Order> orders = paymentOrder.getOrders();
//        for (Order order : orders) {
//            order.setOrderStatus(ORDER_STATUS.CANCELLED);
//            order.setCancelReason("User cancelled payment");
//            List<Product> products = new ArrayList<>();
//            for (OrderItem orderItem : order.getOrderItems()) {
//                Product product = orderItem.getProduct();
//                product.setQuantity(product.getQuantity() + orderItem.getQuantity());
//            }
//            productRepository.saveAll(products);
//        }
//
//        orderRepository.saveAll(orders);
//        return paymentOrderRepository.save(paymentOrder);
        return null;
    }



    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void cancelExpiredPayment() {
//        List<PaymentOrder> expiredPayments = paymentOrderRepository
//                .customFindByExpiredOnlinePayment(
//                        PAYMENT_ORDER_STATUS.PENDING,
//                        LocalDateTime.now()
//                );
//        Map<Long, Integer> productQuantityToAdd = new HashMap<>();
//        List<Order> orders = new ArrayList<>();
//        expiredPayments.forEach(payment -> {
//            payment.getOrders().forEach(order -> {
//                order.setCancelReason("Payment expired");
//                order.setOrderStatus(ORDER_STATUS.CANCELLED);
//                order.getOrderItems().forEach(item -> {
//                    productQuantityToAdd.merge(
//                            item.getProduct().getId(),
//                            item.getQuantity(),
//                            Integer::sum
//                    );
//                });
//                orders.add(order);
//            });
//
//
//            payment.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.FAILED);
//        });
//
//        orderRepository.saveAll(orders);
//        productQuantityToAdd.forEach(productRepository::incrementProductQuantity);
//        paymentOrderRepository.saveAll(expiredPayments);
    }



    @Transactional
    @Override
    public Boolean proceedPayment(Long paymentId)  {
        PaymentOrder paymentOrder = findById(paymentId);
        List<Order> orders = paymentOrder.getOrders();
        if (paymentOrder.getPaymentOrderStatus().equals(PAYMENT_ORDER_STATUS.PENDING)) {
            for (Order order : orders) {
                order.getPaymentDetails().setPaymentStatus(PAYMENT_STATUS.COMPLETED);
                order.setOrderStatus(ORDER_STATUS.PLACED);
            }
            orderRepository.saveAll(orders);
            paymentOrder.setPaymentOrderStatus(PAYMENT_ORDER_STATUS.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            Map<Long, Seller> sellers = sellerRepository.findAllByIdIn(orders.stream().map(Order::getSellerId).collect(Collectors.toSet())).stream().collect(Collectors.toMap(Seller::getId, seller -> seller));
            Map<Seller, SellerReport> sellerReports = sellerReportRepository.findBySellerIn(sellers.values().stream().toList()).stream().collect(Collectors.toMap(SellerReport::getSeller, sellerReport -> sellerReport));
            for (Order order : orders) {
                transactionService.createTransaction(order);
                Seller seller = sellers.get(order.getSellerId());
                SellerReport sellerReport = sellerReports.get(seller);
                sellerReport.setTotalOrders(sellerReport.getTotalOrders() + 1);
                sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
                sellerReport.setTotalSales(sellerReport.getTotalSales() + order.getOrderItems().size());
                sellerReport.setTotalTransactions(sellerReport.getTotalTransactions() + 1);
            }
            sellerReportService.updateSellerReports(sellerReports.values().stream().toList());
            return true;
        }
        return false;
    }

    @Override
    public String createVNPaymentLink(User user, Long amount, Long paymentId, Map<String, String> params) {
        String orderType = params.get("orderType");
        String bankCode = params.get("bankCode");
        String orderInfo = params.get("orderInfo");
        return vnPayService.createPaymentUrl(amount, orderInfo, orderType, bankCode, paymentId);
    }


    @Override
    public String createStripePaymentLink(User user, Long amount, Long paymentId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond())
                .setSuccessUrl("http://localhost:5173/payment-success/" + paymentId)
                .setCancelUrl("http://localhost:5173/payment-cancel/" + paymentId)
                .addLineItem(SessionCreateParams.LineItem.builder()

                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()

                                .setCurrency("vnd")
                                .setUnitAmount(amount)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Thanh toán đơn hàng " + paymentId)
                                        .build())
                                .build())
                        .build())
                .putMetadata("payment_id", String.valueOf(paymentId))
                .build();

        Session session = Session.create(sessionCreateParams);

        return session.getUrl();
    }





    @Override
    public PaymentOrder findById(Long paymentId) {
        PaymentOrder paymentOrder= paymentOrderRepository.findById(paymentId).orElseThrow(()->new AppException("Payment not found"));
        return paymentOrder;
    }


    @Override
    public List<Order> findAllOrdersInPaymentOrder(PaymentOrder paymentOrder) {
        return orderRepository.findAllByPaymentOrder(paymentOrder);
    }



    @Override
    public List<PaymentOrder> findUserPaymentOrders(User user) {
        return paymentOrderRepository.findByUser(user);
    }


}
