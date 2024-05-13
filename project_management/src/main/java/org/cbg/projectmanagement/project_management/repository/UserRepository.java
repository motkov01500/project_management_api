package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import org.cbg.projectmanagement.project_management.entity.*;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Stateless
public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super(User.class);
    }

    public List<User> getUserByUsername(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Predicate getByUsername = criteriaBuilder.equal(userRoot.get(User_.username), username);
        Predicate notDeletedUser = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        query.select(userRoot)
                .where(criteriaBuilder.and(getByUsername, notDeletedUser));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<User> getUnassignedUsers() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Project> projectRoot = subquery.from(Project.class);
        Join<Project, User> projectUserJoin = projectRoot.join(Project_.users);
        subquery.select(projectUserJoin.get(User_.id));
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate notInProjectUsers = criteriaBuilder.not(userRoot.get(User_.id).in(subquery));
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, notInProjectUsers));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<User> getUsersRelatedToProject(String key) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Project> userProjectJoin = userRoot.join(User_.projects);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate getUsersRelatedToProject = criteriaBuilder.equal(userProjectJoin.get(Project_.key), key);
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, getUsersRelatedToProject));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<User> getUsersRelatedToMeeting(Long id) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Meeting> userMeetingJoin = userRoot.join(User_.meetings);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate getUsersRelatedToMeeting = criteriaBuilder.equal(userMeetingJoin.get(Meeting_.id), id);
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, getUsersRelatedToMeeting));
        return getEntityByCriteriaa(query).getResultList();
    }

    public boolean isUserExists(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        query.select(userRoot)
                .where(criteriaBuilder.equal(userRoot.get(User_.username), username));
        return !getEntityByCriteriaa(query).getResultList().isEmpty();
    }

    public List<User> findAll() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate excludeAdminUser = criteriaBuilder.notEqual(userRoot.get(User_.username), "admin");
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, excludeAdminUser));
        return getEntityByCriteriaa(query).getResultList();
    }
}
