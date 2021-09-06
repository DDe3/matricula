package com.ing_software.servicios;

import com.ing_software.entity.Materia;
import com.ing_software.repo.MateriaRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MateriaUtilities {

    @Inject
    MateriaRepository repo;


    public List<Materia> findAll() {
        return repo.findAll();
    }


}
