package com.learn.hibernate.data.repository;

import com.learn.hibernate.data.HibernateUtils;
import com.learn.hibernate.data.StudentCourseVO;
import com.learn.hibernate.data.StudentVO;
import com.learn.hibernate.data.model.*;
import com.learn.hibernate.security.GenerateKey;
import com.learn.hibernate.security.PasswordHash;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.jws.soap.SOAPBinding;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentCRUD {
    private static final Logger logger = Logger.getLogger("StudentAPI");

    public static StudentVO saveStudent(StudentVO studentVO){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Student student = new Student();

        student.setCode(studentVO.getCode());
        student.setFirstName(studentVO.getFirstName());
        student.setLastName(studentVO.getLastName());
        student.setPassword(PasswordHash.getMd5(studentVO.getPassword()));
        student.setUserName(studentVO.getUserName());


        studentVO.getPhones().forEach(phone -> {
            Phone newPhone = new Phone();
            newPhone.setNumber(phone);

            session.save(newPhone);
            student.getPhoneList().add(newPhone);
        });

        session.save(student);
        session.getTransaction().commit();
        session.close();

        studentVO.setId(student.getId());
        studentVO.setCreated(student.getCreated());
        studentVO.setUpdated(student.getCreated());

//        StudentVO studentVO1 = new StudentVO();
//
//        try {
//            BeanUtils.copyProperties(studentVO1 , student);
//
//        }catch (IllegalAccessException|InvocationTargetException e) {
//            e.printStackTrace();
//            logger.log(Level.WARNING , e.getMessage());
//
//        }
        //todo try beanutils for this method
        return studentVO;

    }

    public static StudentVO getStudentById(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Student where id = :id");
        query.setParameter("id" ,id);
        List<Student> students = query.getResultList();
        if (students == null) return null;
        Student student = students.get(0);
        StudentVO studentVO = new StudentVO();

        studentVO.setCode(student.getCode());
        studentVO.setId(student.getId());
        studentVO.setFirstName(student.getFirstName());
        studentVO.setLastName(student.getLastName());
        studentVO.setCreated(student.getCreated());
        studentVO.setUpdated(student.getUpdated());
        student.getPhoneList().forEach(phone -> {
            studentVO.getPhones().add(phone.getNumber());
        });
        session.close();
        return studentVO;
    }

    public static StudentVO updateStu(StudentVO studentVO){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Student where id = :id");
//        Query query = session.createQuery("UPDATE Student set firstName = :fname where id = :id");
        query.setParameter("id" , studentVO.getId());
        Student student = (Student)query.getSingleResult();
        //todo change
        if (student == null )
            throw new NoResultException("student not found");
        student.setLastName(studentVO.getLastName());
        student.setFirstName(studentVO.getFirstName());
        session.update(student);

        studentVO.setCode(student.getCode());
        studentVO.setUpdated(student.getUpdated());
        studentVO.setCreated(student.getCreated());

        session.getTransaction().commit();
        session.close();
        return studentVO;
    }
    public static List<StudentVO> getAll(){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Student");
        List<Student> students = query.getResultList();

        List<StudentVO> studentVOS = new ArrayList<>();

        students.forEach(std ->{
            StudentVO studentVo = new StudentVO();
            studentVo.setId(std.getId());
            studentVo.setUpdated(std.getUpdated());
            studentVo.setCreated(std.getCreated());
            studentVo.setCode(std.getCode());
            studentVo.setFirstName(std.getFirstName());
            studentVo.setLastName(std.getLastName());
            //set propertice
            Hibernate.initialize(std.getPhoneList());
            std.getPhoneList().forEach(phone -> {
                studentVo.getPhones().add(phone.getNumber());
            });
            studentVOS.add(studentVo);
        });
        session.close();
        return studentVOS;
    }

    //problem with query for delete
    public static void deleteStudent(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
//        Query query1 = session.createQuery("delete from Phone where ")
        Query query = session.createQuery("from Student where id = :id");
        query.setParameter("id" , id);
        Student  result = (Student)query.getSingleResult();
        if (result == null )
            throw new NoResultException("student not found");
        result.getPhoneList().forEach(phone ->{
            result.removePhone(phone);
            session.remove(phone);
        });
        session.update(result);
        session.getTransaction().commit();
        session.beginTransaction();
        session.remove(result);
        session.getTransaction().commit();
        session.close();
    }

    public static StudentCourseVO saveStudentCourse(StudentCourseVO studentCourseVO){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query stuQuery = session.createQuery("from Student where id = :stuId ");
        stuQuery.setParameter("stuId" , studentCourseVO.getStudentId());
        List<Student> students = stuQuery.getResultList();

        Query crsQuery = session.createQuery("from Course where id = :crsId");
        crsQuery.setParameter("crsId" , studentCourseVO.getCourseId());
        List<Course> courses = crsQuery.getResultList();

        //todo this part should change
        if (students ==null || courses == null)
            return null;
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(courses.get(0));
        studentCourse.setStudent(students.get(0));
        studentCourse.setScore(studentCourseVO.getScore());

        students.get(0).getStudentCourses().add(studentCourse);
        courses.get(0).getStudentCourses().add(studentCourse);

        session.save(students.get(0));
        session.save(courses.get(0));
        session.save(studentCourse);
        session.getTransaction().commit();
        session.close();

        studentCourseVO.setCourseName(studentCourse.getCourse().getName());
        studentCourseVO.setStudentName(studentCourse.getStudent().getFirstName());
        studentCourseVO.setCreated(studentCourse.getCreated());
        studentCourseVO.setUpdated(studentCourse.getUpdated());
        studentCourseVO.setId(studentCourse.getId());

        return studentCourseVO;
    }

    public static List<StudentCourseVO> getAllCourses(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from StudentCourse where student.id = : stuId");
        query.setParameter("stuId" , id);
        List<StudentCourse> studentCourses = query.getResultList();
        if (studentCourses == null) return null;

        List<StudentCourseVO> studentCourseVOS = new ArrayList<>();

        studentCourses.forEach(studentCourse -> {
            StudentCourseVO studentCourseVO = new StudentCourseVO();

            studentCourseVO.setId(studentCourse.getId());
            studentCourseVO.setUpdated(studentCourse.getUpdated());
            studentCourseVO.setCreated(studentCourse.getCreated());
            studentCourseVO.setStudentName(studentCourse.getStudent().getFirstName());
            studentCourseVO.setCourseName(studentCourse.getCourse().getName());
            studentCourseVO.setCourseId(studentCourse.getCourse().getId());
            studentCourseVO.setStudentId(studentCourse.getStudent().getId());

            studentCourseVOS.add(studentCourseVO);
        });
        session.close();

        return studentCourseVOS;
    }


}
