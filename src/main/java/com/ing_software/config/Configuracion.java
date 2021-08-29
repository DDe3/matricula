package com.ing_software.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class Configuracion {

    @Produces
    @ApplicationScoped
    public EntityManagerFactory entityManagerFactory() {
        EntityManagerFactory factory = Persistence
                .createEntityManagerFactory("avanzada");
        return factory;
    }

    @Produces
    @ApplicationScoped
    public EntityManager entityManager(EntityManagerFactory factory) {
        EntityManager em = factory.createEntityManager();
        return em;
    }

    protected void closeEntityManager(@Disposes EntityManager entityManager)
    {
        if (entityManager.isOpen())
        {
            entityManager.close();
        }
    }

}
