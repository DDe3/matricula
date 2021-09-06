package com.ing_software.servicios;

import com.ing_software.Resultado;
import com.ing_software.entity.Estudiante;
import com.ing_software.entity.Matricula;
import com.ing_software.repo.MatriculaRepository;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MatriculaUtilities {

    @Inject
    MatriculaRepository repo;


    @TransactionScoped
    public void removerMatricula(Matricula m) {
        repo.remove(m);
    }

    @TransactionScoped
    public void registrarMatricula(Matricula m) {
        repo.save(m);
    }



}
