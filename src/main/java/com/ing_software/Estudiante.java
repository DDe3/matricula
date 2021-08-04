package com.ing_software;

import java.sql.Date;
import java.util.List;

public class Estudiante extends Persona {

    Date f_nacimiento;
    Date f_inscripcion;
    Boolean estado;
    Representante representante;
    String observaciones;
    Curso curso;
    List<Matricula> matriculasRegistradas;

    @Override
    public String reporte() {
        return this.toString();
    }
}
