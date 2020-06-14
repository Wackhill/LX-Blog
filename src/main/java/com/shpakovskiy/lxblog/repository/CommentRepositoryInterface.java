package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.Comment;
import com.shpakovskiy.lxblog.entity.TagCloudItem;

import java.util.List;

public interface CommentRepositoryInterface {

    void addComment(Comment comment, int articleId, int userId);
    List<Comment> getArticlesComments(int articleId);
    Comment getArticleCommentById(int articleId, int commentId);
    boolean isArticleContainsComment(int articleId, int commentId);
    Comment getById(int id);
    void remove(Comment comment);
}
