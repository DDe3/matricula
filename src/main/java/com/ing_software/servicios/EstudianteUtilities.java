package com.ing_software.servicios;

import com.ing_software.Resultado;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Estudiante;
import com.ing_software.entity.Matricula;
import com.ing_software.entity.Representante;
import com.ing_software.repo.EstudianteRepository;
import org.apache.deltaspike.data.api.EntityManagerDelegate;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.se.SeContainer;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EstudianteUtilities {

    @Inject
    CuentaUtilities cuentaUtilities;

    @Inject
    EstudianteRepository repo;

    @Inject
    MatriculaUtilities matriculaUtilities;

    @Inject
    EntityManager em;

    @TransactionScoped
    public void crearEstudiante(String cedula, String nombre, String telefono, Representante r, Cuenta c) {
        Estudiante e = new Estudiante();
        e.setCedula(cedula);
        e.setNombre(nombre);
        e.setTelefono(telefono);
        e.setRepresentante(r);
        e.setMail(c.getNombre());
        e.setEstado(false);
        cuentaUtilities.bindEstudiante(c, e);
        repo.save(e);
    }

    @TransactionScoped
    public void merge(Estudiante estudiante) {
        repo.save(estudiante);
    }

    public Optional<Estudiante> findByCdi(String cdi) {
        return repo.findPorCedula(cdi);
    }


    public void matriculaAdd(Estudiante e) {
        Matricula mat = new Matricula();
        mat.setEstado(true);
        mat.setCiclo(e.getCurso().getCiclo());
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        mat.setF_matricula(date);
        mat.setAula(e.getCurso().getAula());
        mat.setParalelo(e.getCurso().getParalelo());
        mat.setRegistro(e);
        mat.setAprobado("Cursando");
        e.addMatricula(mat);
    }

    @TransactionScoped
    public void removeMatricula(Estudiante e) {
        em.getTransaction().begin();
        Matricula m = e.getMatriculasRegistradas().get(0);
        e.getMatriculasRegistradas().remove(0);
        em.remove(m);
        em.persist(e);
        em.getTransaction().commit();
    }

    @TransactionScoped
    public void borrarEstudiante(Estudiante e) {
        repo.remove(e);
    }


    public List<Estudiante> findAll() {
        return repo.findAll();
    }

    public void clear() {
        this.em.clear();
    }


}
