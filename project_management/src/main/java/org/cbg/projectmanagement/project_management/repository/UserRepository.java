package org.cbg.projectmanagement.project_management.repository;

import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.request.AuthRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Named("UserRepository")
public class UserRepository extends  BaseRepository<User> {

    @Override
    public String getEntityName() {
        return User.class.getSimpleName();
    }

    public User getUserByUsername(String username) throws NotFoundResourceException {
        String query = "from User WHERE username = :username";
        Map<String, Object> criteria  = new HashMap<>();
        criteria.put("username", username);
        List<User> userList = getEntityByCriteria(query,criteria);
        if(userList.isEmpty()) {
            throw new NotFoundResourceException("test",
                    Response.status(Response.Status.NOT_FOUND).entity("Not Found").build());
        }
        return userList.get(0);
    }
}
