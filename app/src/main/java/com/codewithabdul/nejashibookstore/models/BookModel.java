package com.codewithabdul.nejashibookstore.models;

import java.io.Serializable;

public class BookModel implements Serializable {
    private String catname;
    private String product_id;
    private String title;
    private String author;
    private String publisher;
    private String image;
    private String count;
    private String price;
    private String total_price;
    private String description;

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCount() {
        return count == null ? "0" : count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price == null ? "100" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
