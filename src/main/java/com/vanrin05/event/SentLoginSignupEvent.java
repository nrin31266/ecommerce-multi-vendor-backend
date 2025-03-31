package com.vanrin05.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SentLoginSignupEvent {
    String subject;
    String email;
    Map<String, Object> variables;
}
