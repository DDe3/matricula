package com.ing_software.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Profesor extends Persona {

    String titulo;

    String observaciones;

    @OneToOne(mappedBy = "encargado", cascade = CascadeType.ALL)
    Curso curso;

    @OneToOne(mappedBy = "owner2", cascade = CascadeType.ALL)
    Cuenta cuenta;

    @Override
    public String reporte() {
        return this.toString();
    }
}
