package com.codewithabdul.nejashibookstore.response;

import com.codewithabdul.nejashibookstore.models.CategoryModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse extends BaseResponse {

    @SerializedName("categorylist")
    private ArrayList<CategoryModel> categories;

    public ArrayList<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryModel> categories) {
        this.categories = categories;
    }
}
