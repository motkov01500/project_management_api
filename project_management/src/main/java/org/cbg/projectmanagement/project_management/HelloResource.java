package org.cbg.projectmanagement.project_management;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.repository.UserRepository;

import java.util.List;

@Path("/hello-world")
public class HelloResource {

    @Inject
    UserRepository userRepository;

    @GET
    @Produces("application/json")
    public List<User> hello() {
        List<User> users = userRepository.findAll();
        return users;
    }
}