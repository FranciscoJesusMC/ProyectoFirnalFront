package com.ecommerce.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class EcommerceException extends RuntimeException{

    private final Map<String,String> errors;
}
