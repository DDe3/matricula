package com.ing_software.repo;


import com.ing_software.entity.Administrativo;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import javax.enterprise.context.RequestScoped;

@RequestScoped
@Repository
public interface AdministrativoRepository extends EntityRepository<Administrativo, Integer> {
}
