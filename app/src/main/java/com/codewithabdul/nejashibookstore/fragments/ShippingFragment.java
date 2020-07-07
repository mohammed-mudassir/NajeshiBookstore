package com.codewithabdul.nejashibookstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.codewithabdul.nejashibookstore.R;

public class ShippingFragment extends Fragment {

    public ShippingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        TextView tv_addis = view.findViewById(R.id.tv_addis);
        tv_addis.setOnClickListener(v -> {

        });
        TextView tv_out_of_addis = view.findViewById(R.id.tv_out_of_addis);
        tv_out_of_addis.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
        });
        TextView tv_international = view.findViewById(R.id.tv_international);
        tv_international.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}