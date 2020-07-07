package com.codewithabdul.nejashibookstore.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.fragments.AddressFragment;
import com.codewithabdul.nejashibookstore.fragments.PaymentOptionFragment;
import com.codewithabdul.nejashibookstore.fragments.ShippingFragment;

public class CheckoutActivity extends AppCompatActivity {

    TextView tv_dot1, tv_address, tv_dot2, tv_shipping, tv_dot3, tv_payment_option;
    View view1, view2, view3, view4;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            startActivity(new Intent(CheckoutActivity.this, MainActivity.class)
                    .putExtra("goto", "cart"));
            finish();
        });

        tv_dot1 = findViewById(R.id.tv_dot1);
        tv_address = findViewById(R.id.tv_address);
        tv_dot2 = findViewById(R.id.tv_dot2);
        tv_shipping = findViewById(R.id.tv_shipping);
        tv_dot3 = findViewById(R.id.tv_dot3);
        tv_payment_option = findViewById(R.id.tv_payment_option);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);

        LinearLayout ll_address = findViewById(R.id.ll_address);
        ll_address.setOnClickListener(v -> {
            selectionChanged(0);
        });
        LinearLayout ll_shipping = findViewById(R.id.ll_shipping);
        ll_shipping.setOnClickListener(v -> {
            selectionChanged(1);
        });
        LinearLayout ll_payment = findViewById(R.id.ll_payment);
        ll_payment.setOnClickListener(v -> {
            selectionChanged(2);
        });

        selectionChanged(0);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CheckoutActivity.this, MainActivity.class)
                .putExtra("goto", "cart"));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void selectionChanged(int position) {

        tv_dot2.setBackgroundTintList(ContextCompat.getColorStateList(CheckoutActivity.this, R.color.black));
        tv_shipping.setTextColor(getResources().getColor(R.color.black));

        tv_dot3.setBackgroundTintList(ContextCompat.getColorStateList(CheckoutActivity.this, R.color.black));
        tv_payment_option.setTextColor(getResources().getColor(R.color.black));

        view1.setBackgroundColor(getResources().getColor(R.color.black));
        view2.setBackgroundColor(getResources().getColor(R.color.black));
        view3.setBackgroundColor(getResources().getColor(R.color.black));
        view4.setBackgroundColor(getResources().getColor(R.color.black));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.addToBackStack(null);

        switch (position) {
            case 0:
                AddressFragment addressFragment = new AddressFragment();

                fragmentTransaction.replace(R.id.fl_container, addressFragment, "address");
                fragmentTransaction.commit();
                break;
            case 1:
                tv_dot2.setBackgroundTintList(ContextCompat.getColorStateList(CheckoutActivity.this, R.color.app_color));
                tv_shipping.setTextColor(getResources().getColor(R.color.app_color));

                view1.setBackgroundColor(getResources().getColor(R.color.app_color));
                view2.setBackgroundColor(getResources().getColor(R.color.app_color));

                ShippingFragment shippingFragment = new ShippingFragment();

                fragmentTransaction.replace(R.id.fl_container, shippingFragment, "shipping");
                fragmentTransaction.commit();

                break;
            case 2:
                tv_dot2.setBackgroundTintList(ContextCompat.getColorStateList(CheckoutActivity.this, R.color.app_color));
                tv_shipping.setTextColor(getResources().getColor(R.color.app_color));

                tv_dot3.setBackgroundTintList(ContextCompat.getColorStateList(CheckoutActivity.this, R.color.app_color));
                tv_payment_option.setTextColor(getResources().getColor(R.color.app_color));

                view1.setBackgroundColor(getResources().getColor(R.color.app_color));
                view2.setBackgroundColor(getResources().getColor(R.color.app_color));
                view3.setBackgroundColor(getResources().getColor(R.color.app_color));
                view4.setBackgroundColor(getResources().getColor(R.color.app_color));

                PaymentOptionFragment paymentOptionFragment = new PaymentOptionFragment();

                fragmentTransaction.replace(R.id.fl_container, paymentOptionFragment, "payment");
                fragmentTransaction.commit();

                break;
        }
    }
}