package org.cbg.projectmanagement.project_management.repository;


import org.cbg.projectmanagement.project_management.entity.Role;

public class RoleRepository extends BaseRepository<Role>{

    @Override
    public String getEntityName() {
        return Role.class.getSimpleName();
    }
}
