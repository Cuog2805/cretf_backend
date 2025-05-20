package com.cretf.backend.utils;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

@Data
@Builder
public class Response<T> {
    @NotNull
    private boolean success;
    private String message;
    @NotNull
    private T data;
    @NotNull
    private long total;
    private boolean isThrowException;

    public static <T> Response<T> ok() {
        return Response.<T>builder()
                .success(true)
                .message("OK")
                .total(1L)
                .build();
    }

    public static <T> Response<T> error(String errMessage) {
        return Response.<T>builder()
                .success(true)
                .message(errMessage)
                .isThrowException(true)
                .build();
    }

    public static <T> Response<T> ok(T data) {
        long size = 1L;
        if (data != null && data.getClass().isArray()) {
            size = Array.getLength(data);
        } else if (data instanceof Collection) {
            size = ((Collection<?>) data).size();
        }
        return Response.<T>builder()
                .success(true)
                .data(data)
                .message("OK")
                .total(size)
                .build();
    }

    public static <T> Response<List<T>> ok(Page<T> data) {
        return Response.<List<T>>builder()
                .success(true)
                .data(data.getContent())
                .message("OK")
                .total((Long) data.getTotalElements())
                .build();
    }
}
