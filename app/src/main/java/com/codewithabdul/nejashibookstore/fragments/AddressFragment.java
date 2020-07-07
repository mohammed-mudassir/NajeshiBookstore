package com.codewithabdul.nejashibookstore.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.codewithabdul.nejashibookstore.R;
import com.codewithabdul.nejashibookstore.adapters.AddressAdapter;
import com.codewithabdul.nejashibookstore.interfaces.AddressSelectionInterface;
import com.codewithabdul.nejashibookstore.models.AddressModel;
import com.codewithabdul.nejashibookstore.response.AddressesResponse;
import com.codewithabdul.nejashibookstore.retrofit.ApiClient;
import com.codewithabdul.nejashibookstore.retrofit.ApiInterface;
import com.codewithabdul.nejashibookstore.utils.AppPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressFragment extends Fragment implements AddressSelectionInterface, OnMapReadyCallback {

    AddressAdapter addressAdapter;
    ArrayList<AddressModel> addressModels = new ArrayList<>();
    AddressModel selectedAddress;

    private GoogleMap googleMap;
    SupportMapFragment mapFragment;
    View map;

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        Places.initialize(getContext(), getString(R.string.api_key));

        RecyclerView rv_addresses = view.findViewById(R.id.rv_addresses);
        addressAdapter = new AddressAdapter(getContext(), addressModels, this);
        rv_addresses.setAdapter(addressAdapter);

        getAddresses();

        map = view.findViewById(R.id.map);
        map.setVisibility(View.GONE);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAddresses();
    }

    private void getAddresses() {
        addressModels.clear();
        addressAdapter.notifyDataSetChanged();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", AppPref.getUserId(getContext()));
        Call<AddressesResponse> call = apiInterface.get_addresses(map);
        call.enqueue(new Callback<AddressesResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddressesResponse> call, @NonNull Response<AddressesResponse> response) {
                assert response.body() != null;
                if (response.body().getStatus().contentEquals("success")) {
                    addressModels.clear();
                    addressModels.addAll(response.body().getAddresses());
                    addressAdapter.notifyDataSetChanged();
                    if (!AppPref.getKeySelectedAddress(getContext()).equals("0")) {
                        addressAdapter.setSelectionById(AppPref.getKeySelectedAddress(getContext()));
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressesResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAddressSelected(AddressModel addressModel) {
        this.selectedAddress = addressModel;
        AppPref.setKeySelectedAddress(getContext(), addressModel.getAdd_id());
        map.setVisibility(selectedAddress == null ? View.GONE : View.VISIBLE);
        if (selectedAddress != null) {
            addMarker(Double.parseDouble(addressModel.getLatitude()), Double.parseDouble(addressModel.getLongitude()));
        }
    }

    private void addMarker(double lat, double lng) {
        this.googleMap.clear();
        this.googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}