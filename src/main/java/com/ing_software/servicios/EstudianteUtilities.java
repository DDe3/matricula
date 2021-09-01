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
        return null;
    }

    public Resultado anularMatricula() {
        return null;
    }

    public Resultado modificarInformacion(Estudiante estudiante) {
        return null;
    }
}
