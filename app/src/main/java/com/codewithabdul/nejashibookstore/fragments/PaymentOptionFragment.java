package com.codewithabdul.nejashibookstore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.activities.MainActivity;
import com.codewithabdul.nejashibookstore.database.OrderTable;
import com.codewithabdul.nejashibookstore.models.BookModel;
import com.codewithabdul.nejashibookstore.response.BaseResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.codewithabdul.nejashibookstore.utils.ConstantUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentOptionFragment extends Fragment {

    OrderTable orderTable;
    String total = "0";

    public PaymentOptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_option, container, false);
        TextView tv_cod = view.findViewById(R.id.tv_cod);
        tv_cod.setOnClickListener(v -> {

        });
        TextView tv_bank_transfer = view.findViewById(R.id.tv_bank_transfer);
        tv_bank_transfer.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
        });
        TextView tv_card = view.findViewById(R.id.tv_card);
        tv_card.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
        });

        orderTable = new OrderTable(getContext());

        TextView tv_order_total = view.findViewById(R.id.tv_order_total);
        tv_order_total.setText(orderTable.getTotalCartItemPrice() + " " + ConstantUtils.CURRENCY_SYMBOL);
        TextView tv_shipping_charges = view.findViewById(R.id.tv_shipping_charges);
        tv_shipping_charges.setText("100 ETB ");
        TextView tv_total = view.findViewById(R.id.tv_total);
        total = String.valueOf(orderTable.getTotalCartItemPrice() + 100);
        tv_total.setText(total + " " + ConstantUtils.CURRENCY_SYMBOL);

        Button btn_action = view.findViewById(R.id.btn_action);
        btn_action.setOnClickListener(v -> {
            placeOrder();
        });

        return view;
    }

    private void placeOrder() {
        if (AppPref.getKeySelectedAddress(getContext()).equals("0")) {
            Toast.makeText(getContext(), getResources().getString(R.string.pl_sel_address), Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new Gson();
        ArrayList<Map<String, String>> products = new ArrayList<>();
        for (BookModel productModel : orderTable.getCartItems()) {
            Map<String, String> product = new HashMap<>();
            product.put("product_id", productModel.getProduct_id());
            product.put("quantity", productModel.getCount());
            products.add(product);
        }
        String orderJson = gson.toJson(products);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", AppPref.getUserId(getContext()));
        map.put("products", orderJson);
        map.put("transaction_id", String.valueOf(new Date().getTime()));
        map.put("transaction_amount", total);
        map.put("payment_type", "COD");
        map.put("payment_status", "success");
        map.put("add_id", AppPref.getKeySelectedAddress(getContext()));
        Call<BaseResponse> call = apiInterface.place_order(map);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    Toast.makeText(getContext(), "Order placed successfully.", Toast.LENGTH_SHORT).show();
                    AppPref.setKeySelectedAddress(getContext(), "0");
                    orderTable.clearCart();
                    startActivity(new Intent(getContext(), MainActivity.class)
                            .putExtra("goto", "orders"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }
}