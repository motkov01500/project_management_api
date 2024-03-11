package org.cbg.projectmanagement.project_management;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@DeclareRoles({"administrator", "user"})
public class HelloApplication extends Application {

}