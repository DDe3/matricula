package com.ing_software.abstraccion;

public interface Effect<T> {
    void apply(T t);
}
