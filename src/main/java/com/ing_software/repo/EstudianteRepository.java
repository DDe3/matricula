package com.ing_software.repo;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Estudiante;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends EntityRepository<Estudiante,Integer> {


    @Query("select e from Estudiante e where e.cedula = ?1")
    Optional<Estudiante> findPorCedula(String cdi);

}
