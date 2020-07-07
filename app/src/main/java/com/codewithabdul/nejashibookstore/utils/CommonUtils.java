package com.codewithabdul.nejashibookstore.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.codewithabdul.nejashibookstore.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class CommonUtils {


    private static ProgressDialog progressDialog;

    public CommonUtils() {


    }

    public static boolean is_gps_enabled(Context mContext) {

        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }


    public static void shareApp(Context context) {

        String message = "Hey! Check out " + context.getString(R.string.app_name) + " app. Get it here\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.share_with)));
    }


    public static ProgressDialog showProgressDialog(Context context, String message) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);

        progressDialog.show();

        return progressDialog;


    }


    public static void cancelProgressDialog(ProgressDialog progressDialog) {

        if (progressDialog.isShowing()) {


            progressDialog.cancel();
        }
    }


    public static void showToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static String dateFormatConverter(String oldDate) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(oldDate);

        String newstring = new SimpleDateFormat("dd-MMMM-yyyy").format(date);
        return newstring;
    }


    public static void setTitle(Context context, String title) {
        ((AppCompatActivity) context).getSupportActionBar().setTitle(title);
    }


    public static String changeDateFormat(String inDate) {
        String outDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy h:mm a");
        try {
            Date date = input.parse(inDate);   // parse input
            outDate = output.format(date);    // format output
        } catch (ParseException e) {
            Log.e("Date Exception", e.toString());
        }
        return outDate;
    }


    public static String getCurrentDateTime() {


        Calendar c = Calendar.getInstance();
        // System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;


    }


    public static String getCurrentDate() {


        Calendar c = Calendar.getInstance();
        // System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;


    }


    public static String getDateFromSdf(Calendar calendar) {

        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String currentDate = sdf.format(date);

        return currentDate;

    }

    public static String removeSpace(String s) {
        String withoutspaces = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ')
                withoutspaces += s.charAt(i);

        }
        return withoutspaces;

    }


    public static int getRandom() {

        Random rand = new Random();

        int n = rand.nextInt(50) + 1;

        return n;
    }

    public static int spanCount(Context context, WindowManager windowManager) {

        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = context.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 90);
        return columns;
    }

 /*   @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context){

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }*/


    public static boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }


    public static void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


    public static void removeStatusBar(Activity activity) {

        activity.getWindow()
                .setFlags(WindowManager.
                                LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    public static void manageVisibility(View view, int i) {

        if (i == View.GONE) {
            if (view.getVisibility() == View.VISIBLE)
                view.setVisibility(View.GONE);

        } else if (i == View.VISIBLE) {
            if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE)
                view.setVisibility(View.VISIBLE);

        }

    }

    public static void setUIToWait(boolean wait, Context context, String message) {
        try {
            if (wait && (progressDialog == null || !progressDialog.isShowing())) {
                progressDialog = ProgressDialog.show(context, null, null);
                progressDialog.setCancelable(true);
                if (message != null && message.trim().length() > 0)
                    progressDialog.setMessage(message.trim());
            } else if (!wait && progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Drawable setToolbarArrowColor(Context context) {

        final Drawable upArrow = ContextCompat.getDrawable(context, R.drawable.back);
        upArrow.setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        return upArrow;
    }

    //method to convert your text to image
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static void strikeTextView(TextView textView) {


        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    public static String getImageEndpoint() {
        return "";
    }
}
