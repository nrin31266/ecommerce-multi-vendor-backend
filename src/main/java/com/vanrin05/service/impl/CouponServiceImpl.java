package com.vanrin05.service.impl;

import com.vanrin05.domain.COUPON_TYPE;
import com.vanrin05.exception.AppException;
import com.vanrin05.model.Cart;
import com.vanrin05.model.Coupon;
import com.vanrin05.model.User;
import com.vanrin05.repository.CartRepository;
import com.vanrin05.repository.CouponRepository;
import com.vanrin05.repository.UserRepository;
import com.vanrin05.service.CouponService;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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

            double discountPrice = coupon.getCouponType().equals(COUPON_TYPE.PERCENTAGE) ?
                    (cart.getTotalSellingPrice() * coupon.getDiscountValue() / 100) : (coupon.getDiscountValue());

            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);
        }

        throw new AppException("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new AppException("Coupon not found"));
        Cart cart = cartRepository.findByUserId(user.getId());
        double discountPrice = coupon.getCouponType().equals(COUPON_TYPE.PERCENTAGE) ?
                (cart.getTotalSellingPrice() * coupon.getDiscountValue() / 100) : (coupon.getDiscountValue());
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountPrice);
        cart.setCouponCode(null);
        return cartRepository.save(cart);
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
