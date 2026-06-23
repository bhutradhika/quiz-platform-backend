package com.quiz.platform.shared.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "data", "message", "errorCode", "errorMessage"})
public class ApiResponse {
    private boolean success = true;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Object data;
    private String message;
    private String errorCode;
    private String errorMessage;
}
