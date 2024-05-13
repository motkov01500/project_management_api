package org.cbg.projectmanagement.project_management.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import org.cbg.projectmanagement.project_management.entity.Project;
import org.cbg.projectmanagement.project_management.entity.Project_;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.entity.User_;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return Optional.ofNullable(getEntityByCriteriaa(criteriaQuery).getSingleResult());
    }

    public List<Project> findProjectsRelatedToUser(String username) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> query = getCriteriaQuery();
        Root<Project> projectRoot = query.from(Project.class);
        Join<Project, User> projectUserJoin = projectRoot.join(Project_.users);
        Predicate notDeletedProjects = criteriaBuilder.notEqual(projectRoot.get(Project_.isDeleted), true);
        Predicate relatedToUserProjects = criteriaBuilder.equal(projectUserJoin.get(User_.username), username);
        query.select(projectRoot)
                .where(criteriaBuilder.and(relatedToUserProjects, notDeletedProjects));
        return getEntityByCriteriaa(query).getResultList();
    }

    //TODO:findProjectsUserNotAddedIn
//    public List<Project> findProjectsUserNotAddedIn(String username) {
//        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
//        CriteriaQuery<Project> query = getCriteriaQuery();
//        Root<Project> projectRoot = query.from(Project.class);
//        Join<Project, User> projectUserJoin = projectRoot.join(Project_.users);
//        Subquery<String> subquery = query.subquery(String.class);
//        Root<Project> userSubRoot = subquery.from(Project.class);
//        Join<Project, User> projectUserSubJoin = userSubRoot.join(Project_.users);
//        subquery.select(projectUserSubJoin.get(User_.username));
//        query.select(projectRoot)
//                .where(criteriaBuilder.notEqual())
//    }

    public List<Project> findUnassignedProjects() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> query = getCriteriaQuery();
        Root<Project> projectRoot = query.from(Project.class);
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<User> userRoot = subquery.from(User.class);
        Join<User, Project> userProjectJoin = userRoot.join(User_.projects);
        subquery.select(userProjectJoin.get(Project_.id));
        Predicate unassignedProjects = criteriaBuilder.not(projectRoot.get(Project_.id).in(subquery));
        Predicate notDeletedProjects = criteriaBuilder.notEqual(projectRoot.get(Project_.isDeleted), true);
        query.select(projectRoot)
                .where(criteriaBuilder.and(unassignedProjects, notDeletedProjects));
        return getEntityByCriteriaa(query).getResultList();
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
        return !(getEntityByCriteriaa(query)
                .getResultList()
                .isEmpty());
    }

    public List<Project> findAll() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Project> query = getCriteriaQuery();
        Root<Project> projectRoot = query.from(Project.class);
        query.select(projectRoot)
                .where(criteriaBuilder.notEqual(projectRoot.get(Project_.isDeleted), true));
        return getEntityByCriteriaa(query).getResultList();
    }
}
