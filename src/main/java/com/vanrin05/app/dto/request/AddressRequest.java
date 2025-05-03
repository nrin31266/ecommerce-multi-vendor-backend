package com.vanrin05.app.dto.request;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {

    Long id;

    String name;

    String phoneNumber;

    String street;

    String ward;

    String district;

    String province;

    String postalCode;


    Boolean isDefault;
}


