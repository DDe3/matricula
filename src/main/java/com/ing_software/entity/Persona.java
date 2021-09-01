package com.ing_software.entity;


import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class Persona {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    int id;
    @NotNull @Column(unique = true)
    String cedula;

    String nombre;
    String telefono;
    String mail;


    public Persona() {
    }

    public abstract String reporte();


}