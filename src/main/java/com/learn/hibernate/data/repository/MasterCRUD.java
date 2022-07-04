package com.learn.hibernate.data.repository;

import com.learn.hibernate.data.*;
import com.learn.hibernate.data.model.Course;
import com.learn.hibernate.data.model.Master;
import com.learn.hibernate.data.model.Student;
import com.learn.hibernate.data.model.StudentCourse;
import com.learn.hibernate.security.PasswordHash;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterCRUD {
    public static final Logger logger = Logger.getLogger("MasterApi");
    public static MasterVO saveMaster(MasterVO masterVO){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Master master = new Master();
        master.setName(masterVO.getName());
        master.setPassword(PasswordHash.getMd5(masterVO.getPassword()));
        master.setUserName(masterVO.getUserName());

        session.save(master);
        session.getTransaction().commit();
        session.close();

        masterVO.setCreated(master.getCreated());
        masterVO.setUpdated(master.getUpdated());
        masterVO.setId(master.getId());

        return masterVO;
    }

    public static MasterVO getMasterById(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Master where id = :id");
        query.setParameter("id" , id);
        Master master = (Master)query.getSingleResult();
        session.close();
        if (master == null)
            throw new NoResultException();
        MasterVO masterVO = new MasterVO();

        try {
            BeanUtils.copyProperties(masterVO , master);

        }catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
            logger.log(Level.WARNING , e.getMessage());

        }
        return masterVO;
    }

    public static void deleteMaster(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("delete from Master where id = :id");
        query.setParameter("id" , id);
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        if (result == 0)
            throw new NoResultException("not found to delete");
    }

    public static MasterVO updateMaster(MasterVO masterVO , long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Master where id = :id");
        query.setParameter("id", id);
        Master master= (Master) query.getSingleResult();

        if (master == null){
            session.close();
            throw new NoResultException();
        }
        master.setName(master.getName());
        session.update(master);
        masterVO.setCreated(master.getCreated());
        masterVO.setUpdated(master.getCreated());
        masterVO.setId(master.getId());
        session.getTransaction().commit();
        session.close();

        return masterVO;
    }

    public static List<MasterVO> getAll(){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Master");
        List<Master> masters = query.getResultList();

        List<MasterVO> masterVOS = new ArrayList<>();

        masters.forEach(master -> {
            MasterVO masterVO = new MasterVO();
            masterVO.setId(master.getId());
            masterVO.setUpdated(master.getUpdated());
            masterVO.setCreated(master.getCreated());
            masterVO.setName(master.getName());

            masterVOS.add(masterVO);
        });
        session.close();
        return masterVOS;
    }

    //use this method for courses
    //use code defined in courseCRUD instead of this
