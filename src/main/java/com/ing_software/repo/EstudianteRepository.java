package com.ing_software.repo;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Estudiante;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface EstudianteRepository extends EntityRepository<Estudiante,Integer> {
}
