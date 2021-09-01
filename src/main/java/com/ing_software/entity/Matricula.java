package com.ing_software.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String ciclo;
    Date f_matricula;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "estudiante_id")
    Estudiante registro;
    Boolean estado;
    Character paralelo;

}
