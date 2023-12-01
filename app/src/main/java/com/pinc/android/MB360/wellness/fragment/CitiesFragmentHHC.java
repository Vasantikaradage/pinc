package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentCitiesBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.ui.CityAdapter;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.City;
import com.pinc.android.MB360.wellness.homehealthcare.retrofit.CityInterface;

import java.util.ArrayList;
import java.util.List;

public class CitiesFragmentHHC extends Fragment implements CityInterface {

    FragmentCitiesBinding binding;
    View view;
    NavController navController;

    List<City> cityList = new ArrayList<>();

    // city interface
    CityInterface cityInterface;

    City city;

    // City View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;

    //  Adapter
    CityAdapter adapter;

    public CitiesFragmentHHC() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCitiesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        // viewmodel scope in fragment
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        getCities();

        return view;
    }

    private void getCities() {

//        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
//
//
//        });

        homeHealthCareViewModel.getCitiesAvailable().observe(getViewLifecycleOwner(), cityResponse -> {

            if (cityResponse != null) {

//                if(cityResponse.getCities().get(0).getIsMetro().equals("1")) {
                adapter = new CityAdapter(requireContext(), cityResponse.getCities(), this);
                binding.recyclerViewCities.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, cityResponse.getCities().size());


//                }else {
//                    adapter = new CityAdapter(requireContext(), cityResponse.getCities());
//                    binding.recyclerViewCitiesOther.setAdapter(adapter);
//                }


                if (!cityResponse.getMessage().getStatus()) {
//                    binding.messageTextView.setVisibility(View.VISIBLE);
//                    binding.messageTextView.setText("" + diagnosticCenterResponse.getMessage().getMessage());
                }

            } else {
//                binding.messageTextView.setVisibility(View.VISIBLE);
//                binding.messageTextView.setText("Sorry, data not available");
            }
        });
    }

    @Override
    public void getCity(City city) {

        NavDirections action = CitiesFragmentHHCDirections.actionCitiesFragmentToMembersFragment().setGetCity(city);
        navController.navigate(action);
//        homeHealthCareViewModel.setCitiesLiveDataHHC(city);
//        navController.navigateUp();
    }

    @Override
    public void getCityHC(String city) {
//        packagesViewModel.setCitiesLiveDataHC(city);
//        navController.navigateUp();
    }
}