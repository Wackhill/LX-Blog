package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.security.EmailAndPassword;
import com.shpakovskiy.lxblog.security.PasswordEncoder;
import com.shpakovskiy.lxblog.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository implements UserRepositoryInterface {

    private final PasswordEncoder passwordEncoder;

    public UserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From User").list();
    }

    @Override
    @SuppressWarnings("unchecked")  //Used to let compiler ignore probably missing query result casting
    public User getUserById(int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<User> users = (List<User>) session.createQuery("FROM User u WHERE u.id = :id")
                    .setParameter("id", id)
                    .list();

            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public User getUserByEmail(String userEmail) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<User> users = (List<User>) session.createQuery("FROM User u WHERE u.email = :email")
                    .setParameter("email", userEmail)
                    .list();

            if (!users.isEmpty()) {
                return users.get(0);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void updateUser(User user) {
        //TODO
    }

    @Override
    public boolean isUserExist(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return !session.createQuery("FROM User u WHERE u.email = :email")
                    .setParameter("email", user.getEmail())
                    .list().isEmpty();
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean isUserAuthorized(EmailAndPassword emailAndPassword) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return !session.createQuery("FROM User u WHERE u.email = :email AND u.password = :password")
                    .setParameter("email", emailAndPassword.getEmail())
                    .setParameter("password", passwordEncoder.encode(emailAndPassword.getPassword()))
                    .list().isEmpty();
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public void addUser(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
