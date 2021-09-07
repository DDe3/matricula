package com.ing_software.repo;


import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Estudiante;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import javax.enterprise.context.RequestScoped;
import java.util.Optional;

@Repository
public interface AdministrativoRepository extends EntityRepository<Administrativo, Integer> {


    @Query("select a from Administrativo a where a.cedula = ?1")
    Optional<Administrativo> findPorCedula(String cdi);
}
