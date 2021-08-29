package com.ing_software.entity;


import javax.persistence.*;

@Entity
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToOne()
    @JoinColumn(name="materia_id")
    Materia asignatura;

    @ManyToOne(fetch = FetchType.LAZY)
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
