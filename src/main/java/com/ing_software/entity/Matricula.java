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


    public String toString() {
        String status;
        if (this.estado) {
            status = "Activo";
        } else {
            status = "No activo";
        }
        return "Ciclo: "+ciclo+" | Fecha de registro: " +f_matricula.toString()+ " Curso: " + paralelo + status;
    }

}
