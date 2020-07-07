package com.codewithabdul.nejashibookstore.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.OrderHistoryAdapter;
import com.codewithabdul.nejashibookstore.interfaces.CancelOrderInterface;
import com.codewithabdul.nejashibookstore.models.OrderModel;
import com.codewithabdul.nejashibookstore.response.OrderResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment implements CancelOrderInterface {

    ArrayList<OrderModel> orders = new ArrayList<>();
    OrderHistoryAdapter orderHistoryAdapter;

    public OrderHistoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        RecyclerView rv_orders = view.findViewById(R.id.rv_orders);
        orderHistoryAdapter = new OrderHistoryAdapter(getContext(), orders, this);
        rv_orders.setAdapter(orderHistoryAdapter);

        getOrders();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders();
    }

    private void getOrders() {
        orders.clear();
        orderHistoryAdapter.notifyDataSetChanged();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", AppPref.getUserId(getContext()));
        map.put("language", AppPref.getKeyLanguage(getContext()));
        Call<OrderResponse> call = apiInterface.get_orders(map);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    if (response.body().getOrderModels() != null) {
                        orders.clear();
                        orders.addAll(response.body().getOrderModels());
                        orderHistoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCancel(String order_id) {

    }
}