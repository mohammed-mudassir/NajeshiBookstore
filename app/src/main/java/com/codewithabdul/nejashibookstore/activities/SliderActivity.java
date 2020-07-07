package com.codewithabdul.nejashibookstore.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.MyViewPagerAdapter;
import com.codewithabdul.nejashibookstore.utils.AppPref;

public class SliderActivity extends AppCompatActivity {

    TextView tv_start;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        if (!AppPref.isKeyFirstLaunch(this)) {
            launchHomeScreen();
        }

        tv_start = findViewById(R.id.tv_start);

        viewPager = findViewById(R.id.vp_welcome);
        dotsLayout = findViewById(R.id.layoutDots);
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};

        // adding bottom dots
        addBottomDots(0);
        tv_start.setText(getResources().getString(R.string.start));

        myViewPagerAdapter = new MyViewPagerAdapter(this, layouts);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        tv_start.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });
    }

    private void launchHomeScreen() {
        AppPref.setKeyFirstLaunch(this, false);
        startActivity(new Intent(SliderActivity.this, MainActivity.class));
        finish();
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            switch (position) {
                case 0:
                    tv_start.setText(getResources().getString(R.string.start));
                    break;
                case 1:
                    tv_start.setText(getResources().getString(R.string.next));
                    break;
                case 2:
                    tv_start.setText(getResources().getString(R.string.finish));
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
}