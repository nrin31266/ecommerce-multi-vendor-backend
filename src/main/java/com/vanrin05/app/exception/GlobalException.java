package com.vanrin05.app.exception;

import com.vanrin05.app.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> appException(AppException appException, WebRequest webRequest) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(appException.getMessage());
        apiResponse.setCode(appException.getErrorCode().getCode());
//        apiResponse.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(apiResponse, appException.getErrorCode().getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

}
