package com.ems.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ems.app.dto.APIResponse;

@ControllerAdvice // Global exception handling for all controllers
public class GlobalExceptionHandler {

    // Handles all unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Object>> handleException(Exception ex) {
        APIResponse<Object> response = new APIResponse<>();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500 status
        response.setMessage("Something went wrong: " + ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
