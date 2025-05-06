package com.vanrin05.app.controller;

import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.SellerOrder;
import com.vanrin05.app.service.OrderService;
import com.vanrin05.app.service.impl.SellerService;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/users/orders")
public class UserOrderController {
    OrderService orderService;
    SellerService sellerService;
    UserService userService;

    @PutMapping("/seller-order/cancel/{sellerOrderId}")
    public ResponseEntity<SellerOrder> cancelSellerOrder(@PathVariable("sellerOrderId") Long sellerOrderId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(orderService.userCancelSellerOrder(user, "User cancel", sellerOrderId));
    }

    @PutMapping("/seller-order/confirm/{sellerOrderId}")
    public ResponseEntity<SellerOrder> confirmSellerOrder(@PathVariable("sellerOrderId") Long sellerOrderId, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(orderService.userConfirmSellerOrder(user, sellerOrderId));
    }

}
