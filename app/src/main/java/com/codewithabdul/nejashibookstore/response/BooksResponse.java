package com.codewithabdul.nejashibookstore.response;

import com.codewithabdul.nejashibookstore.models.BookModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BooksResponse extends BaseResponse {
    @SerializedName("productlist")
    private ArrayList<BookModel> books;

    public ArrayList<BookModel> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<BookModel> books) {
        this.books = books;
    }
}
