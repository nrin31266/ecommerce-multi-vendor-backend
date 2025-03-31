package com.vanrin05.event;

import com.vanrin05.app.model.Order;
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
