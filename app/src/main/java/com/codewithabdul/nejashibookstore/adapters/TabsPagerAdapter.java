package com.codewithabdul.nejashibookstore.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codewithabdul.nejashibookstore.fragments.OrderAddressFragment;
import com.codewithabdul.nejashibookstore.fragments.OrderItemsFragment;
import com.codewithabdul.nejashibookstore.models.OrderModel;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private static final String[] TAB_TITLES = new String[]{"Items", "Address"};
    OrderModel orderModel;
    Context context;

    public TabsPagerAdapter(Context context, OrderModel orderModel, FragmentManager fm) {
        super(fm);
        this.orderModel = orderModel;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OrderItemsFragment(context, orderModel.getProducts());
            case 1:
                return new OrderAddressFragment(context, orderModel.getAddress());
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}
