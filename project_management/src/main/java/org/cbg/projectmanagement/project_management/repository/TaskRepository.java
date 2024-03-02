package org.cbg.projectmanagement.project_management.repository;

import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.Task;

@Named("TaskRepository")
public class TaskRepository extends BaseRepository<Task>{

    @Override
    public String getEntityName() {
        return Task.class.getSimpleName();
    }
}
