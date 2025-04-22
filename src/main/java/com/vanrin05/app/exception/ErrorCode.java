package com.vanrin05.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    DEFAULT_EXCEPTION(9998, "Default exception", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(9001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    TOKEN_MISSING(9002, "Token is missing", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(9003, "Unauthorized. You don't have permission", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(9004, "Token has expired", HttpStatus.UNAUTHORIZED),
    WRONG_OTP(9005, "Wrong otp", HttpStatus.UNAUTHORIZED),
    // Authentication User

    // Seller
    SELLER_NOT_FOUND(9005, "Seller not found with %s", HttpStatus.NOT_FOUND),
    SELLER_ALREADY_EXISTS(9006, "Seller already exists", HttpStatus.BAD_REQUEST),

    // Customer
    CUSTOMER_NOT_FOUND(9007, "Customer not found", HttpStatus.NOT_FOUND),
    CUSTOMER_ALREADY_EXISTS(9008, "Customer already exists", HttpStatus.BAD_REQUEST),

    // Cart
    CART_NOT_FOUND(9009, "Cart not found", HttpStatus.NOT_FOUND),

    CATEGORY_NOT_FOUND(9010, "Category not found", HttpStatus.NOT_FOUND),

    EMAIL_INVALID(9011, "Email is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_REQUIRED(9012, "Email is required", HttpStatus.BAD_REQUEST),
//    TOKEN_INVALID(9013, "Token is invalid", HttpStatus.BAD_REQUEST),
//    TOKEN_IS_EXPIRED(9014, "Token is expired", HttpStatus.BAD_REQUEST),
    TOKEN_IS_REQUIRED(9015, "Token is required", HttpStatus.BAD_REQUEST),
    FULL_NAME_IS_REQUIRED(9016, "Full name is required", HttpStatus.BAD_REQUEST),
    ;


    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.statusCode=httpStatusCode;
    }


    final int code;
    final String message;
    private final HttpStatusCode statusCode;

    /**
     * Format the error message with additional arguments.
     * @param args The arguments to format the message.
     * @return A formatted error message.
     */
    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}
