package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.cbg.projectmanagement.project_management.entity.Role;
import org.cbg.projectmanagement.project_management.entity.TaskStatus;
import org.cbg.projectmanagement.project_management.entity.TaskStatus_;

import java.util.List;
import java.util.Optional;

@Stateless
public class TaskStatusRepository extends BaseRepository<TaskStatus>{

    public TaskStatusRepository() {
        super(TaskStatus.class);
    }

    public List<TaskStatus> findAll() {
        CriteriaQuery<TaskStatus> query = getCriteriaQuery();
        Root<TaskStatus> taskStatusRoot = query.from(TaskStatus.class);
        query.select(taskStatusRoot);
        return getEntityByCriteriaa(query).getResultList();
    }

    public Optional<TaskStatus> findByName(String name) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<TaskStatus> query = getCriteriaQuery();
        Root<TaskStatus> taskStatusRoot = query.from(TaskStatus.class);
        query.select(taskStatusRoot)
                .where(criteriaBuilder.equal(taskStatusRoot.get(TaskStatus_.name), name));
        return Optional.ofNullable(getEntityByCriteriaa(query).getSingleResult());
    }
}
