package com.learn.hibernate.Controller;

import com.learn.hibernate.data.CourseVO;
import com.learn.hibernate.data.MasterCourseVO;
import com.learn.hibernate.data.repository.CourseCRUD;
import com.learn.hibernate.security.Secured;
import com.sun.org.apache.regexp.internal.RE;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/course")
public class CourseApi {
    @Secured
    @RolesAllowed("admin")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourse(CourseVO courseVO){
        if (courseVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        CourseVO courseVO1 = CourseCRUD.saveCourse(courseVO);
        return Response.status(200).entity(courseVO1).build();
    }


    @Secured
    @RolesAllowed("admin")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourses(){
        return Response.status(200).entity(CourseCRUD.getAll()).build();
    }

    @Secured
    @RolesAllowed({"admin","master","student"})
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourse(@PathParam("id") long id){
        try {
            return Response.status(Response.Status.OK).entity(CourseCRUD.getCourseById(id)).build();
        }
        catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    @Secured
    @RolesAllowed("admin")
    @Path("/{id}")
    @DELETE()
    public Response deleteCourse(@PathParam("id") long id){
        try {
            CourseCRUD.deleteCourse(id);
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
    public Response updateCourse(@PathParam("id") long id , CourseVO courseVO){
        if (courseVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        try {
            return Response.status(Response.Status.ACCEPTED)
                    .entity(CourseCRUD.updateCourse(courseVO , id)).build();
        }
        catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}/master")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourseToMaster(@PathParam("id") long id , MasterCourseVO masterCourseVO){
        if (masterCourseVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        masterCourseVO.setCourseId(id);
        return Response.status(200).entity(CourseCRUD.addMasterToCourse(masterCourseVO)).build();
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}/master")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseMasters(@PathParam("id") long id){
        try{
            return Response.status(Response.Status.OK)
                    .entity(CourseCRUD.getCourseMaster(id)).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed({"master" , "student" , "admin"})
    @Path("/{id}/student")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseStudents(@PathParam("id") long id){
        try {
            return Response.status(Response.Status.OK)
                    .entity(CourseCRUD.getCourseStudents(id)).build();
        }
        catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
