package com.pinc.android.MB360.insurance.hospitalnetwork.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentProviderNetworkBinding;
import com.pinc.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;
import com.pinc.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.pinc.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProviderNetworkFragment extends Fragment implements OnHospitalSelectedListener {

    FragmentProviderNetworkBinding binding;
    View view;
    ProviderAdapter adapter;
    HospitalNetworkViewModel hospitalNetworkViewModel;

    List<HospitalInformation> filteredHospitals = new ArrayList<>();
    List<HospitalInformation> nonFilteredHospital = new ArrayList<>();
    NavController navController;

    boolean primary = false;
    boolean secondary = false;
    boolean tertiary = false;
    boolean other = false;


    public ProviderNetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProviderNetworkBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        setHasOptionsMenu(true);

        hospitalNetworkViewModel = new ViewModelProvider(requireActivity()).get(HospitalNetworkViewModel.class);


        getHospitals();


        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        //onclick
        binding.llPrimary.setOnClickListener(v -> {
            if (!primary) {
                primary = true;
                binding.llPrimary.setBackgroundResource(R.drawable.selector_hospital);
                binding.llSecondary.setBackgroundResource(0);
                binding.llTertiary.setBackgroundResource(0);
                binding.llOther.setBackgroundResource(0);

                secondary = false;
                tertiary = false;
                other = false;
                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterPrimary();
            } else {
                resetFilter();

            }
        });

        binding.llSecondary.setOnClickListener(v -> {
            if (!secondary) {
                primary = false;
                secondary = true;
                binding.llPrimary.setBackgroundResource(0);
                binding.llSecondary.setBackgroundResource(R.drawable.selector_hospital);
                binding.llTertiary.setBackgroundResource(0);
                binding.llOther.setBackgroundResource(0);
                tertiary = false;
                other = false;
                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterSecondary();
            } else {
                resetFilter();

            }
        });

        binding.llTertiary.setOnClickListener(v -> {
            if (!tertiary) {
                primary = false;
                secondary = false;
                tertiary = true;
                binding.llPrimary.setBackgroundResource(0);
                binding.llSecondary.setBackgroundResource(0);
                binding.llTertiary.setBackgroundResource(R.drawable.selector_hospital);
                binding.llOther.setBackgroundResource(0);
                other = false;
                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterTertiary();
            } else {
                resetFilter();
            }
        });
        binding.llOther.setOnClickListener(v -> {
            if (!other) {
                primary = false;
                secondary = false;
                tertiary = false;
                other = true;
                binding.llPrimary.setBackgroundResource(0);
                binding.llSecondary.setBackgroundResource(0);
                binding.llTertiary.setBackgroundResource(0);
                binding.llOther.setBackgroundResource(R.drawable.selector_hospital);

                binding.hospitalSearchView.setQuery(null, false);
                binding.hospitalSearchView.setVisibility(View.GONE);
                setMenuVisibility(true);
                filterOther();
            } else {
                resetFilter();
            }
        });


        hospitalNetworkViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

        });


        binding.hospitalSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterByText(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterByText(s);
                return false;
            }
        });


        return view;


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.network_provider_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            if (binding.hospitalSearchView.getVisibility() == View.GONE) {
                binding.hospitalSearchView.setVisibility(View.VISIBLE);
                item.setIcon(R.drawable.close_white);
                setMenuVisibility(false);
            } else {
                binding.hospitalSearchView.setVisibility(View.GONE);
                item.setIcon(R.drawable.search);
                setMenuVisibility(true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setCounts(List<HospitalInformation> hospitalList) {

        binding.hospCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.size())));

        //primary
        binding.priCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (hospital.getHospLevelOfCare().toLowerCase().equals("primary"))).count())));

        //secondary
        binding.secCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (hospital.getHospLevelOfCare().toLowerCase().equals("secondary"))).count())));

        //tertiary
        binding.terCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (hospital.getHospLevelOfCare().toLowerCase().equals("tertiary"))).count())));

        //others
        binding.otherCount.setText(UtilMethods.PriceFormat(String.valueOf(hospitalList.stream().filter(hospital -> (!hospital.getHospLevelOfCare().toLowerCase().equals("primary")) && (!hospital.getHospLevelOfCare().toLowerCase().equals("secondary")) && (!hospital.getHospLevelOfCare().toLowerCase().equals("tertiary"))).count())));


    }

    private void filterByText(String searchedString) {

        //filtering of the  search-view text hospitals
        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospName().toLowerCase().contains(searchedString.toLowerCase())) || hospital.getHospAddress().toLowerCase().contains(searchedString.toLowerCase())).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);
        binding.hospCount.setText(UtilMethods.PriceFormat(String.valueOf(filteredHospitals.size())));

        if (filteredHospitals.size() == 0) {
            binding.noHospitalFoundText.setText("No Hospitals Found!");
            binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
        } else {
            binding.emptyHospitalsLayout.setVisibility(View.GONE);
        }
        setCounts(filteredHospitals);
    }


    private void resetFilter() {

        //reset the filters
        adapter = new ProviderAdapter(requireContext(), nonFilteredHospital, this);
        adapter.notifyItemRangeChanged(0, nonFilteredHospital.size());
        binding.hospitalCycle.setAdapter(adapter);

        if (nonFilteredHospital.size() == 0) {
            binding.noHospitalFoundText.setText("No Hospitals Found!");
            binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
        } else {
            binding.emptyHospitalsLayout.setVisibility(View.GONE);
        }

        primary = false;
        secondary = false;
        tertiary = false;
        other = false;

        binding.llPrimary.setBackgroundResource(0);
        binding.llSecondary.setBackgroundResource(0);
        binding.llTertiary.setBackgroundResource(0);
        binding.llOther.setBackgroundResource(0);
    }

    private void filterPrimary() {

        //filtering of the  Primary hospitals
        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospLevelOfCare().toLowerCase().equals("primary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);

        if (filteredHospitals.size() == 0) {
            binding.noHospitalFoundText.setText("No Primary Hospitals Found!");
            binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
        } else {
            binding.emptyHospitalsLayout.setVisibility(View.GONE);
        }
    }

    private void filterTertiary() {

        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospLevelOfCare().toLowerCase().equals("tertiary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);
        if (filteredHospitals.size() == 0) {
            binding.noHospitalFoundText.setText("No Tertiary Hospitals Found!");
            binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
        } else {
            binding.emptyHospitalsLayout.setVisibility(View.GONE);
        }
    }

    private void filterOther() {

        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (!hospital.getHospLevelOfCare().toLowerCase().equals("primary")) && (!hospital.getHospLevelOfCare().toLowerCase().equals("secondary")) && (!hospital.getHospLevelOfCare().toLowerCase().equals("tertiary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);

        if (filteredHospitals.size() == 0) {
            binding.noHospitalFoundText.setText("No Other Hospitals Found!");
            binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
        } else {
            binding.emptyHospitalsLayout.setVisibility(View.GONE);
        }
    }

    private void filterSecondary() {

        filteredHospitals = nonFilteredHospital.stream().filter(hospital -> (hospital.getHospLevelOfCare().toLowerCase().equals("secondary"))).collect(Collectors.toList());
        adapter = new ProviderAdapter(requireContext(), filteredHospitals, this);
        adapter.notifyItemRangeChanged(0, filteredHospitals.size());
        binding.hospitalCycle.setAdapter(adapter);

        if (filteredHospitals.size() == 0) {
            binding.noHospitalFoundText.setText("No Secondary Hospitals Found!");
            binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
        } else {
            binding.emptyHospitalsLayout.setVisibility(View.GONE);
        }
    }

    private void getHospitals() {
        ArrayList<HospitalInformation> hospital_list = new ArrayList<>();

        adapter = new ProviderAdapter(requireContext(), hospital_list, this);
        binding.hospitalCycle.setAdapter(adapter);
        hospitalNetworkViewModel.getHospitalsData().observe(getViewLifecycleOwner(), hospitalData -> {
            if (hospitalData != null) {

                if (!hospitalData.getHospitalInformation().isEmpty()) {
                    hospital_list.addAll(hospitalData.getHospitalInformation());
                    adapter.notifyItemRangeChanged(0, hospital_list.size());


                    filteredHospitals = hospitalData.getHospitalInformation();
                    nonFilteredHospital = hospitalData.getHospitalInformation();
                } else {
                    binding.noHospitalFoundText.setText(getString(R.string.no_hospital_found));
                    binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
                }
            } else {
                binding.noHospitalFoundText.setText(getString(R.string.no_hospital_found));
                binding.emptyHospitalsLayout.setVisibility(View.VISIBLE);
            }

            //to set the count of the total available hospital
            setCounts(hospital_list);

        });

    }


    @Override
    public void selectedHospital(String name, String address) {
        NavDirections actions = ProviderNetworkFragmentDirections.actionProviderNetworkToMapsFragment(name, address);
        navController.navigate(actions);
    }
}