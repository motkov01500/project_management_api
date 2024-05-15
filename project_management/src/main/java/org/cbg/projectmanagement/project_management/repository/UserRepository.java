package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.entity.*;

import java.util.List;

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
        return getEntityByCriteria(query).getResultList();
    }

    public List<User> getUsersRelatedToProjectAndNotAddedToTask(Long taskId, String projectKey) {
        TypedQuery<User> userTypedQuery = getEntityManager()
                .createQuery("FROM User U JOIN U.projects P JOIN P.taskSet T " +
                        "WHERE P.key = :projectKey AND :taskId NOT IN (SELECT T2.id FROM User U2 JOIN U2.tasks T2 WHERE U2.id = U.id)", User.class);
        userTypedQuery.setParameter("projectKey", projectKey);
        userTypedQuery.setParameter("taskId", taskId);
        return userTypedQuery.getResultList();
    }

    public List<User> getUsersRelatedToProjectAndNotAddedToMeeting(Long meetingId, String projectKey) {
        TypedQuery<User> userTypedQuery = getEntityManager()
                .createQuery("FROM User U JOIN U.projects P JOIN P.meetingSet M " +
                        "WHERE P.key = :projectKey AND :meetingId NOT IN (SELECT M2.id FROM User U2 JOIN U2.meetings M2 WHERE U2.id = U.id)", User.class);
        userTypedQuery.setParameter("projectKey", projectKey);
        userTypedQuery.setParameter("meetingId", meetingId);
        return userTypedQuery.getResultList();
    }

    public List<User> getUsersNotToProject(String projectKey) {
        TypedQuery<User> userTypedQuery = getEntityManager()
                .createQuery("FROM User U JOIN U.projects P " +
                        "WHERE :projectKey NOT IN (SELECT P2.key FROM User U2 JOIN U2.projects P2 WHERE U2.id = U.id)", User.class);
        userTypedQuery.setParameter("projectKey", projectKey);
        return userTypedQuery.getResultList();
    }

    public boolean isUserInMeeting(String username, Long meetingId) {
        TypedQuery<User> userTypedQuery = getEntityManager()
                .createQuery("FROM User U JOIN U.meetings M " +
                        "WHERE U.username=:username AND M.id=:meetingId", User.class);
        userTypedQuery.setParameter("username", username);
        userTypedQuery.setParameter("meetingId", meetingId);
        return !(userTypedQuery.getResultList().isEmpty());
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
        return getEntityByCriteria(query).getResultList();
    }

    public List<User> getUsersRelatedToProject(String key, int pageNumber, int offset) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Project> userProjectJoin = userRoot.join(User_.projects);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate getUsersRelatedToProject = criteriaBuilder.equal(userProjectJoin.get(Project_.key), key);
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, getUsersRelatedToProject));
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber - 1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<User> getUsersRelatedToMeeting(Long id, int pageNumber, int offset) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Meeting> userMeetingJoin = userRoot.join(User_.meetings);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate getUsersRelatedToMeeting = criteriaBuilder.equal(userMeetingJoin.get(Meeting_.id), id);
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, getUsersRelatedToMeeting));
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<User> getRelatedToTask(Long taskId, int pageNumber, int offset) {
        TypedQuery<User> userTypedQuery = getEntityManager()
                .createQuery("FROM User U JOIN U.tasks T " +
                        "WHERE T.id=:taskId AND U.isDeleted!=true", User.class);
        userTypedQuery.setParameter("taskId",taskId);
        if(pageNumber == 0 && offset == 0) {
            return userTypedQuery.getResultList();
        }
        return userTypedQuery
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public boolean isUserExists(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        query.select(userRoot)
                .where(criteriaBuilder.equal(userRoot.get(User_.username), username));
        return !getEntityByCriteria(query).getResultList().isEmpty();
    }

    public List<User> findAll(int pageNumber, int offset) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate excludeAdminUser = criteriaBuilder.notEqual(userRoot.get(User_.username), "admin");
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, excludeAdminUser));
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }
}
