package com.ing_software.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Profesor extends Persona {

    String titulo;

    String observaciones;

    @OneToOne(mappedBy = "encargado")
    Curso curso;

    @OneToOne(mappedBy = "owner2")
    Cuenta cuenta;

    @Override
    public String reporte() {
        return this.toString();
    }
}
