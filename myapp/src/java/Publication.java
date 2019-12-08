package com.leroydev.blottsbooks;

public class Publication {
    private String title;
    private String author;

    public Publication(String t, String a) {
        this.title = t;
        this.author = a;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
