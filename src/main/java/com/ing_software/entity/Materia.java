package com.ing_software.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String nombre;
    String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    Curso cur;


}
