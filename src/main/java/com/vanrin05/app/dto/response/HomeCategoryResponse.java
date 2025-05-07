package com.vanrin05.app.dto.response;

import com.vanrin05.app.model.HomeCategory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
//HOME_FURNITURE
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeCategoryResponse {
    List<HomeCategory> electronics;
    List<HomeCategory> men;
    List<HomeCategory> women;
    List<HomeCategory> homeFurniture;
}
