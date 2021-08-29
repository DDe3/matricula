package com.ing_software.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Estudiante extends Persona {

    private Boolean estado;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="representante_id")
    private Representante representante;

    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;


    @OneToMany(mappedBy = "registro", cascade = CascadeType.ALL)
    private List<Matricula> matriculasRegistradas;

    @OneToOne(mappedBy = "owner1", cascade = CascadeType.ALL)
    private Cuenta cuenta;

    @OneToMany(mappedBy = "pert", cascade = CascadeType.ALL)
    private List<Nota> notas;

    @Override
    public String reporte() {
        return this.toString();
    }
    public void addMatricula(Matricula m) {
        this.matriculasRegistradas.add(0,m);
    }



}
