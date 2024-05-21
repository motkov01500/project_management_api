package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.entity.*;
import org.cbg.projectmanagement.project_management.enums.SortOrder;

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
                        "WHERE P.key = :projectKey AND U.id NOT IN (SELECT U2.id FROM User U2 JOIN U2.tasks T2 WHERE U2.id = U.id AND T2.id=:taskId) AND U.isDeleted!=true AND U.username != 'admin'", User.class);
        userTypedQuery.setParameter("projectKey", projectKey);
        userTypedQuery.setParameter("taskId", taskId);
        return userTypedQuery.getResultList();
    }

    public List<User> getUsersRelatedToProjectAndNotAddedToMeeting(Long meetingId, String projectKey) {
        TypedQuery<User> userTypedQuery = getEntityManager()
                .createQuery("FROM User U JOIN U.projects P JOIN P.meetingSet M " +
                        "WHERE P.key = :projectKey AND :meetingId NOT IN (SELECT M2.id FROM User U2 JOIN U2.meetings M2 WHERE U2.id = U.id) AND U.isDeleted!=true AND U.username != 'admin'", User.class);
        userTypedQuery.setParameter("projectKey", projectKey);
        userTypedQuery.setParameter("meetingId", meetingId);
        return userTypedQuery.getResultList();
    }

    public List<User> getUsersNotToProject(String projectKey) {
        TypedQuery<User> userTypedQuery = getEntityManager()
                .createQuery("FROM User U  " +
                        "WHERE U.id NOT IN (SELECT U2.id FROM User U2 JOIN U2.projects P2 WHERE U2.id = U.id AND P2.key=:projectKey) AND  U.isDeleted != true AND U.username != 'admin'", User.class);
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

    public List<User> getUsersRelatedToProject(String key, int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Project> userProjectJoin = userRoot.join(User_.projects);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate getUsersRelatedToProject = criteriaBuilder.equal(userProjectJoin.get(Project_.key), key);
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, getUsersRelatedToProject));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = userRoot.get(sort.getColumn());
            query.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber - 1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<User> getUsersRelatedToMeeting(Long id, int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Meeting> userMeetingJoin = userRoot.join(User_.meetings);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate getUsersRelatedToMeeting = criteriaBuilder.equal(userMeetingJoin.get(Meeting_.id), id);
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, getUsersRelatedToMeeting));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = userRoot.get(sort.getColumn());
            query.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }

    public List<User> getRelatedToTask(Long taskId, int pageNumber, int offset,Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Join<User, Task> userTaskJoin = userRoot.join(User_.tasks);
        Predicate equalTaskId = criteriaBuilder.equal(userTaskJoin.get(Task_.id), taskId);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted),true);
        query.select(userRoot)
                        .where(criteriaBuilder.and(equalTaskId,notDeletedUsers));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = userRoot.get(sort.getColumn());
            query.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
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

    public List<User> findAll(int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<User> query = getCriteriaQuery();
        Root<User> userRoot = query.from(User.class);
        Predicate notDeletedUsers = criteriaBuilder.notEqual(userRoot.get(User_.isDeleted), true);
        Predicate excludeAdminUser = criteriaBuilder.notEqual(userRoot.get(User_.username), "admin");
        query.select(userRoot)
                .where(criteriaBuilder.and(notDeletedUsers, excludeAdminUser));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = userRoot.get(sort.getColumn());
            query.orderBy(SortOrder.ASCENDING.equals(sort.getOrder()) ?
                    criteriaBuilder.asc(sortColumn) :
                    criteriaBuilder.desc(sortColumn));
        }
        if(pageNumber == 0 && offset == 0) {
            return getEntityByCriteria(query).getResultList();
        }
        return getEntityByCriteria(query)
                .setFirstResult((pageNumber-1) * offset)
                .setMaxResults(offset)
                .getResultList();
    }
}
