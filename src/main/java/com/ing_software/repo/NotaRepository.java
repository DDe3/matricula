package com.ing_software.repo;

import com.ing_software.entity.Administrativo;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface NotaRepository extends EntityRepository<Administrativo,Integer> {
}
