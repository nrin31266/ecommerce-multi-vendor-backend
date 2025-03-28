package com.event;

import com.vanrin05.model.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReportEvent {

    Long paymentId;
    Set<Order> orders;
}
