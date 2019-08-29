package com.example.mybase.http;


/**
 * @author DIYUN
 */
public interface HttpErrorListener {
    /**
     * 错误或异常抛出
     * @param data
     */
    void error(Throwable data);
}