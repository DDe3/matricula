package com.ing_software.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String ciclo;
    Date f_matricula;
    String aula;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "estudiante_id")
    Estudiante registro;
    Boolean estado;
    Character paralelo;


    String aprobado;


    public String toString() {
        return "Ciclo: "+ciclo+" | Fecha de registro: " +f_matricula.toString()+ " Curso: " + paralelo + " " + estado;
    }

}
