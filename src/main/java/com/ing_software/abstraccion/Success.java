package com.ing_software.abstraccion;

class Success<T> implements Result<T> {

    private final T value;
    public Success(T t) {
        this.value=t;
    }

    @Override
    public void bind(Effect<T> success, Effect<T> failure) {
        success.apply(value);
    }
}
