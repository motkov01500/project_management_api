package org.cbg.projectmanagement.project_management.repository;

import jakarta.inject.Named;
import org.cbg.projectmanagement.project_management.entity.User;

@Named("UserRepository")
public class UserRepository extends  BaseRepository<User> {

    @Override
    public String getEntityName() {
        return User.class.getSimpleName();
    }
}
