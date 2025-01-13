package com.vanrin05.service;

import com.vanrin05.configuration.JwtProvider;
import com.vanrin05.model.User;
import com.vanrin05.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    JwtProvider jwtProvider;


    public User findUserByJwtToken (String jwtToken) {
        String email = jwtProvider.getEmailFromJwtToken(jwtToken);
        User user = this.findUserByEmail(email);


        return user;
    }

    public User findUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found with email " + email));

        return user;
    }

}
