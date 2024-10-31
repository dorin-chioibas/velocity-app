package com.velocity.app.dto.response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BirdResponseDto {
    Long id;
    String name;
    String color;
    Double weight;
    Double height;
}
