package com.vanrin05.controller;

import com.razorpay.RazorpayException;
import com.vanrin05.dto.response.ApiResponse;
import com.vanrin05.model.*;
import com.vanrin05.service.PaymentService;
import com.vanrin05.service.SellerReportService;
import com.vanrin05.service.impl.SellerService;
import com.vanrin05.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;
    UserService userService;
    SellerService sellerService;
    SellerReportService sellerReportService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandle(@PathVariable String paymentId,
                                                            @RequestParam String paymentLinkId,
                                                            @RequestHeader("Authorization") String jwtToken
                                                            ) throws RazorpayException {
        User user = userService.findUserByJwtToken(jwtToken);

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.proceedPayment(paymentOrder, paymentId, paymentLinkId);

        if (paymentSuccess) {
            for(Order order : paymentOrder.getOrders()) {
                //
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport sellerReport= sellerReportService.getSellerReport(seller);
                sellerReport.setTotalOrders(sellerReport.getTotalOrders() + 1);
                sellerReport.setTotalEarnings(sellerReport.getTotalEarnings() + order.getTotalSellingPrice());
                sellerReport.setTotalSales(sellerReport.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(sellerReport);

            }
        }

        return ResponseEntity.ok(ApiResponse.builder()
                        .message("Payment successful")
                .build());
    }

}
