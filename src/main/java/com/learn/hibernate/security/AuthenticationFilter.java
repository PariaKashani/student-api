package com.learn.hibernate.security;


import com.learn.hibernate.data.TokenVO;
import com.learn.hibernate.data.model.Role;
import com.learn.hibernate.data.model.User;
import com.learn.hibernate.data.repository.MasterCRUD;
import com.learn.hibernate.data.repository.UserCRUD;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.jws.soap.SOAPBinding;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();
    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
            .entity("Access blocked for all users !!").build();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (!method.isAnnotationPresent(PermitAll.class)){
            if (method.isAnnotationPresent(DenyAll.class)){
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }
            if (method.isAnnotationPresent(RolesAllowed.class)){
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                String authorizationHeader =
                        requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
                if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")){
                    throw new NotAuthorizedException("Authorization header must be provided");
                }
                final String token = authorizationHeader.substring("Bearer".length()).trim();
                UriInfo uriInfo = requestContext.getUriInfo();
                if (! isTokenValid(token , rolesSet , uriInfo)){
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }
            }
        }
    }

    private boolean isTokenValid(final String token
            , final Set<String> rolesSet,final UriInfo uriInfo){
        boolean isAllowed = false;
        MultivaluedMap<String, String> pathparam = uriInfo.getPathParameters();
        if (JwtTokenProvider.validateToken(token)){
            TokenVO tokenVO = JwtTokenProvider.getTokenDetail(token);
            if (rolesSet.contains(tokenVO.getRole())){
                if (tokenVO.getRole().equals(Role.ADMIN))
                    isAllowed = true;
                else {
                    if (pathparam.containsKey("id")
                            && (Long.valueOf(pathparam.getFirst("id"))==tokenVO.getId()))
                        if (uriInfo.getPath().startsWith(tokenVO.getRole()))
                            isAllowed = true;
                    else if (pathparam.containsKey("id")
                                && tokenVO.getRole().equals(Role.MASTER)
								&& uriInfo.getPath().startsWith(Role.STUDENT)
                                && MasterCRUD.isMasterStudent(tokenVO.getId() ,Long.valueOf(pathparam.getFirst("id")) ))
                        isAllowed=true;
                    //put a new score for student by master
                }
            }
        }
        return isAllowed;
    }
}
