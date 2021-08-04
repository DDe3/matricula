package com.ing_software;

public abstract class Persona {

    String cid;
    String nombre;
    String genero;
    Integer edad;
    String nacionalidad;
    String telefono;
    String mail;


    public Persona() {
    }

    public abstract String reporte();


}