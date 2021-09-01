package com.ing_software.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Administrativo extends Persona{

    String cargo;
    String observaciones;

    @OneToOne(mappedBy = "owner3", cascade = CascadeType.ALL)
    Cuenta cuenta;

    @Override
    public String reporte() {
        return this.toString();
    }
}
