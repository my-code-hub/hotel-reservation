package com.hotel.errors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static lombok.AccessLevel.PACKAGE;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = PACKAGE)
class ErrorResponse {
    private String message;
}
