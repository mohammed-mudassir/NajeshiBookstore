package com.codewithabdul.nejashibookstore.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.TabsPagerAdapter;
import com.codewithabdul.nejashibookstore.models.OrderModel;

public class OrderDetailsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        OrderModel order = (OrderModel) getIntent().getSerializableExtra("order");

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, order, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);

        ImageView img_back = findViewById(R.id.iv_back);
        img_back.setOnClickListener(v -> {
            onBackPressed();
        });


    }
}
