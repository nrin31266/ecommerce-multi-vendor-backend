package com.vanrin05.model;

import com.vanrin05.domain.PAYMENT_STATUS;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetails {
    String paymentId;
    String razorPaymentLinkId;
    String razorPaymentLinkReferenceId;
    String razorPaymentLinkStatus;
    String razorPaymentIdZWSP;
    PAYMENT_STATUS status;
}
