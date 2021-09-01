package com.ing_software.abstraccion;

class Failure<T> implements Result<T> {

    private final T value;
    public Failure(T t) {
        this.value=t;
    }
    @Override
    public void bind(Effect<T> success, Effect<T> failure) {
        failure.apply(value);
    }
}