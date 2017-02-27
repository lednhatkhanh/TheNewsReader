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
    private long publishedAt;

    public Article() {
        title = author = description = url
                = urlToImage = null;
        publishedAt = 0;
    }

    public Article(String title, String author, String description,
                   String url, String urlToImage, long publishedAt) {
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

    public long getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(long publishedAt) {
        this.publishedAt = publishedAt;
    }
}
