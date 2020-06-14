package com.shpakovskiy.lxblog.util;

import com.shpakovskiy.lxblog.entity.Article;
import com.shpakovskiy.lxblog.entity.Comment;
import com.shpakovskiy.lxblog.entity.Tag;
import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.security.registration.RegistrationQueueItem;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Tag.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(RegistrationQueueItem.class);
                configuration.addAnnotatedClass(Article.class);
                configuration.addAnnotatedClass(Comment.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}