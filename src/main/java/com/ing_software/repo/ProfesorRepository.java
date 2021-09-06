package com.ing_software.repo;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Estudiante;
import com.ing_software.entity.Profesor;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorRepository extends EntityRepository<Profesor,Integer> {



}
