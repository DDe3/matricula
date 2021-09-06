package com.ing_software.repo;


import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Matricula;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface MatriculaRepository extends EntityRepository<Matricula,Integer> {
}
