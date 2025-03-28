package com.vanrin05.exception;

import com.vanrin05.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exception(Exception ex, WebRequest request) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ex.printStackTrace();
        return new  ResponseEntity<>(ApiResponse.builder()
                .message(ex.getMessage() != null ? ex.getMessage() : errorCode.getMessage())
                .code(errorCode.getCode())
                .build(), errorCode.getStatusCode());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> sellerExceptionHandler(AppException appException, WebRequest webRequest) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(appException.getMessage());
        apiResponse.setCode(appException.getErrorCode().getCode());
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setDetails(webRequest.getDescription(false));
        return new ResponseEntity<>(apiResponse, appException.getErrorCode().getStatusCode());
    }
}
