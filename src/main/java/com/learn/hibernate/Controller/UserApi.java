package com.learn.hibernate.Controller;

import com.learn.hibernate.data.LoginInfoVO;
import com.learn.hibernate.data.TokenVO;
import com.learn.hibernate.data.UserVO;
import com.learn.hibernate.data.repository.UserCRUD;
import com.learn.hibernate.security.BaseAuth;
import com.sun.org.apache.regexp.internal.RE;

import javax.annotation.security.DenyAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserApi {
//    @BaseAuth
    @Path("/login")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginInfoVO loginInfoVO){
        try{
            String token = UserCRUD.athenticateUser(loginInfoVO);
//            return Response.ok().entity(token).build();
            return Response.status(Response.Status.OK)
                    .header("token" , token).entity(token).build();
        }
        catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DenyAll
    @Path("/signup")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(UserVO userVO){
        if (userVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.status(Response.Status.CREATED)
                .entity(UserCRUD.addUser(userVO))
                .build();
    }
}
