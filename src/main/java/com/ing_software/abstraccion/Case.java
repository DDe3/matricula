package com.ing_software.abstraccion;

import java.util.function.Supplier;

public class Case<T> {

    private Supplier<Boolean> condicion;
    private Supplier<Result<T>> accion;

    public Case(Supplier<Boolean> condicion, Supplier<Result<T>> accion) {
        this.condicion = condicion;
        this.accion = accion;
    }

    public static <T> Case<T> mcase( Supplier<Boolean> condicion, Supplier<Result<T>> accion) {
        return new Case<>(condicion,accion);
    }

    public static <T> DefaultCase<T> defaultCase( Supplier<Result<T>> accion) {
        return new DefaultCase<>(accion);
    }

    @SafeVarargs
    public static <T> Result<T> match(DefaultCase<T> defaultCase, Case<T>... cases) {
        for (Case<T> acase: cases) {
            if (acase.condicion.get()) {
                return acase.accion.get();
            }
        }
        return defaultCase.getAccion();
    }

    public Result<T> getAccion() {
        return accion.get();
    }
}


class DefaultCase<T> extends Case<T> {
    public DefaultCase(Supplier<Result<T>> accion) {
        super(()->true,accion);
    }


}
