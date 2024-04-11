package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import org.cbg.projectmanagement.project_management.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Stateless
public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super(User.class);
    }

    public List<User> findAll() {
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        query.select(userRoot);
        return getEntityByCriteriaa(query).getResultList();
    }

    public Optional<User> getUserByUsername(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        query.select(userRoot)
                .where(criteriaBuilder.equal(userRoot.get(User_.username),username));
        return Optional.ofNullable(getEntityByCriteriaa(query).getSingleResult());
    }

    public List<User> getUnassignedUsers() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Project> projectRoot = subquery.from(Project.class);
        Join<Project,User> projectUserJoin = projectRoot.join(Project_.users);
        subquery.select(projectUserJoin.get(User_.id));
        query.select(userRoot)
                .where(criteriaBuilder.not(userRoot.get(User_.id).in(subquery)));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<User> getUsersRelatedToProject(String key) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User,Project> userProjectJoin = userRoot.join(User_.projects);
        query.select(userRoot)
                .where(criteriaBuilder.equal(userProjectJoin.get(Project_.key),key));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<User> getUsersRelatedToMeeting(Long id) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Meeting> userMeetingJoin = userRoot.join(User_.meetings);
        query.select(userRoot)
                .where(criteriaBuilder.equal(userMeetingJoin.get(Meeting_.id),id));
        return getEntityByCriteriaa(query).getResultList();
    }
}
