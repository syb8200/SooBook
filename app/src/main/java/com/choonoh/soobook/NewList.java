package com.choonoh.soobook;

public class NewList {
    String title;
    String author;
    String publisher;
    String customerReviewRank;
    String coverSmallUrl;

    String isbn;
    String description;
    String pubDate;

    /*
    public BestsellerList(String rank, String title, String author, String publisher, String customerReviewRank, String coverSmallUrl){
        this.rank = rank;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.customerReviewRank = customerReviewRank;
        this.coverSmallUrl = coverSmallUrl;
    }*/

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCustomerReviewRank() {
        return customerReviewRank;
    }

    public String getCoverSmallUrl() {
        return coverSmallUrl;
    }


    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setCustomerReviewRank(String customerReviewRank) {
        this.customerReviewRank = customerReviewRank;
    }

    public void setCoverSmallUrl(String coverSmallUrl) {
        this.coverSmallUrl = coverSmallUrl;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
