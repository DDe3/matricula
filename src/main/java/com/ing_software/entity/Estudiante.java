package com.ing_software.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Estudiante extends Persona {

    private Boolean estado;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="representante_id")
    private Representante representante;

    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "curso_id")
    private Curso curso;


    @OneToMany(mappedBy = "registro", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false)
    private List<Matricula> matriculasRegistradas = new ArrayList<>();

    @OneToOne(mappedBy = "owner1", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cuenta cuenta;

    @OneToMany(mappedBy = "pert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Nota> notas = new ArrayList<>();

    @Override
    public String reporte() {
        return this.toString();
    }
    public void addMatricula(Matricula m) {
        boolean aux=true;
        for (Matricula mat :matriculasRegistradas) {
            if (mat.getAula().equals(m.getAula())) {
                aux = false;
                break;
            }
        }
        if (aux) {
            this.matriculasRegistradas.add(0,m);
        }
    }

    public void addNota(Nota n) {
        boolean cond = true;
        for (Nota not: notas) {
            if (not.getCodmat().equalsIgnoreCase(n.codmat)) {
                cond = false;
                break;
            }
        }
        if (cond) {
            notas.add(n);
        }
    }


    @Override
    public String toString() {
        return
                "CDI='" + cedula + '\'' +
                ", NOMBRE :'" + nombre;
    }
}
