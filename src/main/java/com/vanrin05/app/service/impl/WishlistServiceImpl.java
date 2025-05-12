package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.WishlistItemDto;
import com.vanrin05.app.dto.response.UserWishlistProductResponse;
import com.vanrin05.app.mapper.WishlistMapper;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.WishlistItem;
import com.vanrin05.app.repository.WishlistRepository;
import com.vanrin05.app.service.WishListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class WishlistServiceImpl implements WishListService {
    WishlistRepository wishlistRepository;
    WishlistMapper wishlistMapper;

    @Override
    public List<WishlistItemDto> getWishListByUser(User user) {
        return wishlistRepository.findByUser(user).stream().map(wishlistMapper::toWishlistItemDto).toList();
    }




    @Override
    public UserWishlistProductResponse addProductToWishlist(User user, Product product) {
        boolean isUserWishlist;
        Optional<WishlistItem> optionalWishlistItem = wishlistRepository.findByUserAndProduct(user, product);
        if (optionalWishlistItem.isPresent()) {
            wishlistRepository.delete(optionalWishlistItem.get());
            isUserWishlist = false;
        }else{
            WishlistItem wishlistItem = new WishlistItem();
            wishlistItem.setProduct(product);
            wishlistItem.setUser(user);
            wishlistRepository.save(wishlistItem);
            isUserWishlist = true;
        }
        return new UserWishlistProductResponse(isUserWishlist);
    }

    @Override
    public UserWishlistProductResponse isUserWishList(User user, Product product) {
        return wishlistRepository.findByUserAndProduct(user, product).isPresent() ? new UserWishlistProductResponse(true)
                : new UserWishlistProductResponse(false);
    }
}
