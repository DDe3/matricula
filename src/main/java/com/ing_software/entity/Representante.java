package com.ing_software.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Representante extends Persona {

    String lugarTrabajo;

    @OneToOne(mappedBy = "representante", cascade = CascadeType.ALL)
    Estudiante representado;

    @Override
    public String reporte() {
        return this.toString();
    }


}
