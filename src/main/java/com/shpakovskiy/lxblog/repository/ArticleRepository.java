package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.Article;
import com.shpakovskiy.lxblog.entity.Tag;
import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepository implements ArticleRepositoryInterface {

    private TagRepository tagRepository;

    public TagRepository getTagRepository() {
        return tagRepository;
    }

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Article getById(int id) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Article.class, id);
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> getPublicArticles() {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<Article> articles = (List<Article>) session
                    .createNativeQuery("SELECT * FROM articles WHERE status = 'PUBLIC'"/* + Article.Status.PUBLIC*/)
                    .list();
            return articles;
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void addArticle(Article article, User author) {
        article.setAuthorId(author.getId());
        resolveTags(article);
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(article);
            session.getTransaction().commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void updateArticle(Article article) {
        resolveTags(article);
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(article);
            session.getTransaction().commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void removeArticle(Article article) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(article);
            session.getTransaction().commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public boolean isUsersArticle(int articleId, User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return !session.createQuery("FROM Article a WHERE a.authorId = :authorId")
                    .setParameter("authorId", user.getId())
                    .list().isEmpty();
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> getUsersArticles(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<Article> articles = (List<Article>) session
                    .createNativeQuery("SELECT * FROM articles WHERE author_id = " + user.getId())
                    .list();
            return articles;
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void resolveTags(Article article) {
        List<Tag> tags = article.getTags();
        if (!tags.isEmpty()) {
            for (int i = 0; i < tags.size(); i++) {
                if (!tagRepository.isTagExists(tags.get(i))) {
                    System.out.println("Tag not exist: " + tags.get(i));
                    tagRepository.addTag(tags.get(i));
                }
                tags.set(i, tagRepository.getByName(tags.get(i).getName()));
            }
        }
    }
}
