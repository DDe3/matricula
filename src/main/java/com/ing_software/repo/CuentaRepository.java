package com.ing_software.repo;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Cuenta;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Repository
public interface CuentaRepository extends EntityRepository<Cuenta,Integer> {

    @Query("select c from Cuenta c where c.nombre = ?1 and c.password = ?2")
    Optional<Cuenta> findByName(String name, String pass);

    @Query("select c from Cuenta c where c.nombre = ?1")
    Optional<Cuenta> findPorNombre(String name);



}
