package org.cbg.projectmanagement.project_management;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;

@Path("/hello-world")
public class HelloResource {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");

    @PersistenceContext
    private EntityManager entityManager;

    @GET
    @Produces("application/json")
    public List hello() {
        entityManager.getTransaction().begin();
        List roles = entityManager.createQuery("SELECT R.name FROM Role R").getResultList();
        entityManager.getTransaction().commit();
        return roles;
    }
}