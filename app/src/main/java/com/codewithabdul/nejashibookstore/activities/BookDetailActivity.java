package com.codewithabdul.nejashibookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.database.OrderTable;
import com.codewithabdul.nejashibookstore.models.BookModel;
import com.codewithabdul.nejashibookstore.response.BookDetailResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.codewithabdul.nejashibookstore.utils.ConstantUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codewithabdul.nejashibookstore.utils.ConstantUtils.BASE_BOOK_IMAGES;

public class BookDetailActivity extends AppCompatActivity {

    private BookModel bookModel;
    TextView tv_qty;

    OrderTable orderTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        orderTable = new OrderTable(this);

        String id = getIntent().getStringExtra("id");

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            onBackPressed();
        });

        TextView tv_goto_cart = findViewById(R.id.tv_goto_cart);
        tv_goto_cart.setOnClickListener(v -> {
            startActivity(new Intent(BookDetailActivity.this, MainActivity.class)
                    .putExtra("goto", "cart"));
        });

        tv_qty = findViewById(R.id.tv_qty);
        ImageView iv_minus = findViewById(R.id.iv_minus);
        iv_minus.setOnClickListener(v -> {
            String qty = tv_qty.getText().toString();
            if (qty.equals("0")) {
                return;
            }
            tv_qty.setText(String.valueOf(Integer.parseInt(qty) - 1));
            orderTable.addToCart(bookModel, orderTable.getCartItemQty(bookModel.getProduct_id()) - 1);
        });
        ImageView iv_plus = findViewById(R.id.iv_plus);
        iv_plus.setOnClickListener(v -> {
            String qty = tv_qty.getText().toString();
            tv_qty.setText(String.valueOf(Integer.parseInt(qty) + 1));
            orderTable.addToCart(bookModel, orderTable.getCartItemQty(bookModel.getProduct_id()) + 1);
        });

        getBookDetail(id);
    }

    private void getBookDetail(String id) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("product_id", id);
        map.put("language", AppPref.getKeyLanguage(this));
        Call<BookDetailResponse> call = apiInterface.get_product_details(map);
        call.enqueue(new Callback<BookDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookDetailResponse> call, @NonNull Response<BookDetailResponse> response) {
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    bookModel = response.body().getBookModel();
                    showBookDetails();
                }
            }

            @Override
            public void onFailure(Call<BookDetailResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

    private void showBookDetails() {
        ImageView img_main = findViewById(R.id.img_main);
        Glide.with(this)
                .load(BASE_BOOK_IMAGES + bookModel.getImage())
                .into(img_main);

        TextView tv_price = findViewById(R.id.tv_price);
        tv_price.setText(bookModel.getPrice() + " " + ConstantUtils.CURRENCY_SYMBOL);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(bookModel.getTitle());

        TextView tv_author = findViewById(R.id.tv_author);
        tv_author.setText(bookModel.getAuthor());

        TextView tv_desc = findViewById(R.id.tv_desc);
        tv_desc.setText(bookModel.getDescription());

        tv_qty.setText(String.valueOf(orderTable.getCartItemQty(bookModel.getProduct_id())));
    }
}