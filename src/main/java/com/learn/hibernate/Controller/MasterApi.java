package com.learn.hibernate.Controller;

import com.learn.hibernate.data.CourseVO;
import com.learn.hibernate.data.MasterCourseVO;
import com.learn.hibernate.data.MasterVO;
import com.learn.hibernate.data.repository.CourseCRUD;
import com.learn.hibernate.data.repository.MasterCRUD;
import com.learn.hibernate.security.Secured;
import com.sun.org.apache.regexp.internal.RE;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/master")
public class MasterApi {
    @Secured
    @RolesAllowed("admin")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMaster(MasterVO masterVO){
        if (masterVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        MasterVO masterVO1 = MasterCRUD.saveMaster(masterVO);
        return Response.status(200).entity(masterVO1).build();
    }

    @Secured
    @RolesAllowed("admin")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMasters(){
        return Response.status(200).entity(MasterCRUD.getAll()).build();
    }

    @Secured
    @RolesAllowed({"admin" ,"master"})
    @Path("/{id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMaster(@PathParam("id") long id){
        try{
            return Response.status(Response.Status.OK).entity(MasterCRUD.getMasterById(id)).build();
        }
        catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}")
    @DELETE()
    public Response deleteMaster(@PathParam("id") long id){
        try {
            MasterCRUD.deleteMaster(id);
            return Response.status(Response.Status.ACCEPTED).build();
        }
        catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}")
    @PUT()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourse(@PathParam("id") long id , MasterVO masterVO){
        try {
            return Response.status(Response.Status.ACCEPTED)
                    .entity(MasterCRUD.updateMaster(masterVO , id)).build();
        }
        catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}/course")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourseToMaster(@PathParam("id") long id , MasterCourseVO masterCourseVO){
        if (masterCourseVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        masterCourseVO.setMasterId(id);
        return Response.status(200).entity(CourseCRUD.addMasterToCourse(masterCourseVO)).build();
    }

    @Secured
    @RolesAllowed({"master","admin"})
    @Path("/{id}/course")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMasterCourses(@PathParam("id") long id){
        return Response.status(200).entity(MasterCRUD.getAllCourses(id)).build();
    }

    @Secured
    @RolesAllowed({"master" , "admin"})
    @Path("/{id}/course/{cid}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMasterCourse(@PathParam("id") long id , @PathParam("cid") long cid){
        try{
            return Response.status(Response.Status.OK)
                    .entity(MasterCRUD.getCourse(id , cid)).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}/course/{cid}")
    @DELETE()
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMasterCourse(@PathParam("id") long id , @PathParam("cid") long cid){
        try{
            MasterCRUD.deleteMasterCourse(id , cid);
            return Response.status(Response.Status.ACCEPTED).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed({"admin","master"})
    @Path("/{id}/student")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMasterStudents(@PathParam("id") long id){
        try {
            return Response.status(Response.Status.OK)
                    .entity(MasterCRUD.getMasterStudents(id)).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed({"admin","master"})
    @Path("/{id}/scores")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMasterScoreList(@PathParam("id") long id){
        try {
            return Response.status(Response.Status.OK)
                    .entity(MasterCRUD.getMasterScoreList(id)).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
