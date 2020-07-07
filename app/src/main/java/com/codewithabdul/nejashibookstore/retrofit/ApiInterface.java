package com.codewithabdul.nejashibookstore.retrofit;


import com.codewithabdul.nejashibookstore.response.AddressesResponse;
import com.codewithabdul.nejashibookstore.response.BaseResponse;
import com.codewithabdul.nejashibookstore.response.BookDetailResponse;
import com.codewithabdul.nejashibookstore.response.BooksResponse;
import com.codewithabdul.nejashibookstore.response.CategoryResponse;
import com.codewithabdul.nejashibookstore.response.LoginResponse;
import com.codewithabdul.nejashibookstore.response.OrderResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("userLogin")
    Call<LoginResponse> login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("userRegistration")
    Call<BaseResponse> register(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("getAllCategory")
    Call<CategoryResponse> get_categories(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("getAllProduct")
    Call<BooksResponse> get_all_books(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("getCategoryProducts")
    Call<BooksResponse> get_books_by_category(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("getProductDetails")
    Call<BookDetailResponse> get_product_details(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("addressDetails")
    Call<AddressesResponse> get_addresses(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("addAddress")
    Call<BaseResponse> addAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("updateAddress")
    Call<BaseResponse> updateAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("orderDetails")
    Call<OrderResponse> get_orders(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("productOrder")
    Call<BaseResponse> place_order(@FieldMap Map<String, String> map);

}
