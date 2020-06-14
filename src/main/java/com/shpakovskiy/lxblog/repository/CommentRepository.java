package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.Comment;
import com.shpakovskiy.lxblog.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository implements CommentRepositoryInterface {

    @Override
    public void addComment(Comment comment, int articleId, int userId) {
        comment.setPostId(articleId);
        comment.setAuthorId(userId);
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(comment);
            session.getTransaction().commit();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public List<Comment> getArticlesComments(int articleId) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return (List<Comment>) session.createQuery("FROM Comment c WHERE c.postId = :articleId")
                    .setParameter("articleId", articleId)
                    .list();
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Comment getArticleCommentById(int articleId, int commentId) {
        List<Comment> allComments = getArticlesComments(articleId);
        return (commentId > 0 && allComments.size() >= commentId) ?
                allComments.get(commentId - 1) : null;
    }

    @Override
    public boolean isArticleContainsComment(int articleId, int commentId) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return !session.createQuery("FROM Comment c WHERE c.postId = :articleId AND c.id = :commentId")
                    .setParameter("articleId", articleId)
                    .setParameter("commentId", commentId)
                    .list().isEmpty();
        }
        catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public Comment getById(int id) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Comment.class, id);
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void remove(Comment comment) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(comment);
            session.getTransaction().commit();
        }
    }
}