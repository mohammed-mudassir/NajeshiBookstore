package com.codewithabdul.nejashibookstore.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.BooksAdapter;
import com.codewithabdul.nejashibookstore.database.OrderTable;
import com.codewithabdul.nejashibookstore.interfaces.CustomOnClickInterface;
import com.codewithabdul.nejashibookstore.models.BookModel;
import com.codewithabdul.nejashibookstore.response.BooksResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.codewithabdul.nejashibookstore.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity implements CustomOnClickInterface {

    TextView tv_nodata;
    private BooksAdapter booksAdapter;
    private ArrayList<BookModel> books = new ArrayList<>();
    String category_id;
    TextView tv_bag_count;
    private OrderTable orderTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        this.orderTable = new OrderTable(this);

        category_id = getIntent().getStringExtra("category_id");

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        RelativeLayout rl_cart = findViewById(R.id.rl_cart);
        rl_cart.setOnClickListener(v -> {
            startActivity(new Intent(BookListActivity.this, MainActivity.class)
                    .putExtra("goto", "cart"));
        });

        tv_bag_count = findViewById(R.id.tv_bag_count);

        tv_nodata = findViewById(R.id.tv_nodata);

        RecyclerView rv_books = findViewById(R.id.rv_books);
        booksAdapter = new BooksAdapter(this, books, this, false);
        rv_books.setAdapter(booksAdapter);

        getBooksByCategory();
    }

    private void getBooksByCategory() {
        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(this, "Loading, please wait...");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("language", AppPref.getKeyLanguage(this));
        map.put("categoryid", category_id);
        Call<BooksResponse> call = apiInterface.get_books_by_category(map);
        call.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(@NonNull Call<BooksResponse> call, @NonNull Response<BooksResponse> response) {
                CommonUtils.cancelProgressDialog(progressDialog);
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    books.clear();
                    books.addAll(response.body().getBooks());
                    booksAdapter.notifyDataSetChanged();
                    tv_nodata.setVisibility(books.size() > 0 ? View.GONE : View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                CommonUtils.cancelProgressDialog(progressDialog);
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(String id) {
        tv_bag_count.setVisibility(orderTable.getTotalCartItemQty() > 0 ? View.VISIBLE : View.GONE);
        if (orderTable.getTotalCartItemQty() > 0) {
            tv_bag_count.setText(String.valueOf(orderTable.getTotalCartItemQty()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_bag_count.setVisibility(orderTable.getTotalCartItemQty() > 0 ? View.VISIBLE : View.GONE);
        if (orderTable.getTotalCartItemQty() > 0) {
            tv_bag_count.setText(String.valueOf(orderTable.getTotalCartItemQty()));
        }
    }
}