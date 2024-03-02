package org.cbg.projectmanagement.project_management.repository;

import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.Project;

@Named("ProjectRepository")
public class ProjectRepository extends BaseRepository<Project>{

    @Override
    public String getEntityName() {
        return Project.class.getSimpleName();
    }
}
