package com.vanrin05.app.dto.request;

import com.vanrin05.app.domain.BANNER_TARGET_TYPE;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUpdateBannerRequest {
    String imageUrl;
    String title;

    BANNER_TARGET_TYPE targetType;
    String target;

    boolean active = true;
    Date startDate;
    Date endDate;
}
