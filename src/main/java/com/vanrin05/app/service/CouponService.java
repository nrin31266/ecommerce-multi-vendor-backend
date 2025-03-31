package com.vanrin05.app.service;

import com.vanrin05.app.model.Cart;
import com.vanrin05.app.model.Coupon;
import com.vanrin05.app.model.User;

import java.util.List;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user);
    Cart removeCoupon(String code, User user);

    Coupon findCouponById(Long couponId);

    Coupon createCoupon(Coupon coupon);

    List<Coupon> findAllCoupons();

    void deleteCoupon(Long couponId);

}
