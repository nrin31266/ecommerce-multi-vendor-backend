package com.vanrin05.dto.request;

import com.vanrin05.domain.USER_ROLE;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SigningRequest {
    String email;
    String otp;
}
