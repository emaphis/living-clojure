package com.leroydev.blottsbooks;

public class Book extends Publication {
    private int numberChapters;

    public Book(String t, String a, int nChaps) {
        super(t, a);
        numberChapters = nChaps;
    }

    public int getNumberChapters() {
        return numberChapters;
    }

    public void publish() {
        // Do something to publish the book.
    }

    public void payRoyalties() {
        // Do something to pay royalites
    }
}
