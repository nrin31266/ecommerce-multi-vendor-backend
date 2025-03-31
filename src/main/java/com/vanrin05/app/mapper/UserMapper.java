package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.SignupRequest;
import com.vanrin05.app.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(SignupRequest signupRequest);
}
