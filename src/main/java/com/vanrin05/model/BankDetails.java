package com.vanrin05.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
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
