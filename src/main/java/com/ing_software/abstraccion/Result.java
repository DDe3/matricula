package com.ing_software.abstraccion;

public interface Result<T> {

    void bind(Effect<T> success, Effect<T> failure);

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    static <T> Failure<T> failure(T value) {
        return new Failure<>(value);
    }

}
