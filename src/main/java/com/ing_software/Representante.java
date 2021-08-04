package com.ing_software;

public class Representante extends Persona {

    String lugarTrabajo;
    Estudiante representado;

    @Override
    public String reporte() {
        return this.toString();
    }


}
