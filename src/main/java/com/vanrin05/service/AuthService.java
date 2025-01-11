package com.vanrin05.service;

import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.mapper.UserMapper;
import com.vanrin05.model.User;
import com.vanrin05.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    UserMapper userMapper;

    public User signup(SignupRequest req) {
        User user = userMapper.toUser(req);


        return userRepository.save(user);
    }
}
