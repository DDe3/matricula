package com.ing_software;

import com.ing_software.entity.Administrativo;
import com.ing_software.repo.AdministrativoRepository;
import com.ing_software.repo.CuentaRepository;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Principal {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence
                .createEntityManagerFactory("matricula");
        EntityManager em = factory.createEntityManager();

        System.out.println("hola");
//        Administrativo adm = new Administrativo();
    }
}
