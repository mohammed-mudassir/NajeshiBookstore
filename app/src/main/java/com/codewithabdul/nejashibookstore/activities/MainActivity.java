package com.codewithabdul.nejashibookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.database.OrderTable;
import com.codewithabdul.nejashibookstore.fragments.CartFragment;
import com.codewithabdul.nejashibookstore.fragments.ExploreFragment;
import com.codewithabdul.nejashibookstore.fragments.OrderHistoryFragment;
import com.codewithabdul.nejashibookstore.interfaces.CustomOnClickInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.codewithabdul.nejashibookstore.utils.ConstantUtils;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleToggleView;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.codewithabdul.nejashibookstore.utils.ConstantUtils.CURRENCY_SYMBOL;

public class MainActivity extends AppCompatActivity implements CustomOnClickInterface {

    LinearLayout ll_checkout;
    TextView tv_total;
    OrderTable orderTable;
    BubbleNavigationConstraintView bottomNavigation;
    private static int LOGIN_REQ = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String profile = AppPref.getKeyProfileImage(this);

        CircleImageView civ_profile = findViewById(R.id.civ_profile);
        Glide.with(this)
                .load(profile)
                .error(getResources().getDrawable(R.drawable.ic_user))
                .into(civ_profile);

        ll_checkout = findViewById(R.id.ll_checkout);
        ll_checkout.setOnClickListener(v -> {
            if (AppPref.isLoggedIn(MainActivity.this)) {
                startActivity(new Intent(MainActivity.this, PayPalConfig.class));
            } else {
                startActivity(new Intent(MainActivity.this, PayPalConfig.class));

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setTitle("Login to continue");
                builder.setMessage("You are not logged in. Login first to continue.");
                builder.setPositiveButton("OK",
                        (dialog, which) -> {
                            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LOGIN_REQ);
                        });
                builder.setNegativeButton("Cancel",
                        (dialog, which) -> {
                        });
                builder.show();
            }
        });
        tv_total = findViewById(R.id.tv_total);

        orderTable = new OrderTable(this);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setNavigationChangeListener((view, position) -> {
            ll_checkout.setVisibility(position == 1 && orderTable.getTotalCartItemQty() > 0 ? View.VISIBLE : View.GONE);
            switch (position) {
                case 0:
                    ExploreFragment exploreFrag = new ExploreFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, exploreFrag, "exploreFrag")
                            .commit();
                    break;
                case 1:
                    CartFragment cartFrag = new CartFragment(MainActivity.this);
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.container, cartFrag, "cartFrag").
                            commit();
                    break;
                case 2:
                    OrderHistoryFragment orderHistoryFrag = new OrderHistoryFragment();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.container, orderHistoryFrag, "orderHistoryFrag").
                            commit();
                    break;
                case 3:
                    Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show();
//                    WishlistFragment wishlistFrag = new WishlistFragment();
//                    getSupportFragmentManager().beginTransaction().
//                            replace(R.id.container, wishlistFrag, "wishlistFrag").
//                            commit();
                    break;
            }
        });
        ExploreFragment exploreFrag = new ExploreFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, exploreFrag, "exploreFrag")
                .commit();

        String gotoTab = getIntent().getStringExtra("goto");
        if (gotoTab != null) {
            if (gotoTab.equals("cart")) {
                CartFragment cartFrag = new CartFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, cartFrag, "cartFrag").
                        commit();
                bottomNavigation.setCurrentActiveItem(1);
                BubbleToggleView item_cart = findViewById(R.id.l_item_home2);
                item_cart.toggle();
            }
            if (gotoTab.equals("orders")) {
                OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, orderHistoryFragment, "orderHistoryFrag").
                        commit();
                bottomNavigation.setCurrentActiveItem(2);
                BubbleToggleView item_orders = findViewById(R.id.l_item_home3);
                item_orders.toggle();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ && resultCode == RESULT_OK) {
            String profile = AppPref.getKeyProfileImage(this);

            CircleImageView civ_profile = findViewById(R.id.civ_profile);
            Glide.with(this)
                    .load(profile)
                    .error(getResources().getDrawable(R.drawable.ic_user))
                    .into(civ_profile);
        }
    }

    @Override
    public void onClick(String total) {
        tv_total.setText(orderTable.getTotalCartItemPrice() + " " + ConstantUtils.CURRENCY_SYMBOL);
        ll_checkout.setVisibility(orderTable.getTotalCartItemQty() > 0 ? View.VISIBLE : View.GONE);
        if (orderTable.getTotalCartItemQty() > 0) {
            bottomNavigation.setBadgeValue(1, String.valueOf(orderTable.getTotalCartItemQty()));
        } else {
            bottomNavigation.setBadgeValue(1, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_total.setText(orderTable.getTotalCartItemPrice() + " " + ConstantUtils.CURRENCY_SYMBOL);
        if (orderTable.getTotalCartItemQty() > 0) {
            bottomNavigation.setBadgeValue(1, String.valueOf(orderTable.getTotalCartItemQty()));
        } else {
            bottomNavigation.setBadgeValue(1, null);
        }
    }
}
