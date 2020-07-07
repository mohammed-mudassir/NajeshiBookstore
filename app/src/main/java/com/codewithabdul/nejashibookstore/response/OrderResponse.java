package com.codewithabdul.nejashibookstore.response;

import com.codewithabdul.nejashibookstore.models.OrderModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderResponse extends BaseResponse {
    @SerializedName("order_details")
    private ArrayList<OrderModel> orderModels;

    public ArrayList<OrderModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(ArrayList<OrderModel> orderModels) {
        this.orderModels = orderModels;
    }
}
