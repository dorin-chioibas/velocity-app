package com.velocity.app.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.velocity.app.model.Bird;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class SightingResponseDto {
    Long id;
    Bird bird;
    String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;
}
