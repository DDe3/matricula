package com.ing_software.entity;


import javax.persistence.*;

@Entity
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="materia_id")
    Materia asignatura;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "estudiante_id")
    Estudiante pert;

    Double aporteDeberes;
    Double aporteLecciones;
    Double aporteExamen;
    Double notaSupletorio;
    Double notaFinal;

    public boolean aprobacion() {
        return aporteDeberes + aporteLecciones + aporteExamen > 14;
    }

}
