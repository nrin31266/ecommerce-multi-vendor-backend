package com.vanrin05.app.service.impl;

import com.vanrin05.app.model.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.Wishlist;
import com.vanrin05.app.repository.WishlistRepository;
import com.vanrin05.app.service.WishListService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class WishlistServiceImpl implements WishListService {
    WishlistRepository wishlistRepository;

    @Override
    public Wishlist getWishListByUser(User user) {
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId());
        if (wishlist == null) {
            wishlist = createWishList(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist createWishList(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);

        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {
        Wishlist wishlist = getWishListByUser(user);

        if(wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
        }else{
            wishlist.getProducts().add(product);
        }

        return wishlistRepository.save(wishlist);
    }
}
