package com.lednhatkhanh.thenewsreader.models;

/**
 * Created by lednh on 2/27/2017.
 */

public class Article {
    private String title;
    private String author;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;

    public Article() {
        title = author = description = url
                = urlToImage = publishedAt = null;
    }

    public Article(String title, String author, String description,
                   String url, String urlToImage, String publishedAt) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
