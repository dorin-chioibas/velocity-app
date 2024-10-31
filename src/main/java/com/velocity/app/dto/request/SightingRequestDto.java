package com.velocity.app.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class SightingRequestDto {
    Long birdId;
    String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;
}
