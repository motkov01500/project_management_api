package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import org.cbg.projectmanagement.project_management.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class TaskRepository extends BaseRepository<Task> {

    public TaskRepository() {
        super(Task.class);
    }

    public List<Task> getTasksRelatedToUser(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        Join<Task, Project> taskProjectJoin = taskRoot.join(Task_.project);
        Join<Project, User> projectUserJoin = taskProjectJoin.join(Project_.users);
        query.select(taskRoot)
                .where(criteriaBuilder.equal(projectUserJoin.get(User_.username),username));
        return getEntityByCriteriaa(query).getResultList();
    }

    public List<Task> findAll() {
        CriteriaQuery<Task> query = getCriteriaQuery();
        Root<Task> taskRoot = query.from(Task.class);
        query.select(taskRoot);
        return getEntityByCriteriaa(query).getResultList();
    }
}
