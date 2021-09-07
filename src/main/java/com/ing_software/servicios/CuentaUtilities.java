package com.ing_software.servicios;


import com.ing_software.Principal;
import com.ing_software.entity.*;
import com.ing_software.repo.CuentaRepository;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.se.SeContainer;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class CuentaUtilities implements ServicioCuenta {

    @Inject
    CuentaRepository repo;


    public Optional<Cuenta> findCuenta(String nombre, String password) throws ExecutionException, InterruptedException {
        CompletableFuture<Optional<Cuenta>> ret = CompletableFuture.supplyAsync(
                () -> repo.findByName(nombre, password), Principal.e);
        return ret.get();
    }


    public CompletableFuture<List<Cuenta>> nombresDisponibles() {
        return CompletableFuture.supplyAsync(() -> repo.findAll(), Principal.e);
    }

    @Override
    public Optional<Cuenta> findCuentaNombre(String nombre) {
        return repo.findPorNombre(nombre);
    }

    @Override
    public List<Cuenta> findAll() {
        return repo.findAll();
    }


    @TransactionScoped
    public void crearCuenta(String nombre, String password) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(nombre);
        cuenta.setPassword(password);
        repo.save(cuenta);
    }

    public void bindEstudiante(Cuenta c, Estudiante p) {
        p.setCuenta(c);
        c.setOwner1(p);
    }

    public void bindProfesor(Cuenta c, Profesor p) {
        p.setCuenta(c);
        c.setOwner2(p);
    }

    public void bindAdministrativo(Cuenta c, Administrativo p) {
        p.setCuenta(c);
        c.setOwner3(p);
    }

}
