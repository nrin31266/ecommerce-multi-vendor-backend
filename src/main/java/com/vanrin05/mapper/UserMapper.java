package com.vanrin05.mapper;

import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(SignupRequest signupRequest);
}
