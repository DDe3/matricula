package com.ing_software;

public class Administrativo extends Persona{

    String cargo;
    Contrato contrato;
    String observaciones;

    @Override
    public String reporte() {
        return this.toString();
    }
}
