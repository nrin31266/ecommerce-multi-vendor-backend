package com.vanrin05.dto.request;

import com.vanrin05.domain.ACCOUNT_STATUS;
import com.vanrin05.domain.USER_ROLE;
import com.vanrin05.model.Address;
import com.vanrin05.model.BankDetails;
import com.vanrin05.model.BusinessDetails;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSellerRequest {
    String sellerName;
    String mobile;
    String email;
    String password;
    BusinessDetails businessDetails;
    BankDetails bankDetails;
    Address pickupAddress;
    String GSTIN;
}
