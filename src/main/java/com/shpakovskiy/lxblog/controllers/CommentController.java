package com.shpakovskiy.lxblog.controllers;

import com.shpakovskiy.lxblog.entity.Comment;
import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.repository.CommentRepository;
import com.shpakovskiy.lxblog.security.authorization.RegistrationChecker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    private final RegistrationChecker registrationChecker;
    private final CommentRepository commentRepository;

    public CommentController(RegistrationChecker registrationChecker,
                             CommentRepository commentRepository) {

        this.registrationChecker = registrationChecker;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/articles/{articleId}/comments")
    public HttpStatus addComment(@PathVariable int articleId, @RequestBody Comment comment,
                                 HttpServletRequest request, HttpServletResponse response) {

        User user = registrationChecker.checkRegistration(request, response);
        commentRepository.addComment(comment, articleId, user.getId());
        return HttpStatus.OK;
    }

    @GetMapping("/articles/{articleId}/comments")
    public List<Comment> getArticlesComments(@PathVariable int articleId,
                                             HttpServletRequest request, HttpServletResponse response) {

        registrationChecker.checkRegistration(request, response);
        return commentRepository.getArticlesComments(articleId);
    }

    @GetMapping("/articles/{articleId}/comments/{commentId}")
    public Comment getArticleCommentById(@PathVariable int articleId, @PathVariable int commentId,
                                             HttpServletRequest request, HttpServletResponse response) {

        registrationChecker.checkRegistration(request, response);

        Optional<Comment> article
                = Optional.ofNullable(commentRepository.getArticleCommentById(articleId, commentId));

        if (article.isPresent()) {
            return article.get();
        }
        else {
            commentNotFound(response);
            return null;
        }
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public HttpStatus deleteCommentById(@PathVariable int articleId, @PathVariable int commentId,
                                     HttpServletRequest request, HttpServletResponse response) {

        registrationChecker.checkRegistration(request, response);

        if (commentRepository.isArticleContainsComment(articleId, commentId)) {
            commentRepository.remove(commentRepository.getById(commentId));
            return HttpStatus.OK;
        }
        else {
            commentNotFound(response);
            return null;
        }
    }

    private void commentNotFound(HttpServletResponse response) {
        try {
            response.sendRedirect("/comment-not-found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/comment-not-found")
    public HttpStatus commentNotFound() {
        return HttpStatus.NOT_FOUND;
    }
}