//    public static MasterCourseVO addCourse(MasterCourseVO masterCourseVO){
//        Session session = HibernateUtils.SESSION_FACTORY.openSession();
//        session.beginTransaction();
//
//        Query masterQ = session.createQuery("from Master where id = :id");
//        masterQ.setParameter("id" , masterCourseVO.getMasterId());
//        List<Master> masters = masterQ.getResultList();
//
//        Query crsQ = session.createQuery("from Course where id = :id");
//        crsQ.setParameter("id" , masterCourseVO.getCourseId());
//        List<Course> courses = crsQ.getResultList();
//
//        //todo change this code to return suitable error
//        if (masters==null || courses==null)
//            return null;
//
//        masters.get(0).getCourseList().add(courses.get(0));
//        courses.get(0).getMasterList().add(masters.get(0));
//
//        session.save(courses.get(0));
//        session.save(masters.get(0));
//        session.getTransaction().commit();
//
//        session.close();
//
//        masterCourseVO.setCourseName(courses.get(0).getName());
//        masterCourseVO.setMasterName(masters.get(0).getName());
//
//        return masterCourseVO;
//    }

    public static List<MasterCourseVO> getAllCourses(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Master where id = :id");
        query.setParameter("id" , id);
        List<Master> masters =  query.getResultList();

        List<MasterCourseVO> masterCourseVOS = new ArrayList<>();

        if (masters == null)
            return null;

        masters.get(0).getCourseList().forEach(course -> {
            MasterCourseVO masterCourseVO = new MasterCourseVO();

            masterCourseVO.setMasterName(masters.get(0).getName());
            masterCourseVO.setMasterId(masters.get(0).getId());
            masterCourseVO.setCourseName(course.getName());
            masterCourseVO.setCourseId(course.getId());

            masterCourseVOS.add(masterCourseVO);
        });
        session.close();
        return masterCourseVOS;
    }

    public static CourseVO getCourse(long id , long cid){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Master where id = :id");
        query.setParameter("id" , id);
        Master master = (Master) query.getSingleResult();
        if (master == null)
            throw new NoResultException("master not found");
        Optional<Course> optional = master.getCourseList().stream().filter(c->(c.getId() == cid)).findFirst();
        session.close();
        if (optional.isPresent()){
            Course course = optional.get();
            CourseVO courseVO = new CourseVO();
            try {
                BeanUtils.copyProperties(courseVO , course);
            }catch (IllegalAccessException|InvocationTargetException e) {
                e.printStackTrace();
                logger.log(Level.WARNING , e.getMessage());
            }
            return courseVO;
        }
        else{
            throw new NoResultException("course not found");
        }

    }

    public static void deleteMasterCourse(long id , long cid){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from Master where id = :id");
        query.setParameter("id" , id);
        Master master = (Master) query.getSingleResult();
        if (master == null)
            throw new NoResultException("master not found");
        Optional<Course> optional = master.getCourseList().stream().filter(c->(c.getId() == cid)).findFirst();
        if (optional.isPresent()){
            Course course = optional.get();
            master.getCourseList().remove(course);
            session.update(master);
            session.getTransaction().commit();
            session.close();
        }
        else{
            session.close();
            throw new NoResultException("course not found");
        }
    }

    public static List<StudentVO> getMasterStudents(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Master where id = :id");
        query.setParameter("id" , id);
        Master master = (Master) query.getSingleResult();
        if (master == null) {
            session.close();
            throw new NoResultException("master not found");
        }
        List<StudentVO> studentVOS = new ArrayList<>();
        master.getCourseList().forEach(course -> {
            course.getStudentCourses().forEach(studentCourse -> {
                StudentVO studentVO = new StudentVO();
                try{
                    BeanUtils.copyProperties(studentVO , studentCourse.getStudent());
                }catch (IllegalAccessException|InvocationTargetException e){
                    e.printStackTrace();
                    logger.log(Level.WARNING , e.getMessage());
                }
                studentVOS.add(studentVO);
            });
        });
        session.close();
        return studentVOS;
    }
    public static boolean isMasterStudent(long mid , long sid){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Result result = new Result();
        result.setResult(false);
        Query query = session.createQuery("from Master where id = :mid");
        query.setParameter("mid" , mid);
        Master master = (Master) query.getSingleResult();
        master.getCourseList().forEach(course -> {
            course.getStudentCourses().forEach(studentCourse -> {
                if (studentCourse.getStudent().getId() == sid){
                   session.close();
                   result.setResult(true);
                   return;
                }
            });
        });
        return result.isResult();
    }
    public static List<StudentCourseVO> getMasterScoreList(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Master where id = :id");
        query.setParameter("id" , id);
        Master master = (Master) query.getSingleResult();
        if (master == null) {
            session.close();
            throw new NoResultException("master not found");
        }
        List<StudentCourseVO> studentCourseVOS = new ArrayList<>();
        master.getCourseList().forEach(course -> {
            course.getStudentCourses().forEach(studentCourse -> {
                //
                StudentCourseVO studentCourseVO = new StudentCourseVO();
                studentCourseVO.setUpdated(studentCourse.getUpdated());
                studentCourseVO.setCreated(studentCourse.getCreated());
                studentCourseVO.setScore(studentCourse.getScore());
                studentCourseVO.setStudentName(studentCourse.getStudent().getFirstName());
                studentCourseVO.setCourseName(studentCourse.getCourse().getName());
                studentCourseVO.setCourseId(studentCourse.getCourse().getId());
                studentCourseVO.setStudentId(studentCourse.getStudent().getId());
                studentCourseVOS.add(studentCourseVO);
            });
        });
        return studentCourseVOS;
    }

}
