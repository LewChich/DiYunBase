package com.example.mybase.http;

/**
 * 异常处理的一个类
 */
public class ApiException extends RuntimeException {

    public ApiException(String errorCode, String errorMessage) {
        super(errorMessage);
    }
}
