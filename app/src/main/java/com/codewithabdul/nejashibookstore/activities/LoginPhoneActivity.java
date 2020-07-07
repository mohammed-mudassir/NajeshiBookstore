package com.codewithabdul.nejashibookstore.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.utils.CommonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth fbAuth;
    EditText phoneText, digit1, digit2, digit3, digit4, digit5, digit6;
    TextView countrytxt, countrycodetxt, sendtotxt;
    RelativeLayout select_country;
    ViewFlipper viewFlipper;
    String phoneNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        fbAuth = FirebaseAuth.getInstance();

        fbAuth.setLanguageCode("en");


        phoneText = findViewById(R.id.phonetxt);

        select_country = findViewById(R.id.select_country);
        select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opencountry();
            }
        });

        countrytxt = findViewById(R.id.countrytxt);
        countrycodetxt = findViewById(R.id.countrycodetxt);

        sendtotxt = findViewById(R.id.sendtotxt);

        viewFlipper = findViewById(R.id.viewfillper);

        codefill();

    }

    //message code fill in edittext and change focus in android
    public void codefill() {

        digit1 = findViewById(R.id.digitone);
        digit2 = findViewById(R.id.digittwo);
        digit3 = findViewById(R.id.digitthree);
        digit4 = findViewById(R.id.digitfour);
        digit5 = findViewById(R.id.digitfive);
        digit6 = findViewById(R.id.digitsix);

        digit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit1.getText().toString().length() == 0) {
                    digit2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit2.getText().toString().length() == 0) {
                    digit3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit3.getText().toString().length() == 0) {
                    digit4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit4.getText().toString().length() == 0) {
                    digit5.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        digit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (digit5.getText().toString().length() == 0) {
                    digit6.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    String country_iso_code = "US";

    @SuppressLint("WrongConstant")
    public void Opencountry() {

        final CountryPicker picker = CountryPicker.newInstance(getResources().getString(R.string.select_country));
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                countrytxt.setText(name);
                countrycodetxt.setText(dialCode);
                picker.dismiss();
                country_iso_code = code;
            }
        });
        picker.setStyle(R.style.countrypicker_style, R.style.countrypicker_style);
        picker.show(getSupportFragmentManager(), getResources().getString(R.string.select_country));

    }

    public void Nextbtn(View view) {

        phoneNumber = countrycodetxt.getText().toString() + phoneText.getText().toString();
        Send_Number_tofirebase(phoneNumber);

    }


    public void Send_Number_tofirebase(String phoneNumber) {
        setUpVerificatonCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {
        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(this, getResources().getString(R.string.please_wait));
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                        CommonUtils.cancelProgressDialog(progressDialog);
                        signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        CommonUtils.cancelProgressDialog(progressDialog);

                        Log.d("responce", e.toString());
                        Toast.makeText(LoginPhoneActivity.this, getResources().getString(R.string.enter_correct_number), Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                        CommonUtils.cancelProgressDialog(progressDialog);

                        phoneVerificationId = verificationId;
                        resendToken = token;
                        sendtotxt.setText(getResources().getString(R.string.send_to) + " ( " + phoneNumber + " )");
                        viewFlipper.setInAnimation(LoginPhoneActivity.this, R.anim.in_from_right);
                        viewFlipper.setOutAnimation(LoginPhoneActivity.this, R.anim.out_to_left);
                        viewFlipper.setDisplayedChild(1);

                    }
                };
    }


    public void verifyCode(View view) {
        String code = "" + digit1.getText().toString() + digit2.getText().toString() + digit3.getText().toString() + digit4.getText().toString() + digit5.getText().toString() + digit6.getText().toString();
        if (!code.equals("")) {
            progressDialog = CommonUtils.showProgressDialog(this, getResources().getString(R.string.please_wait));
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, getResources().getString(R.string.enter_correct_code), Toast.LENGTH_SHORT).show();
        }


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // get the user info to know that user is already login or not
                            Intent intent = new Intent();
                            intent.putExtra("mobile", phoneNumber);
                            setResult(RESULT_OK, intent);
                            finish();

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {

                                CommonUtils.cancelProgressDialog(progressDialog);
                            }
                        }
                    }
                });
    }


    public void resendCode(View view) {

        String phoneNumber = phoneText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }


    public void Goback_1(View view) {
        finish();
    }

    public void Goback(View view) {
        viewFlipper.setInAnimation(LoginPhoneActivity.this, R.anim.in_from_left);
        viewFlipper.setOutAnimation(LoginPhoneActivity.this, R.anim.out_to_right);
        viewFlipper.setDisplayedChild(0);
    }

}
