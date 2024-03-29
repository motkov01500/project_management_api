package org.cbg.projectmanagement.project_management.repository;

import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("ProjectRepository")
public class ProjectRepository extends BaseRepository<Project>{

    @Override
    public String getEntityName() {
        return Project.class.getSimpleName();
    }

    public boolean findUserInProject(String projectKey, String username) {
        String query = "FROM Project PR JOIN PR.users US WHERE PR.key = :project_key AND US.username = :username";
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("project_key", projectKey);
        criteria.put("username", username);
        List<Project> projectList = getEntityByCriteria(query, criteria);
        return !projectList.isEmpty();
    }

    public List<Project> findUnassignedProjects() {
        String query = "FROM Project P WHERE P.id NOT IN (FROM User U JOIN U.projects)";
        return getEntityByCriteria(query, new HashMap<>());
    }
}
