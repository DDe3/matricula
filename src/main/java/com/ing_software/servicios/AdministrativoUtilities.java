package com.ing_software.servicios;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Cuenta;
import com.ing_software.repo.AdministrativoRepository;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AdministrativoUtilities {

    @Inject
    AdministrativoRepository repo;

    @Inject
    CuentaUtilities cuentaUtilities;

    @Inject
    EntityManager em;


    public List<Administrativo> findAll() {
        return this.repo.findAll();
    }

    public Optional<Administrativo> findByCedula(String cdi) {
        return repo.findPorCedula(cdi);
    }


    @TransactionScoped
    public void persistirAdmin(Cuenta c, Administrativo a){
        cuentaUtilities.bindAdministrativo(c,a);
        repo.save(a);
    }

    public void clear() {
        em.clear();
    }

    @TransactionScoped
    public void saveAdmin(Administrativo a) {
        repo.save(a);
    }

}
