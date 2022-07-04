package com.learn.hibernate.security;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
//not used yet
@BaseAuth
@Provider
@Priority(Priorities.AUTHORIZATION)
public class LoginFilter implements ContainerRequestFilter{
    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();
        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
        //If no authorization information present; block access
        if(authorization == null || authorization.isEmpty())
        {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }
    }
}
