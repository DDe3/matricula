package com.ing_software.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
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
