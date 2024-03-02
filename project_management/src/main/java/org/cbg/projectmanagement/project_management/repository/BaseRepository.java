package org.cbg.projectmanagement.project_management.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public abstract class BaseRepository<T> {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");

    @PersistenceContext
    private EntityManager entityManager;

    public abstract String getEntityName();

    public void create(T entity) {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void delete(Long id) {
        try {
            String query = "delete from " + getEntityName();
            entityManager.createQuery(query + " where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public List<T> findAll() {
        return entityManager.createQuery("from " + getEntityName()).getResultList();
    }
}
