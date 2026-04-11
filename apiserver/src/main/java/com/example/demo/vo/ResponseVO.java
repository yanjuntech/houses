package com.example.demo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    public ResponseVO() {
    }

    public ResponseVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>(200, "success", null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(200, "success", data);
    }

    public static <T> ResponseVO<T> error(int code, String message) {
        return new ResponseVO<>(code, message, null);
    }

    public static <T> ResponseVO<T> error(String message) {
        return new ResponseVO<>(500, message, null);
    }
}
