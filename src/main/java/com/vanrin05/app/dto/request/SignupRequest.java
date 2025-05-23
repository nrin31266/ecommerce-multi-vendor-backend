package com.vanrin05.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    String email;
    @NotBlank(message = "FULL_NAME_IS_REQUIRED")
    String fullName;
    String otp;
}
