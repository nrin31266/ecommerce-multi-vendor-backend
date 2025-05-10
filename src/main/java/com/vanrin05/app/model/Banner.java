package com.vanrin05.app.model;

import com.vanrin05.app.domain.BANNER_TARGET_TYPE;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String imageUrl;
    String title;

    BANNER_TARGET_TYPE targetType;
    String target;

    boolean active = true;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
