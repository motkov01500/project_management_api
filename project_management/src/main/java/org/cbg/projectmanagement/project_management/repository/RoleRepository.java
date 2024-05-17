package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.Role_;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.entity.User_;

import java.util.List;
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
        return Optional.of(getEntityByCriteria(criteriaQuery).getSingleResult());
    }

    public Role getRolesByUsername(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = getCriteriaQuery();
        Root<Role> roleRoot = criteriaQuery.from(Role.class);
        Join<Role, User> roleUserJoin = roleRoot.join(Role_.user);
        criteriaQuery.select(roleRoot)
                .where(criteriaBuilder.equal(roleUserJoin.get(User_.username),username));
        return getEntityByCriteria(criteriaQuery).getSingleResult();
    }
}
