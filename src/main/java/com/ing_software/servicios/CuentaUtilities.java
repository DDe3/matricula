package com.ing_software.servicios;


import com.ing_software.entity.*;
import com.ing_software.repo.CuentaRepository;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.se.SeContainer;
import javax.inject.Inject;

@RequestScoped
public class CuentaUtilities {

    @Inject
    SeContainer container;

    public Cuenta crearCuenta(String nombre, String password) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(nombre);
        cuenta.setPassword(password);
        return cuenta;
    }

    public void bindEstudiante(Cuenta c, Estudiante p) {
        c.setOwner1(p);
    }

    public void bindProfesor(Cuenta c, Profesor p) {
        c.setOwner2(p);
    }

    public void bindAdministrativo(Cuenta c, Administrativo p) {
        c.setOwner3(p);
    }

}
