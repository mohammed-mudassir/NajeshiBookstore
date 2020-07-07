package com.codewithabdul.nejashibookstore.response;

import com.codewithabdul.nejashibookstore.models.UserModel;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse {

    @SerializedName("user_info")
    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
