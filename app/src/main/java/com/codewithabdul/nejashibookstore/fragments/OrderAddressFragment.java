package com.codewithabdul.nejashibookstore.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.models.AddressModel;

public class OrderAddressFragment extends Fragment {

    Context context;
    AddressModel addressModel;

    public OrderAddressFragment(Context context, AddressModel addressModel) {
        this.context = context;
        this.addressModel = addressModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_address, container, false);
        TextView tv_name = view.findViewById(R.id.tv_name);
        tv_name.setText(addressModel.getTitle());
        TextView tv_contact = view.findViewById(R.id.tv_contact);
        tv_contact.setText(addressModel.getContact());
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        tv_desc.setText(addressModel.getDescription());
        return view;
    }
}