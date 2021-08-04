package com.ing_software;

public class Profesor extends Persona {

    String titulo;
    Contrato contrato;
    String observaciones;
    Curso curso;

    @Override
    public String reporte() {
        return this.toString();
    }
}
