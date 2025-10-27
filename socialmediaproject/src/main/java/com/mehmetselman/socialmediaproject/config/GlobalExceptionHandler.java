package com.mehmetselman.socialmediaproject.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Map<String,String> handleRunTimeException(RuntimeException ex){
         Map<String, String> error = new HashMap<>();
         error.put("error", ex.getMessage());
         return error;
    }

}
