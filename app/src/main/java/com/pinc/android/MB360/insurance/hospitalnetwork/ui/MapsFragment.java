package com.pinc.android.MB360.insurance.hospitalnetwork.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentMapsBinding;
import com.pinc.android.MB360.insurance.hospitalnetwork.reponseclass.Hospitals;
import com.pinc.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.pinc.android.MB360.utilities.AppLocalConstant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    Hospitals hospitals = new Hospitals();
    private String Name, Address;
    Double lat = 0.0;
    Double lng = 0.0;
    private LatLng hospitalLatLng;
    FragmentMapsBinding binding;
    View view;

    //viewModel
    HospitalNetworkViewModel hospitalNetworkViewModel;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Mumbai, India.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the MapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            showLoading();
            mMap = googleMap;
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            //Initialize Google Play Services
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            } else {
                AppLocalConstant.verifyLocationPermissions(requireActivity());

            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);

        showLoading();

        hospitals.setHospitalName(MapsFragmentArgs.fromBundle(getArguments()).getGetHospitalName());
        hospitals.setHospitalAddress(MapsFragmentArgs.fromBundle(getArguments()).getGetHospitalAddress());
        Name = MapsFragmentArgs.fromBundle(getArguments()).getGetHospitalName();
        Address = MapsFragmentArgs.fromBundle(getArguments()).getGetHospitalAddress();

        hospitalNetworkViewModel = new ViewModelProvider(requireActivity()).get(HospitalNetworkViewModel.class);


        GeocodingLocation locationAddress = new GeocodingLocation();
        String queryAddress = hospitals.getHospitalName() + " " + hospitals.getHospitalAddress();
        queryAddress = queryAddress.replace(", ,", ",");
        Log.d("LOCATION", "search-Hospital: " + queryAddress);
        locationAddress.getAddressFromLocation(hospitals.getHospitalName(),
                requireContext(), new GeocoderHandler());


        String AddressQuery = Name + "," + Address;
        AddressQuery = AddressQuery.replace(", ,", ",");
        AddressQuery = AddressQuery.replace(", ", "+");

       /* hospitalNetworkViewModel.getHospitalsLocation(AddressQuery).observe(getViewLifecycleOwner(), placesResponse -> {
            try {
                hospitalLatLng = new LatLng(placesResponse.getResults().get(0).getGeometry().getLocation().getLat(), placesResponse.getResults().get(0).getGeometry().getLocation().getLng());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/

        view = binding.getRoot();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }

    private class GeocoderHandler extends Handler {
        //show loading in the ui
        @Override
        public void handleMessage(Message message) {
            String address;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    address = bundle.getString("address");
                    lat = bundle.getDouble("lat", 0.0);
                    lng = bundle.getDouble("lng", 0.0);
                    break;
                default:
                    address = null;
            }
            hospitalLatLng = new LatLng(lat, lng);
            if (lat == 0.0 && lng == 0.0) {
                Toast.makeText(requireActivity(), "Location not Found!", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
                hideLoading();
            }
            showLoading();
            mMap.addMarker(new MarkerOptions().position(hospitalLatLng).title(Name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLatLng, 15.0f));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(hospitalLatLng));
            Log.d("LOCATION", "handleMessage: " + String.valueOf(hospitalLatLng));
            hideLoading();


        }
    }


    private void showLoading() {
        binding.progressLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressLayout.setVisibility(View.GONE);
    }
}