package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import org.cbg.projectmanagement.project_management.dto.Sort;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Project_;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.entity.User_;
import org.cbg.projectmanagement.project_management.enums.SortOrder;

import java.util.List;
import java.util.Optional;

@Stateless
public class ProjectRepository extends BaseRepository<Project> {

    public ProjectRepository() {
        super(Project.class);
    }

    public Optional<Project> findProjectByKey(String projectKey) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = getCriteriaQuery();
        Root<Project> projectRoot = criteriaQuery.from(Project.class);
        Predicate findProjectByKey = criteriaBuilder.equal(projectRoot.get(Project_.key), projectKey);
        criteriaQuery.select(projectRoot)
                .where(criteriaBuilder.and(findProjectByKey));
        return Optional.ofNullable(getEntityByCriteria(criteriaQuery).getSingleResult());
    }

    public List<Project> findProjectsRelatedToUser(String username, int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> query = getCriteriaQuery();
        Root<Project> projectRoot = query.from(Project.class);
        Join<Project, User> projectUserJoin = projectRoot.join(Project_.users);
        Predicate notDeletedProjects = criteriaBuilder.notEqual(projectRoot.get(Project_.isDeleted), true);
        Predicate relatedToUserProjects = criteriaBuilder.equal(projectUserJoin.get(User_.username), username);
        query.select(projectRoot)
                .where(criteriaBuilder.and(relatedToUserProjects, notDeletedProjects));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = projectRoot.get(sort.getColumn());
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

    public boolean isUserInProject(String projectKey, String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> query = getCriteriaQuery();
        Root<Project> projectRoot = query.from(Project.class);
        Join<Project, User> projectUserJoin = projectRoot.join(Project_.users);
        Predicate equalUsername = criteriaBuilder.equal(projectUserJoin.get(User_.username), username);
        Predicate equalProjectKey = criteriaBuilder.equal(projectRoot.get(Project_.key), projectKey);
        query.select(projectRoot)
                .where(criteriaBuilder.and(equalUsername, equalProjectKey));
        return !(getEntityByCriteria(query)
                .getResultList()
                .isEmpty());
    }

    public boolean isProjectExists(String projectKey) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> query = getCriteriaQuery();
        Root<Project> projectRoot = query.from(Project.class);
        Predicate equalProjectKey = criteriaBuilder.equal(projectRoot.get(Project_.key), projectKey);
        query.select(projectRoot)
                .where(criteriaBuilder.and(equalProjectKey));
        return !getEntityByCriteria(query).getResultList().isEmpty();
    }

    public List<Project> findAll(int pageNumber, int offset, Sort sort) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> query = getCriteriaQuery();
        Root<Project> projectRoot = query.from(Project.class);
        query.select(projectRoot)
                .where(criteriaBuilder.notEqual(projectRoot.get(Project_.isDeleted), true));
        if (sort != null && !SortOrder.DEFAULT.equals(sort.getOrder())) {
            final Path<Object> sortColumn = projectRoot.get(sort.getColumn());
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
