package com.hotel.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ErrorResponseFactory {

    static final String VALIDATION_ERROR_MESSAGE = "Validation error, please check your request.";
    static final String SERVER_ERROR_MESSAGE = "Oops, something went terribly wrong.";

    public static ResponseEntity<ErrorResponse> response(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message));
    }

    public static ResponseEntity<ErrorResponse> badRequestResponse() {
        return response(BAD_REQUEST, VALIDATION_ERROR_MESSAGE);
    }

    public static ResponseEntity<ErrorResponse> internalServerError() {
        return response(INTERNAL_SERVER_ERROR, SERVER_ERROR_MESSAGE);
    }
}
