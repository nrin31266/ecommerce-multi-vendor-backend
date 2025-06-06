package com.vanrin05.app.dto.response;

import com.vanrin05.app.domain.USER_ROLE;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String jwt;
    String message;
    USER_ROLE role;
}
