package org.cbg.projectmanagement.project_management.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Optional;

public abstract class BaseRepository<T> {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");

    @Getter
    @PersistenceContext
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    Class<T> entity;

    public BaseRepository(Class<T> entity) {
        this.entity = entity;
    }

    public void save(T entity) {
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            entityManager.persist(entity);
            entityManager.flush();
            entityManager.refresh(entity);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            entityManager.merge(entity);
            entityManager.flush();
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
            if (entityToRemove != null) {
                Method isDeleted = entityToRemove.getClass()
                        .getDeclaredMethod("setIsDeleted", Boolean.class);
                isDeleted.invoke(entityToRemove, Boolean.TRUE);
            }
            entityManager.merge(entityToRemove);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TypedQuery<T> getEntityByCriteria(CriteriaQuery<T> query) {
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
