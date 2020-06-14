package com.shpakovskiy.lxblog.controllers;

import com.shpakovskiy.lxblog.entity.Article;
import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.repository.ArticleRepository;
import com.shpakovskiy.lxblog.security.authorization.RegistrationChecker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
public class ArticleController {

    private final RegistrationChecker registrationChecker;
    private final ArticleRepository articleRepository;

    public ArticleController(RegistrationChecker registrationChecker,
                             ArticleRepository articleRepository) {

        this.registrationChecker = registrationChecker;
        this.articleRepository = articleRepository;
    }

    @PutMapping("/articles/{id}")
    private HttpStatus updateArticle(@PathVariable int id, @RequestBody Article article,
                                     HttpServletResponse response, HttpServletRequest request) {

        User author = registrationChecker.checkRegistration(request, response);
        if (articleRepository.isUsersArticle(id, author)) {
            article.setId(id);
            article.setCreatedAt(articleRepository.getById(id).getCreatedAt());
            article.setUpdatedAt(new Date());
            article.setAuthorId(author.getId());
            articleRepository.updateArticle(article);
            return HttpStatus.OK;
        }
        else {
            return HttpStatus.FORBIDDEN;
        }
    }

    @PostMapping("/articles")
    private HttpStatus addArticle(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(article);
        User author = registrationChecker.checkRegistration(request, response);
        System.out.println(author);
        articleRepository.addArticle(article, author);
        return HttpStatus.OK;
    }

    @GetMapping("/articles")
    private List<Article> getPublicArticles() {
        return articleRepository.getPublicArticles();
    }

    @GetMapping("/my")
    private List<Article> getUsersPosts(HttpServletRequest request, HttpServletResponse response) {
        User author = registrationChecker.checkRegistration(request, response);
        return articleRepository.getUsersArticles(author);
    }

    @DeleteMapping("/articles/{id}")
    private void removeArticle(@PathVariable int id) {
        articleRepository.removeArticle(articleRepository.getById(id));
    }

    @RequestMapping("/access-denied")
    public HttpStatus accessDenied() {
        return HttpStatus.UNAUTHORIZED;
    }
}
