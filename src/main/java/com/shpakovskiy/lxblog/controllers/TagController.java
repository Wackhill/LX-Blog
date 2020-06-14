package com.shpakovskiy.lxblog.controllers;

import com.shpakovskiy.lxblog.entity.Article;
import com.shpakovskiy.lxblog.entity.TagCloudItem;
import com.shpakovskiy.lxblog.repository.ArticleRepository;
import com.shpakovskiy.lxblog.repository.TagRepository;
import com.shpakovskiy.lxblog.security.authorization.RegistrationChecker;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class TagController {

    private final TagRepository tagRepository;
    private final RegistrationChecker registrationChecker;

    public TagController(TagRepository tagRepository,
                         RegistrationChecker registrationChecker) {

        this.tagRepository = tagRepository;
        this.registrationChecker = registrationChecker;
    }

    @RequestMapping("/articles/tags")
    private List<Article> findArticlesByTags(@RequestParam("tags") String[] tags,
                                             HttpServletRequest request, HttpServletResponse response) {
        registrationChecker.checkRegistration(request, response);
        return tagRepository.getArticlesByTags(tags);
    }

    @GetMapping("/tag-cloud/{tagName}")
    public TagCloudItem tagCloud(@PathVariable String tagName,
                                       HttpServletRequest request, HttpServletResponse response) {

        registrationChecker.checkRegistration(request, response);
        return tagRepository.getTagCloud(tagName);
    }
}
