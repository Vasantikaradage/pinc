package com.pinc.android.MB360.wellness;

/**
 * this activity is the dashboard activity which will hold the wellness view models
 * and all the navigation components across the app for wellness.
 * this file holds the main activity for wellness
 * <p>
 * ps: if we ever add some native functionality for the app we must check if user is logged in or no
 **/

import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;
import static com.pinc.android.MB360.utilities.AppLocalConstant.verifyLocationPermissions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ActivityWellnessDashBoardBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.City;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class WellnessDashBoardActivity extends AppCompatActivity {

    ActivityWellnessDashBoardBinding binding;
    View view;

    BottomNavigationView bottomNavigationView;
    NavController navController;

    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;
    PackagesViewModel packagesViewModel;
    MaterialToolbar toolbar;


    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWellnessDashBoardBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        toolbar = binding.toolbar.wellnessToolbar;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        verifyLocationPermissions(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String city = "";

            if (location != null) {

                Log.d("city", "getCity: " + location.toString());

                try {

                    List<Address> latlng = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    city = latlng.get(0).getLocality();
                    Log.d("city", "onSuccess: " + city);


                    String finalCity = city;
                    //we set the city for health checkup and home health care here
                    //if not available then we have to get the city from user (dialogue box)

                    packagesViewModel.setCitiesLiveDataHC(city);
                    homeHealthCareViewModel.getCitiesAvailable().observe(this, cities -> {
                        List<City> citiesAvailable = cities.getCities();

                        for (City c : citiesAvailable) {
                            if (c.getCity().toLowerCase().equals(finalCity.toLowerCase())) {
                                homeHealthCareViewModel.setCity(c);

                            }
                        }

                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("city", "ERROR: ", e);
                }
            } else {

                //TODO set city Manually ( select city Dialog)
            }


        });

        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(this).get(DashboardWellnessViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(this).get(HomeHealthCareViewModel.class);
        packagesViewModel = new ViewModelProvider(this).get(PackagesViewModel.class);


        loadSessions();

        //to set the city for diagnostic center in health checkup
        packagesViewModel.getCitiesLiveDataHC().observe(this, city -> {
            if (city != null) {
                binding.toolbarCity.tvcity.setText(city);
            } else {
                Log.e(LogTags.WELLNESS_DASHBOARD_ACTIVITY, "onDiagnostic: city not found");
            }

        });

        //todo check the user is logged in or no!
        //apparently we don't need it now.


        //setting  up the bottom navigation bar
        bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_wellness);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //to navigate
        /** to say bottom navigation that we are on home page or-else it will select the first option in menu in this case (Support) **/
        navController.navigate(R.id.wellnessHomeFragment);
        //we need to remove a page as we are navigating from something in this case (same page is getting stacked)
        navController.popBackStack();


        binding.fab.setOnClickListener(view -> {
            //cause our home page is the first page
            navController.navigateUp();
        });

        binding.toolbarCity.locationLL.setOnClickListener(view1 -> {
            navController.navigate(R.id.citiesFragment);
        });

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {

            if (navDestination.getId() == R.id.wellnessHomeFragment) {

                resetSelection();
                toolbar = binding.toolbar.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.logo.setVisibility(View.VISIBLE);
                binding.toolbarCity.getRoot().setVisibility(View.GONE);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbar.titleTV.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                //todo disable the clicking of the Ongoing and history
                //todo disable the clicking of the addMember bottom sheet
                //showBottomBar();

            } else if (navDestination.getId() == R.id.diagnosticFragment) {
                resetSelection();

                toolbar = binding.toolbar.wellnessToolbar;

                binding.toolbarCity.getRoot().setVisibility(View.VISIBLE);
                binding.toolbar.getRoot().setVisibility(View.GONE);
                toolbar = binding.toolbarCity.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.getRoot().setVisibility(View.GONE);
                binding.toolbar.titleTV.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);

            } else if (navDestination.getId() == R.id.membersFragment) {
                resetSelection();

                toolbar = binding.toolbar.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbarCity.getRoot().setVisibility(View.GONE);
                binding.toolbar.logo.setVisibility(View.GONE);
                binding.toolbar.titleTV.setVisibility(View.VISIBLE);
                binding.toolbar.titleTV.setText("" + homeHealthCareViewModel.getService());
                getSupportActionBar().setDisplayShowTitleEnabled(false);

            }else {
                resetSelection();

                toolbar = binding.toolbar.wellnessToolbar;
                setSupportActionBar(toolbar);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbarCity.getRoot().setVisibility(View.GONE);
                binding.toolbar.logo.setVisibility(View.GONE);
                binding.toolbar.titleTV.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);

            }
        });

        binding.toolbar.logo.setVisibility(View.VISIBLE);
        NavigationUI.setupActionBarWithNavController(this, navController);

    }


    private void showBottomBar() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        binding.bottomAppBar.setVisibility(View.VISIBLE);
        binding.fab.setVisibility(View.VISIBLE);

    }

    private void hideBottomBar() {
        bottomNavigationView.setVisibility(View.GONE);
        binding.bottomAppBar.setVisibility(View.GONE);
        binding.fab.setVisibility(View.GONE);
    }

    private void loadSessions() {

        Log.d(LogTags.LOGIN_ACTIVITY, "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    Log.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());

                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                }
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    Log.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithEmail(getEmail());

                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                }
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    Log.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithID(getLoginID());

                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    Log.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithID(getLoginID());
                }

                break;
        }

        loadSessionViewModel.getLoadSessionData().observe(this, loadSessionResponse -> {
            String EmpIdNo = "";
            String GroupCode = "";
            EmpIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            GroupCode = loadSessionResponse.getGroupInfoData().getGroupcode();
            dashboardWellnessViewModel.getEmployeeWellnessDetails(EmpIdNo, GroupCode);
        });

    }

    private void resetSelection() {
        bottomNavigationView.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0, size = bottomNavigationView.getMenu().size(); i < size; i++) {
            bottomNavigationView.getMenu().getItem(i).setChecked(false);
            bottomNavigationView.getMenu().getItem(i).setVisible(true);
        }
        bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
    }

    private String getLoginType() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String login_type = sharedPreferences.getString(AUTH_LOGIN_TYPE, null);
        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getPhoneNumber() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_PHONE_NUMBER, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getEmail() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_EMAIL, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getLoginID() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_LOGIN_ID, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    public void PaymentNow(String youpay) {

//        final Activity activity = this;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_P85Qujlx0rs3IX");
        checkout.setImage(R.mipmap.ic_launcher_round);

//        double finalamount = Float.parseFloat(amount)*100;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "MyBenefits");
            options.put("description", "Payment");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://wellness.mybenefits360.com/mybenefits/assets/img/logo-master-small.png");
            options.put("currency", "INR");
            options.put("amount", "" + youpay);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");

            options.put("prefill", preFill);


            checkout.open(this, options);

        } catch (Exception e) {
            Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "Error in starting Razorpay Checkout : " + e);
            e.printStackTrace();
        }

    }
}