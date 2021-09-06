package com.ing_software.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(unique = true)
    String codigo;
    String nombre;
    String descripcion;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)

    @JoinTable(
            joinColumns = @JoinColumn(name = "materia_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id"))
    List<Curso> cur = new ArrayList<>();

    @Override
    public String toString() {
        return
                "ID: " + id +
                ", CODIGO: '" + codigo +
                ", NOMBRE: '" + nombre ;
    }

    public void addCurso(Curso c) {
        if (!cur.contains(c)) {
            this.cur.add(c);
        }
    }
}
