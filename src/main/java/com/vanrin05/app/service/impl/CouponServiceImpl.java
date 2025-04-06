package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.COUPON_TYPE;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.cart.Coupon;
import com.vanrin05.app.model.User;
import com.vanrin05.app.repository.CartRepository;
import com.vanrin05.app.repository.CouponRepository;
import com.vanrin05.app.repository.UserRepository;
import com.vanrin05.app.service.CouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponServiceImpl implements CouponService {
    CouponRepository couponRepository;
    CartRepository cartRepository;
    UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new AppException("Coupon not found"));
        Cart cart = cartRepository.findByUserId(user.getId());

        if(user.getUsedCoupons().contains(coupon)){
            throw new AppException("Coupon already used");
        }

        if(orderValue <= coupon.getMinimumOrderValue()){
            throw new AppException("Valid for minimum order values: "+ coupon.getMinimumOrderValue());
        }

        if(coupon.isActive() && LocalDateTime.now().isBefore(coupon.getValidityStartDate()) && LocalDateTime.now().isAfter(coupon.getValidityEndDate())){
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            cart.setTotalSellingPrice(calculateDiscountPrice(cart.getTotalSellingPrice(), coupon.getCouponType(), coupon.getDiscountValue()));
            cart.setCouponCode(code);
            cartRepository.save(cart);
        }

        throw new AppException("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new AppException("Coupon not found"));
        Cart cart = cartRepository.findByUserId(user.getId());


        cart.setTotalSellingPrice(calculateDiscountPrice(cart.getTotalSellingPrice(), coupon.getCouponType(), coupon.getDiscountValue()));
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    private Long calculateDiscountPrice(Long totalSellingPrice, COUPON_TYPE couponType, Long discountValue) {

        if (couponType.equals(COUPON_TYPE.PERCENTAGE)) {
            return Math.round(totalSellingPrice * discountValue / 100.0);
        } else {
            return discountValue;
        }
    }

    @Override
    public Coupon findCouponById(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(() -> new AppException("Coupon not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteCoupon(Long couponId) {
        findCouponById(couponId);
        couponRepository.deleteById(couponId);
    }
}
