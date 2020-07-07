package com.codewithabdul.nejashibookstore.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.models.AddressModel;
import com.codewithabdul.nejashibookstore.response.BaseResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditAddressActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener {

    AddressModel addressModel;

    private GoogleMap googleMap;
    SupportMapFragment mapFragment;
    Marker mCurrLocationMarker;
    Location mLastLocation;

    double latitude = 0, longitude = 0;

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 1004;

    ImageView iv_location_pin;

    RelativeLayout select_country;
    TextView countrytxt, countrycodetxt;
    EditText et_title, et_description, phonetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_address);

        addressModel = (AddressModel) getIntent().getSerializableExtra("address");

        iv_location_pin = findViewById(R.id.iv_location_pin);
        et_title = findViewById(R.id.et_title);
        et_description = findViewById(R.id.et_description);
        phonetxt = findViewById(R.id.phonetxt);
        if (addressModel != null) {
            et_title.setText(addressModel.getTitle());
            et_description.setText(addressModel.getDescription());
            phonetxt.setText(addressModel.getContact());
        }

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.address));

        Button btn_action = findViewById(R.id.btn_action);
        btn_action.setText(getResources().getString(addressModel == null ? R.string.add_address : R.string.update_address));
        btn_action.setOnClickListener(v -> {
            doAction();
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        select_country = findViewById(R.id.select_country);
        select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opencountry();
            }
        });

        countrytxt = findViewById(R.id.countrytxt);
        countrycodetxt = findViewById(R.id.countrycodetxt);
    }

    private void doAction() {
        if (et_title.getText().toString().contentEquals("")) {
            Toast.makeText(this, getResources().getString(R.string.pl_enter_title), Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_description.getText().toString().contentEquals("")) {
            Toast.makeText(this, getResources().getString(R.string.pl_enter_desc), Toast.LENGTH_SHORT).show();
            return;
        }
        if (phonetxt.getText().toString().contentEquals("")) {
            Toast.makeText(this, getResources().getString(R.string.pl_enter_mobile), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_id", AppPref.getUserId(this));
        map.put("title", et_title.getText().toString());
        map.put("description", et_description.getText().toString());
        map.put("contact", countrycodetxt.getText().toString() + phonetxt.getText().toString());
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
        if (addressModel != null) {
            // update address
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            map.put("add_id", addressModel.getAdd_id());

            Call<BaseResponse> call = apiInterface.updateAddress(map);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                    assert response.body() != null;
                    if (response.body().getStatus().contentEquals("success")) {
                        Toast.makeText(AddEditAddressActivity.this, getResources().getString(R.string.address_updated_success), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddEditAddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(AddEditAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // add address
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<BaseResponse> call = apiInterface.addAddress(map);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                    assert response.body() != null;
                    if (response.body().getStatus().contentEquals("success")) {
                        Toast.makeText(AddEditAddressActivity.this, getResources().getString(R.string.address_added_success), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddEditAddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(AddEditAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (addressModel != null) {
            addMarker(Double.parseDouble(addressModel.getLatitude()), Double.parseDouble(addressModel.getLongitude()));
        } else {
            fetchLocation();
        }
        if (googleMap != null) {
            googleMap.setOnCameraMoveListener(this);
            googleMap.setOnCameraIdleListener(this);
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLastLocation = location;
                    addMarker(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    private void addMarker(double lat, double lng) {
        this.googleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        this.googleMap.addMarker(markerOptions);
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = this.googleMap.addMarker(markerOptions);

        //move map camera
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    @Override
    public void onCameraIdle() {
        iv_location_pin.setVisibility(View.GONE);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(googleMap.getCameraPosition().target);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOptions);
        latitude = googleMap.getCameraPosition().target.latitude;
        longitude = googleMap.getCameraPosition().target.longitude;
    }

    @Override
    public void onCameraMove() {
        googleMap.clear();
        iv_location_pin.setVisibility(View.VISIBLE);
    }
}