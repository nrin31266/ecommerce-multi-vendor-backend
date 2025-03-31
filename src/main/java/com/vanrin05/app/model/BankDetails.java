package com.vanrin05.app.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class BankDetails {
    String accountNumber;
    String accountHolderName;
    String ifscCode;

    String swiftCode;
}
