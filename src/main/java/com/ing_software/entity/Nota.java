package com.ing_software.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="materia_id")
    Materia asignatura;

    String codmat;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "estudiante_id")
    Estudiante pert;

    Double aporteDeberes;
    Double aporteLecciones;
    Double aporteExamen;
    Double notaSupletorio=0.0;
    Double notaFinal;


    public void calcular(Double deber, Double leccion, Double examen) {
        aporteDeberes = deber*0.30;
        aporteLecciones = leccion*0.35;
        aporteExamen = examen*0.35;
        notaFinal = aporteDeberes + aporteLecciones + aporteExamen;
    }

    public void suple(Double supletorio) {
        if (notaFinal<7.0 && notaFinal>5.0) {
            notaSupletorio = supletorio*0.35;
            notaFinal = notaFinal + supletorio*0.35;
        }

    }

    public String dameEstado() {
        return notaFinal>=7.0 ? "Aprobado" : "Reprobado";
    }

    public boolean aprobacion() {
        return aporteDeberes + aporteLecciones + aporteExamen > 14;
    }

    @Override
    public String toString() {
        return  " Deberes: " + aporteDeberes +
                " Lecciones :" + aporteLecciones +
                " Examen: " + aporteExamen +
                " Supletorio: " + notaSupletorio +
                " Nota final: " + notaFinal;
    }
}
