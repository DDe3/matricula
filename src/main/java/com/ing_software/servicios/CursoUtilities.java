package com.ing_software.servicios;

import com.ing_software.entity.*;
import com.ing_software.repo.CursoRepository;
import org.apache.deltaspike.data.api.EntityManagerDelegate;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CursoUtilities {

    @Inject
    CursoRepository repo;

    @Inject
    EstudianteUtilities est;

    @Inject
    EntityManager em;




    @TransactionScoped
    public void registrarCurso(Curso c) {
        System.out.println("Lista de size: "+ c.getEstudiantes().size()+ " recibida en registrarCurso: "+c.getEstudiantes());
        em.getTransaction().begin();
        if (!(c.getEstudiantes()==null)) {
            bindDataEstudiantes(c.getEstudiantes(),c);
        }
        if (!(c.getEncargado()==null)) {
            bindDataProfesor(c.getEncargado(),c);
        }
        em.persist(c);
        em.getTransaction().commit();
    }

    @TransactionScoped
    public void crearCursoAndSave(String aula, Character paralelo, String ciclo, Profesor encargado, List<Materia> materias, List<Estudiante> estudiantes, Integer cupo) {

        Curso c = new Curso();
        c.setAula(aula);
        c.setParalelo(paralelo);
        c.setCiclo(ciclo);
        c.setDescripcion(paralelo+ciclo);
        c.setEstado(true);

        c.setMaterias(materias);
        c.setEstudiantes(estudiantes);
        c.setCupo(cupo);

        registrarCurso(c);
    }


    private void bindDataEstudiantes(List<Estudiante> estudiantes, Curso c) {
        List<Estudiante> perro = new ArrayList<>(estudiantes);
        List<Estudiante> aux = perro.stream()
                .peek( (e) ->  {
                    e.setCurso(c);
                    est.matriculaAdd(e);
                }).collect(Collectors.toList());

        c.setEstudiantes(aux);
    }

    public void bindDataProfesor(Profesor p, Curso c) {
        p.setCurso(c);
        c.setEncargado(p);
    }

//    public void bindData(List<Materia> materias, Curso c) {
//        materias.stream().forEach(x -> {
//            x.setCur(c);
//        });
//    }

    public Optional<Curso> findByAula(String aula) {
        return repo.findPorAula(aula.toUpperCase());
    }

    public void removeMaterias(Curso c, Materia a) {
        List<Materia> materias = c.getMaterias();
        List<Curso> cursos = a.getCur();
        materias.remove(a);
        cursos.remove(c);
        a.setCur(cursos);
        c.setMaterias(materias);
        em.getTransaction().begin();
        em.persist(a);
        em.persist(c);
        em.getTransaction().commit();
    }

    public void removeProfesor(Curso c, Profesor p) {
        c.setEncargado(null);
        p.setCurso(null);
        em.getTransaction().begin();
        em.persist(c);
        em.persist(p);
        em.getTransaction().commit();
    }

}
