package com.ing_software.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(unique = true)
    String aula;

    Character paralelo;
    String descripcion;
    String ciclo;
    Boolean estado;

    Integer cupo;


    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Estudiante> estudiantes = new ArrayList<>();;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    Profesor encargado;

    @ManyToMany(mappedBy = "cur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Materia> materias = new ArrayList<>();


    public void addMateria(List<Materia> m) {
        List<Materia> aux = new ArrayList<>(m);
        for (int i = 0; i < aux.size(); i++) {
            if (!materias.contains(aux.get(i))) {
                materias.add(aux.get(i));
            }
        }
    }

    @Override
    public String toString() {
        return  "Aula :'" + aula +
                " Paralelo: " + paralelo +
                " Ciclo: '" + ciclo;
    }

    public void addEstudiante(Estudiante e) {
        if (!estudiantes.contains(e)) {
            estudiantes.add(e);
        }
    }
}
