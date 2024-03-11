package org.cbg.projectmanagement.project_management.security;

import java.util.concurrent.TimeUnit;

public class ConstantProperties {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String SECRET_KEY = "my-secret";

    public static final long TOKEN_VALIDITY = TimeUnit.MINUTES.toMillis(30);
}
