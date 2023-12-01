package com.pinc.android.MB360;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.pinc.android.MB360.databinding.FragmentAddAddressBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Address;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AddAddressFragment extends Fragment {

    FragmentAddAddressBinding binding;
    View view;
    Address address = new Address();
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;

    ArrayList<String> cityList = new ArrayList<>();
    ArrayList<String> stateList = new ArrayList<>();
    NavController navController;
    FamilyMember member;

    public AddAddressFragment() {
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
        binding = FragmentAddAddressBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);


        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {
                address.setWellSrno(employeeCheckResponse.getExtGroupSrNo());
            });
        });

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        setupStatesAndCities();


        ArrayAdapter adState = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, stateList);
        ArrayAdapter adCity = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, cityList);

        adState.setDropDownViewResource(R.layout.spinner_item);
        adCity.setDropDownViewResource(R.layout.spinner_item);

        binding.spinnerCity.setAdapter(adCity);
        binding.spinnerState.setAdapter(adState);

        Window window = getActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(view.getContext(), R.color.greenlightbg2));

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCity = adapterView.getItemAtPosition(i).toString();
                address.setStrCity(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                address.setStrCity(null);
            }
        });

        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                Context context = adapterView.getContext();
                CharSequence text = selected;
                address.setStrState(selected);
                addPreferedCities();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                address.setStrState(null);
            }
        });
        try {
            String html = " I agree to <a href=\"https://portal.mybenefits360.com/termsOfUse.html\">Terms of Use</a>";
            Spanned result;
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);

            Spannable s = (Spannable) result;
            for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
                s.setSpan(new UnderlineSpan() {
                    public void updateDrawState(TextPaint tp) {
                        tp.setUnderlineText(false);
                    }
                }, s.getSpanStart(u), s.getSpanEnd(u), 0);
            }

            binding.chkAgreeTerms.setText(s);
            binding.chkAgreeTerms.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.ivClose.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.btnsaveupdateaddress.setOnClickListener(v -> {

            try {
                if (binding.chkAgreeTerms.isChecked()) {
                    address.setStrMobileNumber(binding.etmobileno.getText().toString().trim());
                    address.setStrLine1(binding.etflatno.getText().toString().trim());
                    address.setStrLine2(binding.etlocation.getText().toString().trim());
                    address.setStrLandmark(binding.etlandmark.getText().toString().trim());
                    address.setStrPincode(binding.etpincode.getText().toString().trim());
                    address.setStrEmail(binding.etemailid.getText().toString());

                    saveAddress();
                }

            } catch (Exception e) {

            }

        });


        return view;

    }

    private void saveAddress() {
        if (!addressIsNotValid()) {
            //post call here for address
            homeHealthCareViewModel.addAddress(address);

            homeHealthCareViewModel.addAddressData().observe(getViewLifecycleOwner(), addressResponse -> {

                Toast.makeText(getContext(), addressResponse.getMessage().getMessage(), Toast.LENGTH_SHORT).show();

                NavDirections actions = AddAddressFragmentDirections.actionAddAddressFragmentToTrainedAttendedFragment(member);
                navController.navigate(actions);
            });

        } else {
            Toast.makeText(getContext(), address.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean addressIsNotValid() {
        boolean isError = false;
        try {
            if (TextUtils.isEmpty(address.getStrMobileNumber()) ||
                    address.getStrMobileNumber().length() <= 9) {
                isError = true;
                binding.etmobileno.setError("Invalid Mobile Number");
            }

            if (TextUtils.isEmpty(address.getStrEmail()) ||
                    !Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(address.getStrEmail())).matches()) {
                isError = true;
                binding.etemailid.setError("Invalid E-Mail ID");
            }

            if (TextUtils.isEmpty(address.getStrLine1())) {
                isError = true;
                binding.etflatno.setError("Enter Flat No.");
            }

            if (TextUtils.isEmpty(address.getStrLine2())) {
                isError = true;
                binding.etArea.setError("Enter Area/Locality");
            }

            if (TextUtils.isEmpty(address.getStrLandmark())) {
                isError = true;
                binding.etlandmark.setError("Enter Landmark");
            }
            if (TextUtils.isEmpty(address.getStrPincode()) ||
                    address.getStrPincode().length() < 5) {
                isError = true;
                binding.etpincode.setError("Enter Pin-code");
            }

            if (TextUtils.isEmpty(address.getStrState())) {
                isError = true;
                Toast.makeText(getContext(), "Enter state", Toast.LENGTH_SHORT).show();
            }

            if (TextUtils.isEmpty(address.getStrCity())) {
                isError = true;
                Toast.makeText(getContext(), "Enter city", Toast.LENGTH_SHORT).show();
            }
            if (!binding.chkAgreeTerms.isChecked()) {
                isError = true;
                binding.chkAgreeTerms.setError("Please accept the terms and conditions");
            } else {
                binding.etmobileno.setError(null);
                binding.etemailid.setError(null);
                binding.etflatno.setError(null);
                binding.etArea.setError(null);
                binding.etlandmark.setError(null);
                binding.etpincode.setError(null);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return isError;
    }

    public void setupStatesAndCities() {
        try {
            JSONArray json = new JSONArray(loadJSONFromAsset());


            for (int i = 0; i < json.length(); i++) {
                JSONObject e = json.getJSONObject(i);
                //cityList.add(e.getString("city"));
                stateList.add(e.getString("state").trim());

            }


            Set<String> set = new HashSet<>(stateList);
            stateList.clear();
            stateList.addAll(set);
            Collections.sort(stateList);
            ArrayAdapter adState = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, stateList);
            binding.spinnerState.setAdapter(adState);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addPreferedCities() {
        try {
            cityList.clear();
            JSONArray json = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < json.length(); i++) {
                JSONObject e = json.getJSONObject(i);
                if (e.getString("state").equals(address.getStrState())) {
                    cityList.add(e.getString("city"));
                }
            }
            Collections.sort(cityList);
            ArrayAdapter adCity = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, cityList);
            adCity.setDropDownViewResource(R.layout.spinner_item);
            binding.spinnerCity.setAdapter(adCity);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("StateCity.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        member = AddAddressFragmentArgs.fromBundle(getArguments()).getGetMember();
        address.setStrPersonSrno(AddAddressFragmentArgs.fromBundle(getArguments()).getGetMember().getExtPersonSrNo());
    }

}