package com.learn.hibernate.Controller;

import com.learn.hibernate.data.ScoreVO;
import com.learn.hibernate.data.StudentCourseVO;
import com.learn.hibernate.data.StudentVO;
import com.learn.hibernate.data.model.Student;
import com.learn.hibernate.data.repository.StudentCRUD;
import com.learn.hibernate.data.repository.StudentCourseCRUD;
import com.learn.hibernate.security.Secured;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Path("/student")
public class StudentApi {
    @Secured
    @RolesAllowed("admin")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentVO studentVO){
        if (studentVO==null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        StudentVO student = StudentCRUD.saveStudent(studentVO);
        return Response.status(201).entity(student).build();

    }
    @Secured
    @RolesAllowed("admin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSturdents(){
        return Response.status(200).entity(StudentCRUD.getAll()).build();
    }

    @Secured
    @RolesAllowed({"admin","master"})
    @Path("/{id}")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") long id){
        return Response.status(200).entity(StudentCRUD.getStudentById(id)).build();
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}")
    @DELETE()
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("id") long id){
        try{
            StudentCRUD.deleteStudent(id);
            return Response.status(Response.Status.ACCEPTED).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}")
    @PUT()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("id") long id, StudentVO studentVO){
        if (studentVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        //diffternt from the other methods
        try {
            studentVO.setId(id);
            return Response.status(Response.Status.ACCEPTED).entity(StudentCRUD.updateStu(studentVO)).build();
        }catch (NoResultException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}/course")
    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCourse(@PathParam("id") long id , StudentCourseVO studentCourseVO){
        if (studentCourseVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        studentCourseVO.setStudentId(id);
        return Response.status(200).entity(StudentCRUD.saveStudentCourse(studentCourseVO)).build();
    }

    @Secured
    @RolesAllowed({"admin","master"})
    @Path("/{id}/course/{cid}")
    @PUT()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourseScore(@PathParam("id") long id , @PathParam("cid") long cid,
                                      ScoreVO scoreVO) {
        if (scoreVO == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        try {
            return Response.status(Response.Status.OK)
                    .entity(StudentCourseCRUD.updateStuCourse(id, cid, scoreVO)).build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Secured
    @RolesAllowed("admin")
    @Path("/{id}/course/{cid}")
    @DELETE()
    public Response deleteCourse(@PathParam("id") long id , @PathParam("cid")long cid){
        StudentCourseCRUD.deleteStuCourse(id, cid);
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @Secured
    @RolesAllowed({"admin","master"})
    @Path("/{id}/course")
    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentCourses(@PathParam("id") long id){
        return Response.status(200).entity(StudentCRUD.getAllCourses(id)).build();
    }

    public static final String UPLOAD_FILE_SERVER = "./temp/";
    @Secured
    @RolesAllowed("admin")
    @Path("/upload-image")
    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadImageFile(@FormDataParam("file")InputStream fileInputStream,
                                    @FormDataParam("file")FormDataContentDisposition fileMetaData,
                                    @FormDataParam("id")String id)
        throws Exception{
        int read = 0;
        byte[] bytes = new byte[2048];
        //student id will be file name
        String fileName = id + ".png";
        String uploadFilePath = null;
        try {
//            fileName = fileMetaData.getFileName();
            uploadFilePath = writeToFileServer(fileInputStream, fileName);
        }
        catch(IOException ioe){
                ioe.printStackTrace();
        }
        return Response.ok("File uploaded successfully at " + uploadFilePath).build();
    }
    private String writeToFileServer(InputStream inputStream , String fileName)
            throws IOException{
        OutputStream outputStream = null;
        String qualifiedUploadFilePath = UPLOAD_FILE_SERVER + fileName;
        try {
            outputStream = new FileOutputStream(new File(qualifiedUploadFilePath));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally{
            //release resource, if any
//            outputStream.flush();
            outputStream.close();
        }
        return qualifiedUploadFilePath;

    }

    @Secured
    @RolesAllowed({"admin","master","student"})
    @Path("/{id}/image")
    @GET()
    @Produces({"image/png","image/jpg","image/gif"})
    public Response downloadImageFile(@PathParam("id") String id){
        File file1 = new File(UPLOAD_FILE_SERVER);
        if(!file1.exists())
            file1.mkdir();
        String fileName = UPLOAD_FILE_SERVER + id + ".png" ;
        File file = new File(fileName);
        Response.ResponseBuilder responseBuilder = Response.ok((Object)file);
        responseBuilder.header("Content-Disposition","attachment; filename=\""+id+".png\"");
        return responseBuilder.build();
    }

}

