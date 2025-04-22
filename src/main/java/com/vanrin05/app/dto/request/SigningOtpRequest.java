package com.vanrin05.app.dto.request;

import com.vanrin05.app.domain.USER_ROLE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SigningOtpRequest {
    @Email(message = "EMAIL_INVALID")
    @NotBlank(message = "EMAIL_REQUIRED")
    String email;
    USER_ROLE role;
}
