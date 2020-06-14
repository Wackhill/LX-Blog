package com.shpakovskiy.lxblog.security.registration;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationQueueService implements RegistrationQueueInterface {

    private final int TOKEN_LENGTH = 32;

    private final SequenceGenerator sequenceGenerator;

    public RegistrationQueueService(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void addItem(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            RegistrationQueueItem registrationQueueItem = new RegistrationQueueItem.Builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .token(generateToken(TOKEN_LENGTH))
                    .build();

            session.save(registrationQueueItem);
            session.beginTransaction().commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void removeFromQueue(RegistrationQueueItem user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public String generateToken(int tokenLength) {
        return sequenceGenerator.generateToken(tokenLength);
    }

    @Override
    public boolean isTokenAvailable(String token) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public User getUserByToken(String token) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<RegistrationQueueItem> users = session.createQuery("FROM RegistrationQueueItem qi WHERE qi.token = :token")
                    .setParameter("token", token)
                    .list();

            if (!users.isEmpty()) {
                RegistrationQueueItem queueItem = users.get(0);
                User newUser = new User();
                newUser.setFirstName(queueItem.getFirstName());
                newUser.setLastName(queueItem.getLastName());
                newUser.setEmail(queueItem.getEmail());
                newUser.setPassword(queueItem.getPassword());

                removeFromQueue(queueItem);

                return newUser;
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getUsersToken(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<RegistrationQueueItem> users = session.createQuery("FROM RegistrationQueueItem qi WHERE qi.email = :email")
                    .setParameter("email", user.getEmail())
                    .list();

            if (!users.isEmpty()) {
                return users.get(0).getToken();
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
