package com.ing_software.servicios;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Estudiante;
import com.ing_software.entity.Profesor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.spi.CurrencyNameProvider;

public interface ServicioCuenta {


    public void crearCuenta(String nombre, String password);
    public Optional<Cuenta> findCuenta(String nombre, String password) throws ExecutionException, InterruptedException;
    public CompletableFuture<List<Cuenta>> nombresDisponibles();
    public Optional<Cuenta> findCuentaNombre(String nombre);



    default public void bindEstudiante(Cuenta c, Estudiante p) {
        c.setOwner1(p);
    }
    default public void bindProfesor(Cuenta c, Profesor p) {
        c.setOwner2(p);
    }
    default public void bindAdministrativo(Cuenta c, Administrativo p) {
        c.setOwner3(p);
    }
}
