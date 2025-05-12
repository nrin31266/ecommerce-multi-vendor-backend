package com.vanrin05.app.dto;

import com.vanrin05.app.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistItemDto {
    Long id;
    ProductDto product;
    User user;
    LocalDateTime addedAt;
}
