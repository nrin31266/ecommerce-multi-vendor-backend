package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.USER_ROLE;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.User;
import com.vanrin05.app.repository.SellerRepository;
import com.vanrin05.app.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CustomUserServiceImpl implements UserDetailsService {
    UserRepository userRepository;
    SellerRepository sellerRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String SELLER_PREFIX = "seller_";
        if(username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Optional<Seller> optionalSeller = sellerRepository.findByEmail(actualUsername);
            if(optionalSeller.isPresent()) {
                Seller seller = optionalSeller.get();
                return  buildUserDetail(seller.getEmail(), seller.getPassword(), seller.getRole());
            }
            throw new AppException("Seller not found with email: " + username);
        }else{
            Optional<User> optionalUser = userRepository.findByEmail(username);

            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                return buildUserDetail(user.getEmail(), user.getPassword(), user.getRole());
            }
            throw new AppException("User not found with email: " + username);
        }

    }

    private UserDetails buildUserDetail(String email, String password, USER_ROLE role) {
        if(role == null){
            role = USER_ROLE.ROLE_CUSTOMER;
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(email, password, grantedAuthorities);
    }
}
