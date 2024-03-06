package org.cbg.projectmanagement.project_management.repository;


import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.request.UserRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

public class RoleRepository extends BaseRepository<Role>{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String getEntityName() {
        return Role.class.getSimpleName();
    }

    public Role findById(Long id) {
        return entityManager.find(Role.class,id);
    }
}
