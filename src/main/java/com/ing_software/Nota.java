package com.ing_software;

public class Nota {

    Materia materia;
    Estudiante estudiante;
    Double aporteDeberes;
    Double aporteLecciones;
    Double aporteExamen;
    Double notaSupletorio;
    Double notaFinal;

    public boolean aprobacion() {
        return aporteDeberes + aporteLecciones + aporteExamen > 14;
    }

}
