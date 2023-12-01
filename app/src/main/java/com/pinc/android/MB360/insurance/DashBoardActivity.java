package com.pinc.android.MB360.insurance;

import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;
import static com.pinc.android.MB360.utilities.AppLocalConstant.USER_AGREEMENT;
import static com.pinc.android.MB360.utilities.AppLocalConstant.askDefaultPermissions;
import static com.pinc.android.MB360.utilities.StatusBarHelper.setWindowFlag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ActivityDashBoardActivityBinding;
import com.pinc.android.MB360.databinding.DeclarationPopupLayoutBinding;
import com.pinc.android.MB360.insurance.adminsetting.ui.AdminSettingViewModel;
import com.pinc.android.MB360.insurance.coverages.repository.CoveragesViewModel;
import com.pinc.android.MB360.insurance.ecards.repository.EcardViewModel;
import com.pinc.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.pinc.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.pinc.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.pinc.android.MB360.insurance.queries.repository.QueryViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.pinc.android.MB360.networkmanager.NetworkStateManager;
import com.pinc.android.MB360.onboarding.SplashScreenActivity;
import com.pinc.android.MB360.onboarding.authentication.LoginActivity;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.FileDownloader;
import com.pinc.android.MB360.utilities.LogTags;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.utilities.error.ErrorActivity;
import com.pinc.android.MB360.utilities.webcustomtab.CustomTabActivityHelper;
import com.pinc.android.MB360.utilities.webcustomtab.WebviewFallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DashBoardActivity extends AppCompatActivity {

    /**
     * we are using view-binding from this activity
     **/
    ActivityDashBoardActivityBinding binding;
    View view;
    NavController navController;
    BottomNavigationView bottomNavigationView;

    Boolean E_CARD_TOAST = false;

    private String error;
    private AlertDialog.Builder mBuilder;

    /**
     * load-session view-model
     **/


    LoadSessionViewModel loadSessionViewModel;
    HospitalNetworkViewModel hospitalNetworkViewModel;
    MyClaimsViewModel myClaimsViewModel;
    QueryViewModel queryViewModel;
    CoveragesViewModel coveragesViewModel;
    EnrollmentViewModel enrollmentViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    EcardViewModel ecardViewModel;
    AdminSettingViewModel adminSettingViewModel;

    private CustomTabActivityHelper mCustomTabActivityHelper;
    private static final int INITIAL_HEIGHT_DEFAULT_PX = 1200;
    private static final int CORNER_RADIUS_MAX_DP = 8;
    private static final int CORNER_RADIUS_DEFAULT_DP = CORNER_RADIUS_MAX_DP;
    private static final int BACKGROUND_INTERACT_OFF_VALUE = 2;
    MaterialToolbar toolbar;


    private final Observer<Boolean> activeNetworkStateObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isConnected) {
            //here we get to that if the device is connected to internet or no
            if (isConnected) {
                binding.internetStatus.setVisibility(View.GONE);
            } else {
                binding.internetStatus.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardActivityBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        toolbar = binding.toolbar.insuranceToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if (!userAgreementPopup()) {
            showDeclarationDialog();
        } else {
            //to ask the permissions at first
            askDefaultPermissions(this);
        }


        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        hospitalNetworkViewModel = new ViewModelProvider(this).get(HospitalNetworkViewModel.class);
        myClaimsViewModel = new ViewModelProvider(this).get(MyClaimsViewModel.class);
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        coveragesViewModel = new ViewModelProvider(this).get(CoveragesViewModel.class);
        enrollmentViewModel = new ViewModelProvider(this).get(EnrollmentViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(this).get(SelectedPolicyViewModel.class);
        ecardViewModel = new ViewModelProvider(this).get(EcardViewModel.class);
        mCustomTabActivityHelper = new CustomTabActivityHelper();

        adminSettingViewModel = new ViewModelProvider(this).get(AdminSettingViewModel.class);

        NetworkStateManager.getInstance().getNetworkConnectivityStatus()
                .observe(this, activeNetworkStateObserver);


        //this function is responsible for the api call for load session
        //Current issue (api gets called 3 times when a user comes from otp activity)
        loadSessions();


        //this function is to get the enrollment data and instructions for now
        //getenrollmentInstructions();

        bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        //to navigate
        /** to say bottom navigation that we are on home page or-else it will select the first option in menu in this case (Support) **/
        navController.navigate(R.id.homeFragment);
        //we need to remove a page as we are navigating from something in this case (same page is getting stacked)
        navController.popBackStack();


        binding.fab.setOnClickListener(view -> {
            //cause our home page is the first page
            while (navController.getBackQueue().size() > 2) {
                navController.navigateUp();
            }

        });


        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.homeFragment) {

                resetSelection();
                // binding.toolbar.btnBack.setVisibility(View.GONE);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                binding.toolbar.logo.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                showBottomBar();


            } else if (navDestination.getId() == R.id.enrollmentHomeFragment || navDestination.getId() == R.id.enrollmentSummaryFragment) {
                //we don't need the toolbar in enrollment home page
                binding.toolbar.getRoot().setVisibility(View.GONE);
                //hide bottomBar
                hideBottomBar();

            } else if (navDestination.getId() == R.id.queryDetailsFragment || navDestination.getId() == R.id.myClaimsdetails || navDestination.getId() == R.id.newQueryFragment) {
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbar.logo.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                hideBottomBar();
            } else {
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                binding.toolbar.logo.setVisibility(View.GONE);
                showBottomBar();
            }

            if (navDestination.getId() == R.id.ecardFragment) {
                navController.navigateUp();

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.smslayout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nhborder));

                AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
                final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
                AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
                AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
                LinearLayout llTitle = dialog.findViewById(R.id.llTitle);
                AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
                llTitle.setVisibility(View.VISIBLE);
                lblSMS.setText("PINC Insurance");
                lblSMSHeader.setText("Do you want to download the E-card?");
                btnSubmit.setText("Yes");
                btnCancel.setText("No");
                smsContact.setVisibility(View.GONE);
                btnSubmit.setOnClickListener(v1 -> {
                    E_CARD_TOAST = false;
                    getEcard(dialog);
                });
                btnCancel.setOnClickListener(v -> {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    dialog.dismiss();

                });

                dialog.show();


            }


            if (navDestination.getId() == R.id.claimsFragment || navDestination.getId() == R.id.providerNetwork) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            } else {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }


            for (int i = 0; i < navController.getBackQueue().size(); i++) {
                if (navController.getBackQueue().get(i).getDestination().getLabel() != null && navController.getBackQueue().get(i).getDestination().getLabel().equals("Profile")) {
                    bottomNavigationView.getMenu().getItem(4).setChecked(true);

                }
            }

        });


        NavigationUI.setupActionBarWithNavController(this, navController);


        loadSessionViewModel.getLoadingState().observe(this, loading -> {
            if (loading) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void showDeclarationDialog() {
        final Dialog dialog = new Dialog(this);

        DeclarationPopupLayoutBinding binding = DeclarationPopupLayoutBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        Rect displayRectangle = new Rect();

        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.9f), dialog.getWindow().getAttributes().height);
        dialog.setCancelable(false);
        binding.term.setOnClickListener(v -> {
            String url = "https://cdn.pincinsurance.com/document/termsofuse.pdf";
           /* Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);*/

            new DownloadFile(this, this)
                    .downloadFilePDF("termsofuse.pdf", url);

            //startActivity(i);


        });
        binding.privacy.setOnClickListener(v -> {
            String url = "https://www.pincinsurance.com/privacy-document";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);
            // startActivity(i);
        });
        binding.disclaimer.setOnClickListener(v -> {
            String url = "https://cdn.pincinsurance.com/document/disclaimer.pdf";
          /*  Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);
            //startActivity(i);*/

            new DownloadFile(this, this)
                    .downloadFilePDF("disclaimer.pdf", url);
        });

        binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.background_effect_continue));
        binding.checkBox.setOnClickListener(v -> {
            if (binding.checkBox.isChecked()) {
                binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.continue_button_clicked));
            } else {
                binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.background_effect_continue));
            }
        });

        binding.cancel.setOnClickListener(v -> {
            UtilMethods.logout(this);

        });

        binding.buttonContinue.setOnClickListener(v -> {
            if (binding.checkBox.isChecked()) {
                SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                // Store false value to say that user already saw the walk-through to SharedPreference
                //   preferences.setEncryptedBoolString(USER_AGREEMENT, false);
                editor.putBoolean(USER_AGREEMENT, true);
                editor.apply();
                dialog.dismiss();
                //to ask the permissions at first
                askDefaultPermissions(this);

            } else {
                Toast.makeText(this, "Please select the checkbox", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private boolean userAgreementPopup() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(USER_AGREEMENT, false);

    }

    private void getEcard(Dialog dialog) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        dialog.dismiss();
        GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        try {
            String employee_sr = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            String group_code = loadSessionResponse.getGroupInfoData().getGroupcode();

            /* String dataRequest = "<DataRequest>" +
                    "<tpacode>MEDI</tpacode>" +
                    "<employeeno>NAYASA06</employeeno>" +
                    "<policynumber>GHI_P_A_1_NAYASA_110821</policynumber>" +
                    "<policycommencementdt>11~08~2021</policycommencementdt>" +
                    "<policyvaliduptodt>11~08~2022</policyvaliduptodt>" +
                    "<groupcode>NAYASA1</groupcode>" +
                    "</DataRequest>";*/
          /*  String dataRequest = "<DataRequest>" +
                    "<tpacode>HITS</tpacode>" +
                    "<employeeno>MCXL01139</employeeno>" +
                    "<policynumber>33180034210400000010</policynumber>" +
                    "<policycommencementdt>01~03~2022</policycommencementdt>" +
                    "<policyvaliduptodt>28~02~2023</policyvaliduptodt>" +
                    "<groupcode>MCX1</groupcode>" +
                    "</DataRequest>";*/

            String dataRequest = "<DataRequest>" +
                    "<tpacode>" + groupPolicyData.getTpaCode() + "</tpacode>" +
                    "<employeeno>" + employee_sr + "</employeeno>" +
                    "<policynumber>" + groupPolicyData.getPolicyNumber() + "</policynumber>" +
                    "<policycommencementdt>" + groupPolicyData.getPolicyCommencementDate() + "</policycommencementdt>" +
                    "<policyvaliduptodt>" + groupPolicyData.getPolicyCommencementDate() + "</policyvaliduptodt>" +
                    "<groupcode>" + group_code + "</groupcode>" +
                    "</DataRequest>";


            String tpa_code = groupPolicyData.getTpaCode();
            try {
                Map<String, String> ecardOptions = new HashMap<>();

                //  for testing
                   /* ecardOptions.put("tpacode", "DINS-I");
                    ecardOptions.put("employeeno", "100037");
                    ecardOptions.put("policynumber", "D096384337");
                    ecardOptions.put("policycommencementdt", "29/03/2023");
                    ecardOptions.put("policyvaliduptodt", "29/02/2024");
                    ecardOptions.put("groupcode", "GSIPL");*/

                ecardOptions.put("tpacode", groupPolicyData.getTpaCode());
                ecardOptions.put("employeeno", employee_sr);
                ecardOptions.put("policynumber", groupPolicyData.getPolicyNumber());
                ecardOptions.put("policycommencementdt", groupPolicyData.getPolicyCommencementDate());
                ecardOptions.put("policyvaliduptodt", groupPolicyData.getPolicyValidUpto());
                ecardOptions.put("groupcode", group_code);

                ecardViewModel.getEcardJson(ecardOptions).observe(this, ecardResponse -> {
                    if (ecardResponse != null) {

                        if (ecardResponse.getMessage().getECard().startsWith("/mybenefits")) {
                            getEcardDownload();
                        } else {
                            if (ecardResponse.getMessage().getECard().contains("E-card under process")) {
                                runUIThread("E-card under process.");
                            } else if (ecardResponse.getMessage().getECard().contains("No records Found")) {

                                runUIThread("No records Found.");

                            } else if (ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {
                                runUIThread("E-card not available.");

                            } else {

                                if (ecardResponse.getMessage().getECard() == null ||
                                        ecardResponse.getMessage().getECard().equalsIgnoreCase("NA") ||
                                        ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {

                                    runUIThread("E-card not available.");
                                } else if (ecardResponse.getMessage().getECard().contains("http")) {
                                    if (
                                            tpa_code.equalsIgnoreCase("PHS") ||
                                                    tpa_code.equalsIgnoreCase("ERICT") ||
                                                    tpa_code.equalsIgnoreCase("VMCT") ||
                                                    tpa_code.equalsIgnoreCase("MDH") ||
                                                    tpa_code.equalsIgnoreCase("MEDI") ||
                                                    tpa_code.equalsIgnoreCase("VHIPL") ||
                                                    tpa_code.equalsIgnoreCase("DINS-I")
                                    ) {


                                        File file = new File(getFilesDir(), System.currentTimeMillis() + ".pdf");
                                        if (Build.VERSION.SDK_INT > 32) {
                                            Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                                            try {
                                                StrictMode.ThreadPolicy gfgPolicy =
                                                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                StrictMode.setThreadPolicy(gfgPolicy);
                                                FileDownloaderAll.downloadFileWithoutPermission(ecardResponse.getMessage().getECard().replace("\"", ""), file, this, this);
                                            } catch (ActivityNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            StrictMode.ThreadPolicy gfgPolicy =
                                                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(gfgPolicy);
                                            try {
                                                FileDownloaderAll.downloadFile(ecardResponse.getMessage().getECard().replace("\"", ""), file, this, this);
                                            } catch (ActivityNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } else {
                                        openCustomTab(ecardResponse.getMessage().getECard().replace("\"", ""));
                                    }
                                }


                            }
                        }
                    } else {

                        runUIThread("E-card not available.");

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (groupPolicyData.getTpaCode().equalsIgnoreCase("DINS-I")) {

            } else {
               /* ecardViewModel.getEcard(dataRequest).observe(this, ecardResponse -> {
                    if (ecardResponse != null) {
                        Log.d(LogTags.E_CARD_ACTIVITY, ecardResponse.toString());
                        if (ecardResponse.getStatus().equals(AppServerConstants.SUCCESS)) {

                            if (ecardResponse.getEcardInformation().contains("E-card under process")) {
                                dialog.dismiss();
                                if (!E_CARD_TOAST) {
                                    Toast.makeText(this, "E-card under process", Toast.LENGTH_LONG).show();
                                    E_CARD_TOAST = true;
                                }

                            } else {
                                String url = "";

                                if (ecardResponse.getEcardInformation().contains("/mybenefits")) {
                                    //if we get this locally from our system (my benefits).
                                    url = BuildConfig.DOWNLAOD_BASE_URL + ecardResponse.getEcardInformation();
                                }
                                url = url + ecardResponse.getEcardInformation();

                                openCustomTab(ecardResponse.getEcardInformation());
                        *//*

                        Intent opendata = new Intent();
                        opendata.setAction(Intent.ACTION_VIEW);
                        opendata.setData(Uri.parse(url));

                        try {
                            startActivity(opendata);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (e instanceof ActivityNotFoundException) {
                                Toast.makeText(this, "No Application available to view this file", Toast.LENGTH_SHORT).show();
                                E_CARD_TOAST = true;
                            } else {
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                E_CARD_TOAST = true;

                            }
                        } *//*

                            }

                        } else {
                            if (!E_CARD_TOAST) {
                                Toast.makeText(this, "E-card not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (!E_CARD_TOAST) {
                            Toast.makeText(this, "E-card not available", Toast.LENGTH_SHORT).show();
                        }
                    }

                });*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ecardViewModel.getLoading().observe(this, loading -> {
            if (loading) {
                binding.progressLayoutEcard.setVisibility(View.VISIBLE);
            } else {
                binding.progressLayoutEcard.setVisibility(View.GONE);
            }
        });


    }

    private void openCustomTab(String url) {


        // Uses the established session to build a PCCT intent.
        CustomTabsSession session = mCustomTabActivityHelper.getSession();
        CustomTabsIntent.Builder intentBuilder =
                new CustomTabsIntent.Builder(session)
                        .setToolbarColor(ContextCompat.getColor(this, R.color.gradient_start))
                        .setUrlBarHidingEnabled(true)
                        .setShowTitle(true)
                        .setStartAnimations(this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);

        int resizeBehavior = false
                ? CustomTabsIntent.ACTIVITY_HEIGHT_FIXED
                : CustomTabsIntent.ACTIVITY_HEIGHT_DEFAULT;

        intentBuilder.setInitialActivityHeightPx(INITIAL_HEIGHT_DEFAULT_PX, resizeBehavior);
        int toolbarCornerRadiusDp = CORNER_RADIUS_DEFAULT_DP;
        intentBuilder.setToolbarCornerRadiusDp(toolbarCornerRadiusDp);

        CustomTabsIntent customTabsIntent = intentBuilder.build();


        customTabsIntent.intent.putExtra(
                "androidx.browser.customtabs.extra.INITIAL_ACTIVITY_HEIGHT_IN_PIXEL",
                INITIAL_HEIGHT_DEFAULT_PX);
        int toolbarCornerRadiusPx =
                Math.round(toolbarCornerRadiusDp * getResources().getDisplayMetrics().density);
        customTabsIntent.intent.putExtra(
                "androidx.browser.customtabs.extra.TOOLBAR_CORNER_RADIUS_IN_PIXEL",
                toolbarCornerRadiusPx);
        if (resizeBehavior != CustomTabsIntent.ACTIVITY_HEIGHT_DEFAULT) {
            customTabsIntent.intent.putExtra(
                    CustomTabsIntent.EXTRA_ACTIVITY_RESIZE_BEHAVIOR, resizeBehavior);
        }

        customTabsIntent.intent.putExtra(
                "androix.browser.customtabs.extra.ENABLE_BACKGROUND_INTERACTION",
                BACKGROUND_INTERACT_OFF_VALUE);


        CustomTabActivityHelper.openCustomTab(
                this, customTabsIntent, Uri.parse(url), new WebviewFallback());
    }

    private void hideBottomBar() {
        binding.bottomNavHolder.setVisibility(View.GONE);
    }


    private void showBottomBar() {
        binding.bottomNavHolder.setVisibility(View.VISIBLE);
    }


    private void getenrollmentInstructions() {
        enrollmentViewModel.getInstructions();
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
                    loadSessionViewModel.loadSessionWithEmail(getEmail());
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

            try {
                String empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                queryViewModel.getQueries(empSrNo);
                String groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                String oeGrpBasInfoSrNo = "";

                for (GroupPolicyData policy : loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData()
                ) {
                    if (policy.getPolicyType().equalsIgnoreCase("base")) {
                        oeGrpBasInfoSrNo = policy.getOeGrpBasInfSrNo();
                    }
                }


                // String dataRequest = "<DataRequest>" + "<groupchildsrno>1224</groupchildsrno>" + "<oegrpbasinfsrno>1356</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";
                String dataRequest = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<oegrpbasinfsrno>" + oeGrpBasInfoSrNo + "</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";

               /* // call networkProvider
                hospitalNetworkViewModel.getHospitals(dataRequest, oeGrpBasInfoSrNo);
                // hospitalNetworkViewModel.getHospitalsCount("1224", "1356");
                hospitalNetworkViewModel.getHospitalsCount(groupChildSrNo, oeGrpBasInfoSrNo);*/
                // hospitalNetworkViewModel.getHospitalsCount("1224", "1356");


                hospitalNetworkViewModel.getHospitals(groupChildSrNo, oeGrpBasInfoSrNo, "ALL");
                hospitalNetworkViewModel.getHospitalsCount(groupChildSrNo, oeGrpBasInfoSrNo, "ALL");


                //Admin Setting
                adminSettingViewModel.getAdminSetting(groupChildSrNo, oeGrpBasInfoSrNo);


                //myclaims
                String dataRequestMyClaims = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<employeesrno>" + empSrNo + "</employeesrno>" + "</DataRequest>";

                myClaimsViewModel.getMyClaims(dataRequestMyClaims);


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadSessionViewModel.getReloginState().observe(this, relogin -> {
            if (relogin) {
                //Toast.makeText(this, "Something went wrong, Please Login again.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, ErrorActivity.class);
                finishAffinity();
                startActivity(intent);

            } else {
                //everything is working
            }
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

    private String getPhoneNumber() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_PHONE_NUMBER, null);
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

    private String getLoginType() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String login_type = sharedPreferences.getString(AUTH_LOGIN_TYPE, null);
        Log.d("DEBUG", "getLoginType: " + login_type);
        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getEmail() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(AUTH_EMAIL, null);
        Log.d("DEBUG", "getLoginType: " + email);
        if (email != null) {
            return email;
        } else {
            return null;
        }
    }

    //to open as bottom sheet

  /*  @Override
    protected void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }*/

    static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;


        public DownloadFile(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;

        }

        public void downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            executors.execute(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                if (Build.VERSION.SDK_INT > 32) {

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {

                        FileDownloader.downloadFileWithoutPermission(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();

                    }
                } else {
                    try {

                        FileDownloader.downloadFile(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();

                    }
                }
            });
        }
    }

    private void getEcardDownload() {

    }

    private void runUIThread(String errorInfo) {
        error = errorInfo;
        runOnUiThread(() -> {
            mBuilder = new AlertDialog.Builder(this);

            mBuilder.setMessage(errorInfo);
            mBuilder.setCancelable(true);
            mBuilder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        });
    }
}

