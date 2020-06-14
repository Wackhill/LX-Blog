package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.Article;
import com.shpakovskiy.lxblog.entity.Tag;
import com.shpakovskiy.lxblog.entity.TagCloudItem;

import java.util.List;

public interface TagRepositoryInterface {

    List<Tag> getAllTags();
    void addTag(Tag tag);
    Tag getById(int id);
    Tag getByName(String name);
    boolean isTagExists(Tag tag);
    List<Article> getArticlesByTags(String[] tags);
    List<Article> getTagsArticles(Tag tag);
    TagCloudItem getTagCloud(String tagName);
}
