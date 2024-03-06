package org.cbg.projectmanagement.project_management.repository;

import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseRepository<T> {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");

    @PersistenceContext
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    public abstract String getEntityName();

    public void create(T entity) {
        try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        try{
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            entityManager.merge(entity);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            String query = "delete from " + getEntityName();
            entityManager.createQuery(query + " where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> getEntityByCriteria(String query, Map<String, Object> parameters) {
        Query resultQuery = entityManager.createQuery(query);
        if (!parameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                resultQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return resultQuery.getResultList();
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + getEntityName()).getResultList();
    }

    public T findById(Long id) {
        String query = "from " + getEntityName();
        List<T> result = entityManager.createQuery(query + " where id = :id")
                .setParameter("id", id)
                .getResultList();
        if (result.isEmpty()) {
             throw new NotFoundResourceException("Resource is not found", Response.status(Response.Status.NOT_FOUND).entity("Not found").build());
        }
        return result.get(0);
    }
}
