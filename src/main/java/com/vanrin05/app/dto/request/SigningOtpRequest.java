package com.vanrin05.app.dto.request;

import com.vanrin05.app.domain.USER_ROLE;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SigningOtpRequest {
    String email;
    USER_ROLE role;
}
