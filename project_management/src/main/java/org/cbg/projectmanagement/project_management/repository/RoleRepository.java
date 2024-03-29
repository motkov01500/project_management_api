package org.cbg.projectmanagement.project_management.repository;


import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RoleRepository extends BaseRepository<Role>{

    @Override
    public String getEntityName() {
        return Role.class.getSimpleName();
    }

    public Optional<Role> getRoleByName(String name) throws NotFoundResourceException {
        String query = "from Role R WHERE R.name = :name";
        Map<String, Object> criteria  = new HashMap<>();
        criteria.put("username", name);
        List<Role> userList = getEntityByCriteria(query,criteria);
        return Optional.ofNullable(userList.get(0));
    }
}
