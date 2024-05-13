package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.Role_;

import java.util.Optional;

@Stateless
public class RoleRepository extends BaseRepository<Role> {

    public RoleRepository() {
        super(Role.class);
    }

    public Optional<Role> getRoleByName(String name) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = getCriteriaQuery();
        Root<Role> roleRoot = criteriaQuery.from(Role.class);
        criteriaQuery.select(roleRoot)
                .where(criteriaBuilder.equal(roleRoot.get(Role_.name),name));
        return Optional.of(getEntityByCriteriaa(criteriaQuery).getSingleResult());
    }
}
