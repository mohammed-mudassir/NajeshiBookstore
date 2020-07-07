package com.codewithabdul.nejashibookstore.response;

import com.codewithabdul.nejashibookstore.models.AddressModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddressesResponse extends BaseResponse {
    @SerializedName("address_details")
    private ArrayList<AddressModel> addresses;

    public ArrayList<AddressModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<AddressModel> addresses) {
        this.addresses = addresses;
    }
}
