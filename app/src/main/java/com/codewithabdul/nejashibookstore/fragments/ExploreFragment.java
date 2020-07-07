package com.codewithabdul.nejashibookstore.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.CategoriesAdapter;
import com.codewithabdul.nejashibookstore.adapters.TrendingAdapter;
import com.codewithabdul.nejashibookstore.models.BookModel;
import com.codewithabdul.nejashibookstore.models.CategoryModel;
import com.codewithabdul.nejashibookstore.response.BooksResponse;
import com.codewithabdul.nejashibookstore.response.CategoryResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.codewithabdul.nejashibookstore.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment {

    private ArrayList<CategoryModel> categories = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;

    private ArrayList<BookModel> trendings = new ArrayList<>();
    private TrendingAdapter trendingAdapter;

    ProgressDialog progressDialog;

    boolean loaded_categories = false;
    boolean loaded_trendings = false;

    public ExploreFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        RecyclerView rv_category = view.findViewById(R.id.rv_category);
        categoriesAdapter = new CategoriesAdapter(getContext(), categories);
        rv_category.setAdapter(categoriesAdapter);

        progressDialog = CommonUtils.showProgressDialog(getContext(), "Loading, please wait...");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (loaded_categories && loaded_trendings) {
                    CommonUtils.cancelProgressDialog(progressDialog);
                    timer.cancel();
                }
            }
        }, 0, 1000);

        getCategories();

        RecyclerView rv_trending = view.findViewById(R.id.rv_trending);
        trendingAdapter = new TrendingAdapter(getContext(), trendings);
        rv_trending.setAdapter(trendingAdapter);

        getTrendingBooks();

        return view;
    }

    private void getTrendingBooks() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("language", AppPref.getKeyLanguage(getContext()));
        Call<BooksResponse> call = apiInterface.get_all_books(map);
        call.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(@NonNull Call<BooksResponse> call, @NonNull Response<BooksResponse> response) {
                loaded_trendings = true;
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    trendings.clear();
                    trendings.addAll(response.body().getBooks());
                    trendingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                loaded_trendings = true;
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

    private void getCategories() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("language", AppPref.getKeyLanguage(getContext()));
        Call<CategoryResponse> call = apiInterface.get_categories(map);
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                loaded_categories = true;
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    categories.clear();
                    categories.addAll(response.body().getCategories());
                    categoriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                loaded_categories = true;
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }
}
