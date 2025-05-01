package com.vanrin05.app.controller;

import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.cart.Coupon;
import com.vanrin05.app.model.User;
import com.vanrin05.app.service.CartService;
import com.vanrin05.app.service.CouponService;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCouponController {
    CouponService couponService;
    UserService userService;
    CartService cartService;

//    @PostMapping("/apply")
//    public ResponseEntity<Cart> applyCoupon(
//            @RequestParam String apply,
//            @RequestParam String code,
//            @RequestParam double orderValue,
//            @RequestHeader("Authorization") String jwtToken
//    ){
//        User user = userService.findUserByJwtToken(jwtToken);
//        Cart cart;
//        if(apply.equals("true")){
//            cart = couponService.applyCoupon(code, orderValue, user);
//        }else{
//            cart = couponService.removeCoupon(code, user);
//        }
//
//        return ResponseEntity.ok(cart);
//    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable long id){
        couponService.deleteCoupon(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        return  ResponseEntity.ok(couponService.findAllCoupons());
    }


}
