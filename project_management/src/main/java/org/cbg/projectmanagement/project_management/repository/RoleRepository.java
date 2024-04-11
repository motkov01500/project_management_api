package org.cbg.projectmanagement.project_management.repository;


import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Stateless
public class RoleRepository extends BaseRepository<Role> {

    public RoleRepository() {
        super(Role.class);
    }

    public Optional<Role> getRoleByName(String name) throws NotFoundResourceException {
        String query = "from Role R WHERE R.name = :name";
        Map<String, Object> criteria  = new HashMap<>();
        criteria.put("name", name);
        List<Role> userList = getEntityByCriteria(query,criteria);
        return Optional.ofNullable(userList.get(0));
    }

    public List<Role> findAll() {
        CriteriaQuery<Role> query = getCriteriaQuery();
        Root<Role> roleRoot = query.from(Role.class);
        query.select(roleRoot);
        return getEntityByCriteriaa(query).getResultList();
    }
}
