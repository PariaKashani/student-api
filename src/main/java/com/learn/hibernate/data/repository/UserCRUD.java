package com.learn.hibernate.data.repository;

import com.learn.hibernate.data.HibernateUtils;
import com.learn.hibernate.data.LoginInfoVO;
import com.learn.hibernate.data.UserVO;
import com.learn.hibernate.data.model.User;
import com.learn.hibernate.security.JwtTokenProvider;
import com.learn.hibernate.security.PasswordHash;
import io.jsonwebtoken.*;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.security.sasl.AuthenticationException;
import java.util.List;

public class UserCRUD {
    public static UserVO addUser(UserVO userVO){
        //todo not good way to get user and password
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        User user = new User();
        user.setUserName(userVO.getUserName());
        user.setUserRole(userVO.getUserRole());
        String pass= null;
        try {
            pass = PasswordHash.getMd5(userVO.getPassword());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        user.setPassword(pass);

        session.save(user);
        session.getTransaction().commit();

        userVO.setCreated(user.getCreated());
        userVO.setUpdated(user.getUpdated());
        userVO.setId(user.getId());
        session.close();

        return userVO;
    }
    public static String athenticateUser(LoginInfoVO loginInfoVO) throws Exception{
        //todo not good way to get user pass
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User where userName = :userName");
        query.setParameter("userName" , loginInfoVO.getUserName());
        User user = (User) query.getSingleResult();
        String pass = PasswordHash.getMd5(loginInfoVO.getPassword());
        if (user == null)
            throw new NoResultException("no user with this username");
        if (!(user.getPassword().equals(pass)))
            throw new AuthenticationException("wrong password");
        String token = JwtTokenProvider
                .createToken(user.getUserName() , user.getUserRole() , user.getId());
        session.close();

        return token;
    }
    public static User getUserById(long id){
        Session session = HibernateUtils.SESSION_FACTORY.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User where id = :id");
        query.setParameter("id" , id);
        List<User> users = query.getResultList();
        if (users!=null)
            return users.get(0);
        return null;
    }
//    public static boolean isTokenValid(long id){
//        Session session = HibernateUtils.SESSION_FACTORY.openSession();
//        session.beginTransaction();
//        Query query = session.createQuery("from Token where user.id = : id");
//        query.setParameter("id" , id);
//
//        List<Token> tokens = query.getResultList();
//        return tokens!=null && tokens.get(0) != null ;
//    }
}
