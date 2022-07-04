package com.learn.hibernate.data.repository;

import com.learn.hibernate.data.*;
import com.learn.hibernate.data.model.Course;
import com.learn.hibernate.data.model.Master;
import com.learn.hibernate.data.model.StudentCourse;
import com.learn.hibernate.security.Secured;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseCRUD {
    public static final Logger logger = Logger.getLogger("CourseApi");
    public static CourseVO saveCourse(CourseVO courseVO){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Course course = new Course();

        course.setName(courseVO.getName());
        course.setUnit(courseVO.getUnit());

        session.save(course);
        session.getTransaction().commit();
        session.close();

        courseVO.setCreated(course.getCreated());
        courseVO.setUpdated(course.getUpdated());
        courseVO.setId(course.getId());

        return courseVO;
    }

    public static List<CourseVO> getAll(){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        Query query = session.createQuery("FROM Course");
        List<Course> courses = query.getResultList();

        List<CourseVO> courseVOS = new ArrayList<>();
        courses.forEach(course -> {
            CourseVO courseVO = new CourseVO();

            courseVO.setId(course.getId());
            courseVO.setName(course.getName());
            courseVO.setUpdated(course.getUpdated());
            courseVO.setCreated(course.getCreated());
            courseVO.setUnit(course.getUnit());

            courseVOS.add(courseVO);
        });
        session.close();
        return courseVOS;
    }

    public static CourseVO getCourseById(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Course where id =:id");
        query.setParameter("id" , id);
        Course course = (Course)query.getSingleResult();
        if (course == null)
            throw new NoResultException("this id doesnt exist");
        CourseVO courseVO = new CourseVO();

        try {
            BeanUtils.copyProperties(courseVO , course);

        }catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
            logger.log(Level.WARNING , e.getMessage());
        }
        session.close();
        return courseVO;

    }

    public static void deleteCourse(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("delete from Course where id = :id");
        query.setParameter("id" , id);
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        if (result == 0)
            throw new NoResultException("not found to delete");
    }

    public static CourseVO updateCourse(CourseVO courseVO , long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Course where id = :id");
        query.setParameter("id", id);
        Course course = (Course) query.getSingleResult();
        if (course == null)
            throw new NoResultException();
        course.setName(courseVO.getName());
        course.setUnit(courseVO.getUnit());
        session.update(course);

        courseVO.setCreated(course.getCreated());
        courseVO.setUpdated(course.getCreated());
        courseVO.setId(course.getId());
        session.getTransaction().commit();
        session.close();

        return courseVO;
    }

    public static MasterCourseVO addMasterToCourse(MasterCourseVO masterCourseVO){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Course where id = :id");
        query.setParameter("id" , masterCourseVO.getCourseId());
        Course course = (Course) query.getSingleResult();

        Query query1 = session.createQuery("from Master where id = : mid");
        query1.setParameter("mid" , masterCourseVO.getMasterId());
        Master master = (Master) query1.getSingleResult();

        if (course == null || master == null){
            session.close();
            throw new NoResultException("master or course not found");
        }
        master.getCourseList().add(course);
        course.getMasterList().add(master);

        masterCourseVO.setCourseName(course.getName());
        masterCourseVO.setMasterName(master.getName());

        session.update(master);
        session.update(course);
        session.getTransaction().commit();
        session.close();

        return masterCourseVO;
    }

    public static List<MasterVO> getCourseMaster(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
//        Query query = session.createQuery("select masterList from Course where id = :id");
//        query.setParameter("id" , id);
//        //todo why uchecked?
//        Set<Master> masters = (HashSet<Master>)query.getSingleResult();
        Query query = session.createQuery("from Course where id = : id");
        query.setParameter("id" , id);
        Course course = (Course) query.getSingleResult();
        if (course == null)
            throw new NoResultException("course not found");
        Set<Master> masters = course.getMasterList();
        List<MasterVO> masterVOS = new ArrayList<>();
        masters.forEach(master -> {
            MasterVO masterVO = new MasterVO();
            try {
                BeanUtils.copyProperties(masterVO , master);
            }
            catch (InvocationTargetException|IllegalAccessException e){
                e.printStackTrace();
                logger.log(Level.WARNING , e.getMessage());
            }
            masterVOS.add(masterVO);
        });
        session.close();
        return masterVOS;
    }

    public static List<StudentCourseVO> getCourseStudents(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Course where id = :id");
        query.setParameter("id" , id);
        Course course = (Course) query.getSingleResult();
        if (course == null)
            throw new NoResultException();
        List<StudentCourseVO> studentCourseVOS = new ArrayList<>();
        course.getStudentCourses().forEach(studentCourse -> {
            StudentCourseVO studentCourseVO = new StudentCourseVO();
            studentCourseVO.setStudentId(studentCourse.getStudent().getId());
            studentCourseVO.setCourseId(studentCourse.getCourse().getId());
            studentCourseVO.setCourseName(studentCourse.getCourse().getName());
            studentCourseVO.setStudentName(studentCourse.getStudent().getFirstName());
            studentCourseVO.setScore(studentCourse.getScore());
            studentCourseVO.setCreated(studentCourse.getCreated());
            studentCourseVO.setUpdated(studentCourse.getUpdated());
            studentCourseVOS.add(studentCourseVO);
        });
        return studentCourseVOS;
    }
}
