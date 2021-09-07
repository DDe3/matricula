package com.ing_software.servicios;

import com.ing_software.Resultado;
import com.ing_software.entity.*;
import com.ing_software.repo.ProfesorRepository;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProfesorUtilities {

    @Inject
    ProfesorRepository repo;

    @Inject
    CuentaUtilities servicioCuenta;

    public void subirNota(Estudiante est, Nota nota, Materia m) {

    }

    @TransactionScoped
    public void saveProfesor(Profesor p) {
        repo.save(p);
    }

    public List<Profesor> findAll() {
        return repo.findAll();
    }

    @TransactionScoped
    public void persistirProfesor(Cuenta c, Profesor a){
        servicioCuenta.bindProfesor(c,a);
        repo.save(a);
    }



}
