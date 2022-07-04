package com.learn.hibernate.data.repository;

import com.learn.hibernate.data.HibernateUtils;
import com.learn.hibernate.data.ScoreVO;
import com.learn.hibernate.data.StudentCourseVO;
import com.learn.hibernate.data.model.StudentCourse;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.logging.Logger;

public class StudentCourseCRUD {
    public static final Logger logger = Logger.getLogger("StudentCourse");
    public static void deleteStuCourse(long sid , long cid){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session
                .createQuery("DELETE from StudentCourse where student.id = :sid and course.id = :cid");
        query.setParameter("sid" , sid);
        query.setParameter("cid" , cid);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    public static StudentCourseVO updateStuCourse(long sid , long cid, ScoreVO scoreVO){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from StudentCourse where student.id = :sid and course.id = :cid");
        query.setParameter("cid" , cid);
        query.setParameter("sid" , sid);
        StudentCourse studentCourse = (StudentCourse) query.getSingleResult();
        if (studentCourse == null)
            throw new NoResultException("stuCrs not found");
        studentCourse.setScore(scoreVO.getScore());
        session.update(studentCourse);

        StudentCourseVO studentCourseVO = new StudentCourseVO();
        studentCourseVO.setCreated(studentCourse.getCreated());
        studentCourseVO.setUpdated(studentCourse.getUpdated());
        studentCourseVO.setStudentId(sid);
        studentCourseVO.setCourseId(cid);
        studentCourseVO.setCourseName(studentCourse.getCourse().getName());
        studentCourseVO.setStudentName(studentCourse.getStudent().getFirstName());
        studentCourseVO.setScore(scoreVO.getScore());

        session.getTransaction().commit();
        session.close();
        return studentCourseVO;
    }
}
