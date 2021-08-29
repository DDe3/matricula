package com.ing_software.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String aula;
    Character paralelo;
    String descripcion;
    String ciclo;
    Boolean estado;


    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    List<Estudiante> estudiantes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profesor_id")
    Profesor encargado;

    @OneToMany(mappedBy = "cur", cascade = CascadeType.ALL)
    private List<Materia> materias;

}
