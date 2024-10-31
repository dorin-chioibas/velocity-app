package com.velocity.app.dto.request;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class BirdRequestDto {
    String name;
    String color;
    Double weight;
    Double height;
}
