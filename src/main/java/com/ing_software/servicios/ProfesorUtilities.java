package com.ing_software.servicios;

import com.ing_software.Resultado;
import com.ing_software.entity.*;
import com.ing_software.repo.ProfesorRepository;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.inject.Inject;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;

public class ProfesorUtilities {

    @Inject
    ProfesorRepository repo;

    public void subirNota(Estudiante est, Nota nota, Materia m) {

    }

    public List<Profesor> findAll() {
        return repo.findAll();
    }



}
