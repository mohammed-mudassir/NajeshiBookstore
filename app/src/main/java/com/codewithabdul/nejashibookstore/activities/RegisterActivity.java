package com.codewithabdul.nejashibookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.response.BaseResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText et_name, et_last_name, et_mobile;
    String email, password, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        mobile = getIntent().getStringExtra("mobile");
        String profile = getIntent().getStringExtra("profile");
        et_name = findViewById(R.id.et_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_mobile = findViewById(R.id.et_mobile);
        if (mobile != null) {
            et_mobile.setText(mobile);
            et_mobile.setEnabled(false);
        }

        if (name.split(" ").length > 1) {
            et_name.setText(name.split(" ")[0]);
            et_last_name.setText(name.substring(name.lastIndexOf(" ")));
        }

        CircleImageView civ_profile = findViewById(R.id.civ_profile);

        Glide.with(this)
                .load(profile)
                .into(civ_profile);

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(v -> {
            if (validInputs()) {
                registerUser();
            }
        });
    }

    private boolean validInputs() {
        if (et_name.getText().toString().contentEquals("")) {
            Toast.makeText(this, "First name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_last_name.getText().toString().contentEquals("")) {
            Toast.makeText(this, "Last name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_mobile.getText().toString().contentEquals("")) {
            Toast.makeText(this, "Mobile number cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Map<String, String> map = new HashMap<>();
        map.put("fname", et_name.getText().toString());
        map.put("lname", et_last_name.getText().toString());
        map.put("mobile", et_mobile.getText().toString());
        map.put("email", email);
        map.put("password", password);
        map.put("deviceid", "1234567890");
        map.put("user_login", "Normal");

        Call<BaseResponse> call = apiInterface.register(map);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    Intent intent = new Intent();
                    intent.putExtra("status", "success");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }
}