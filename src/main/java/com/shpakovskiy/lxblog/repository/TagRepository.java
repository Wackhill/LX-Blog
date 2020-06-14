package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.Article;
import com.shpakovskiy.lxblog.entity.Tag;
import com.shpakovskiy.lxblog.entity.TagCloudItem;
import com.shpakovskiy.lxblog.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TagRepository implements TagRepositoryInterface {

    private ArticleRepository articleRepository;

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return (List<Tag>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Tag").list();
    }

    @Override
    public void addTag(Tag tag) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(tag);
            session.getTransaction().commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public Tag getById(int id) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Tag.class, id);
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tag getByName(String name) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            ArrayList<Tag> tagsList = (ArrayList<Tag>) session.createNativeQuery("SELECT * FROM tags WHERE name = '" + name + "'")
                    .setResultTransformer(Transformers.aliasToBean(Tag.class)).list();

            if (!tagsList.isEmpty()) {
                return tagsList.get(0);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean isTagExists(Tag tag) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return !session.createQuery("FROM Tag t WHERE UPPER(t.name) = :name")
                    .setParameter("name", tag.getName().toUpperCase())
                    .list().isEmpty();
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<Article> getArticlesByTags(String[] tagNames) {
        Set<Article> articlesSet = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = null;
            List<Article> articles = null;
            if (((tag = getByName(tagName)) != null) && ((articles = getTagsArticles(tag)) != null)) {
                //articlesSet.addAll(articles);
                for (int i = 0; i < articles.size(); i++) {
                    articlesSet.add(articles.get(i));
                }
            }
        }
        return new ArrayList<>(articlesSet);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Article> getTagsArticles(Tag tag) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            List<Integer> articleIndexes = (List<Integer>) session
                    .createNativeQuery("SELECT article_id FROM article_tag WHERE tag_id = " + tag.getId()).list();

            List<Article> articles = new ArrayList<>();
            for (Integer articleIndex : articleIndexes) {
                articles.add(articleRepository.getById(articleIndex));
            }
            return articles;
        }
        catch (Exception e) { e.printStackTrace(); return null; }
    }

    @Override
    public TagCloudItem getTagCloud(String tagName) {
        return new TagCloudItem(tagName, getTagsArticles(getByName(tagName)).size());
    }
}