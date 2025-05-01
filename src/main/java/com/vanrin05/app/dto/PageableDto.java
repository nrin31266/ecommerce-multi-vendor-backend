package com.vanrin05.app.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableDto<T> {
    private List<T> content;  // Nội dung của trang hiện tại
    private int totalPages;   // Tổng số trang
    private long totalElements; // Tổng số phần tử trong tất cả các trang
    private int size;         // Kích thước của trang
    private int number;       // Số trang hiện tại
    private boolean first;    // Có phải là trang đầu tiên không
    private boolean last;     // Có phải là trang cuối cùng không
    private boolean empty;    // Trang có rỗng hay không
}
