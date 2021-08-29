package com.ing_software.servicios;

import com.ing_software.Resultado;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Estudiante;
import com.ing_software.entity.Representante;
import com.ing_software.repo.EstudianteRepository;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.se.SeContainer;
import javax.inject.Inject;
import java.sql.Date;

@RequestScoped
public class EstudianteUtilities {

    @Inject
    CuentaUtilities c;

    public Estudiante crearEstudiante(String nombre, String telefono, Representante r, String correo, String password) {
        Estudiante e = new Estudiante();
        e.setNombre(nombre);
        e.setTelefono(telefono);
        Cuenta cuenta = c.crearCuenta(correo, password);
        e.setCuenta(cuenta);
        e.setMail(cuenta.getNombre());
        e.setEstado(true);
        e.setRepresentante(r);
        return e;
    }

    public Resultado anularMatricula() {
        return null;
    }

    public Resultado modificarInformacion(Estudiante estudiante) {
        return null;
    }
}
