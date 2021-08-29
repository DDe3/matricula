package com.ing_software.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    int id;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="estudiante_id")
    Estudiante owner1;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="profesor_id")
    Profesor owner2;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="administrativo_id")
    Administrativo owner3;

    String nombre;
    String password;

}
