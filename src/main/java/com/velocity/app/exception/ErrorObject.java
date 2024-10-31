package com.velocity.app.exception;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorObject {
    String message;
    Integer status;
    String details;
}
