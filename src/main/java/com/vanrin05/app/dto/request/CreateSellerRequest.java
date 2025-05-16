package com.vanrin05.app.dto.request;

import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.BankDetails;
import com.vanrin05.app.model.BusinessDetails;
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
    BusinessDetails businessDetails;
    BankDetails bankDetails;
    Address pickupAddress;
    String taxCode;
}
