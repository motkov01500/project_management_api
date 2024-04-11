package org.cbg.projectmanagement.project_management.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseRepository<T> {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");

    @PersistenceContext
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    Class<T> entity;

    public BaseRepository(Class<T> entity) {
        this.entity = entity;
    }

    public void create(T entity) {
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            entityManager.persist(entity);
            tx.commit();
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
            T entityToRemove = entityManager.find(entity, id);
            if(entityToRemove != null) {
                entityManager.remove(entityToRemove);
            }
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

    public TypedQuery<T> getEntityByCriteriaa(CriteriaQuery<T> query) {
        return entityManager.createQuery(query);
    }

    public Optional<T> findById(Long id) {
        T entityToFind = entityManager.find(entity, id);
        return Optional.ofNullable(entityToFind);
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    public CriteriaQuery<T> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(entity);
    }
}
