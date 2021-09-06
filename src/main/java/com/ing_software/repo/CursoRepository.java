package com.ing_software.repo;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Curso;
import com.ing_software.entity.Estudiante;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends EntityRepository<Curso,Integer> {


    @Query("select c from Curso c where c.aula = ?1")
    Optional<Curso> findPorAula(String aula);
}
