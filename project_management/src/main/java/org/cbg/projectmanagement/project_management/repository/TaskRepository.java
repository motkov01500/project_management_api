package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.entity.*;
import org.cbg.projectmanagement.project_management.enums.SortOrder;

import java.util.List;

@Stateless
public class TaskRepository extends BaseRepository<Task> {

    public TaskRepository() {
        super(Task.class);
    }

    public List<Task> getAllTasksRelatedToProject(String projectKey, int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, Project> taskProjectJoin = taskRoot.join(Task_.project);
        Predicate notDeletedTasks = criteriaBuilder.notEqual(taskRoot.get(Task_.isDeleted), true);
        Predicate getTasksRelatedToCurrentProject = criteriaBuilder
                .equal(taskProjectJoin.get(Project_.key), projectKey);
        query.select(taskRoot)
                .where(criteriaBuilder.and(notDeletedTasks,getTasksRelatedToCurrentProject));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = taskRoot.get(sort.getColumn());
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

    public boolean isUserInTask(Long userId, Long taskId) {
        TypedQuery<Meeting> meetingTypedQuery = getEntityManager()
                .createQuery("FROM Task T JOIN T.users U " +
                        "WHERE U.id=:userId AND T.id=:taskId AND T.isDeleted!=true", Meeting.class);
        meetingTypedQuery.setParameter("userId",userId);
        meetingTypedQuery.setParameter("taskId", taskId);
        return !(meetingTypedQuery.getResultList().isEmpty());
    }

    public int countNotFinishedTasksRelatedToUserAndProject(String username, String projectKey) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, Project> taskProjectJoin = taskRoot.join(Task_.project);
        Join<Task, User> taskUserJoin = taskRoot.join(Task_.users);
        Predicate equalProject = criteriaBuilder.equal(taskProjectJoin.get(Project_.key),projectKey);
        Predicate equalUsername = criteriaBuilder.equal(taskUserJoin.get(User_.username),username);
        Predicate notDeletedTasks = criteriaBuilder.notEqual(taskRoot.get(Task_.isDeleted), true);
        Predicate notFinishedTasks = criteriaBuilder.notEqual(taskRoot.get(Task_.progress), 100);
        query.select(taskRoot)
                .where(criteriaBuilder.and(equalProject,equalUsername, notDeletedTasks, notFinishedTasks));
        return getEntityByCriteria(query)
                .getResultList()
                .size();
    }

    public List<Task> getTasksRelatedToCurrentUserAndProject(String username, String projectKey,
                                                             int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, Project> taskProjectJoin = taskRoot.join(Task_.project);
        Join<Task, User> taskUserJoin = taskRoot.join(Task_.users);
        Predicate notDeletedTasks = criteriaBuilder.notEqual(taskRoot.get(Task_.isDeleted), true);
        Predicate getTasksRelatedToCurrentUser = criteriaBuilder
                .equal(taskUserJoin.get(User_.username), username);
        Predicate getTasksRelatedToCurrentProject = criteriaBuilder
                .equal(taskProjectJoin.get(Project_.key), projectKey);
        query.select(taskRoot)
                .where(criteriaBuilder.and(getTasksRelatedToCurrentProject,
                        getTasksRelatedToCurrentUser, notDeletedTasks));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = taskRoot.get(sort.getColumn());
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

    public List<Task> getTasksRelatedToUser(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, User> taskUserJoin = taskRoot.join(Task_.users);
        query.select(taskRoot)
                .where(criteriaBuilder.equal(taskUserJoin.get(User_.username),username));
        return getEntityByCriteria(query).getResultList();
    }


    public boolean isUserAssignedAlreadyToTask(Long userId, Long taskId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, User> taskUserJoin = taskRoot.join(Task_.users);
        Predicate isUserInTask = criteriaBuilder.equal(taskUserJoin.get(User_.id), userId);
        Predicate isTaskInUser = criteriaBuilder.equal(taskRoot.get(Task_.id), taskId);
        query.select(taskRoot)
                .where(criteriaBuilder.and(isTaskInUser, isUserInTask));
        return !(getEntityByCriteria(query)
                .getResultList()
                .isEmpty());
    }

    public List<Task> findAll(int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        query.select(taskRoot)
                .where(criteriaBuilder.notEqual(taskRoot.get(Task_.isDeleted), true));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = taskRoot.get(sort.getColumn());
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

    public void deleteByProjectId(Long projectId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, Project> taskProjectJoin = taskRoot.join(Task_.project);
        Predicate notDeletedTasks = criteriaBuilder.notEqual(taskRoot.get(Task_.isDeleted), true);
        Predicate getTasksRelatedToProject = criteriaBuilder.equal(taskProjectJoin.get(Project_.id), projectId);
        query.select(taskRoot)
                .where(criteriaBuilder.and(getTasksRelatedToProject, notDeletedTasks));
        List<Task> tasks = getEntityByCriteria(query).getResultList();
        tasks.forEach(task -> {
            task.setIsDeleted(true);
            update(task);
        });
    }
}