package com.vanrin05.app.model;

import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_STATUS;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetails {
    @Enumerated(EnumType.STRING)
    PAYMENT_STATUS paymentStatus;

    @Enumerated(EnumType.STRING)
    PAYMENT_METHOD paymentMethod;
}
