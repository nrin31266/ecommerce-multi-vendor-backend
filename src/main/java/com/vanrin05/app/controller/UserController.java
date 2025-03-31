package com.vanrin05.app.controller;

import com.vanrin05.app.model.User;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(userService.findUserByJwtToken(jwt));
    }
}
