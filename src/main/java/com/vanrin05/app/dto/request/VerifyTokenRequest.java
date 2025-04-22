package com.vanrin05.app.dto.request;

import com.vanrin05.app.exception.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class VerifyTokenRequest {
    @NotBlank(message = "TOKEN_IS_REQUIRED")
    String jwtToken;
}
