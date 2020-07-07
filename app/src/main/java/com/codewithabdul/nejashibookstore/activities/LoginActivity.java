package com.codewithabdul.nejashibookstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.response.LoginResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    private static final String EMAIL = "email";
    private AccessToken mAccessToken;
    private static int REGISTER_REQ = 1002;
    private static int LOGIN_PHONE_REQ = 1003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        google_sign_in();

        loginButton = findViewById(R.id.login_details_fb_iV_id);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mAccessToken = loginResult.getAccessToken();
                getUserProfile(mAccessToken);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "User cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error", "" + error.toString());
            }
        });

        RelativeLayout rl_fb_login = findViewById(R.id.rl_fb_login);
        rl_fb_login.setOnClickListener(v -> {
            loginButton.performClick();
        });

        RelativeLayout rl_google_login = findViewById(R.id.rl_google_login);
        rl_google_login.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        TextView phone_login_layout = findViewById(R.id.phone_login_layout);
        phone_login_layout.setOnClickListener(v -> {
            startActivityForResult(new Intent(LoginActivity.this, LoginPhoneActivity.class), LOGIN_PHONE_REQ);
        });
    }

    private void google_sign_in() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, connectionResult -> {
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (requestCode == REGISTER_REQ && resultCode == RESULT_OK) {
            if (data.getStringExtra("status") != null && data.getStringExtra("status").contentEquals("success")) {
                Toast.makeText(LoginActivity.this, "Registration successful. Login again to continue!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == LOGIN_PHONE_REQ && resultCode == RESULT_OK) {
            String mobile = data.getStringExtra("mobile");
            String email = mobile + "@mobile.com";
            loginUser(email, mobile, "", "", mobile);
        }
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                (object, response) -> {
                    try {
                        String fb_user_id = object.getString("id");
                        String imgurl = "https://graph.facebook.com/" + object.getString("id") + "/picture?width=500";
                        String fb_name = object.getString("name");
                        String fb_email = fb_user_id + "@fb.com";
                        loginUser(fb_email, fb_user_id, fb_name, imgurl, "");
                        LoginManager.getInstance().logOut();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void loginUser(String email_id, String password, String name, String profile, String mobile) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("email", email_id);
        map.put("password", password);
        Call<LoginResponse> call = apiInterface.login(map);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    AppPref.setKeyProfileImage(LoginActivity.this, profile);
                    AppPref.setLoggedIn(LoginActivity.this, true);
                    AppPref.saveUserInfo(LoginActivity.this, response.body().getUserModel());
                    Intent intent = new Intent();
                    intent.putExtra("status", "success");
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class)
                            .putExtra("name", name)
                            .putExtra("email", email_id)
                            .putExtra("password", password)
                            .putExtra("mobile", mobile)
                            .putExtra("profile", profile), REGISTER_REQ);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            try {
                String g_name = acct.getDisplayName();
                String user_id = acct.getId();
                String imgurl = "";
                if (acct.getPhotoUrl() != null) {
                    imgurl = acct.getPhotoUrl().toString();
                }
                String g_email = user_id + "@gmail.com";
                loginUser(g_email, user_id, g_name, imgurl, "");
            } catch (Exception v) {
                Toast.makeText(this, v.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}