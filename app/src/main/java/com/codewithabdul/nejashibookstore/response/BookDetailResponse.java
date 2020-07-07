package com.codewithabdul.nejashibookstore.response;

import com.codewithabdul.nejashibookstore.models.BookModel;
import com.google.gson.annotations.SerializedName;

public class BookDetailResponse extends BaseResponse {
    @SerializedName("product_details")
    private BookModel bookModel;

    public BookModel getBookModel() {
        return bookModel;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }
}
