package com.ing_software.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Administrativo extends Persona{

    String cargo;
    String observaciones;

    @OneToOne(mappedBy = "owner3")
    Cuenta cuenta;

    @Override
    public String reporte() {
        return this.toString();
    }
}
