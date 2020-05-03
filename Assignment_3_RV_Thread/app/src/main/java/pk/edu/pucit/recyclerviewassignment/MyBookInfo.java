package pk.edu.pucit.recyclerviewassignment;

import android.icu.lang.UProperty;

public class MyBookInfo extends Object {
    private String bookTitle;
    private String bookInfo;
    private String bookUrl;
    private String bookLevel;
    private String bookCover;

    public MyBookInfo() {
    }


    public MyBookInfo(String bookTitle, String bookInfo, String bookUrl, String bookLevel, String bookCover) {
        this.bookTitle = bookTitle;
        this.bookInfo = bookInfo;
        this.bookUrl = bookUrl;
        this.bookLevel = bookLevel;
        this.bookCover = bookCover;
    }


    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookInfo(String bookInfo) {
        this.bookInfo = bookInfo;
    }

    public String getBookInfo() {
        return bookInfo;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookLevel(String bookLevel) {
        this.bookLevel = bookLevel;
    }

    public String getBookLevel() {
        return bookLevel;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getBookCover() {
        return bookCover;
    }











}
