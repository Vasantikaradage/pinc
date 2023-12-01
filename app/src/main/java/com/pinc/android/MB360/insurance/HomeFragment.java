package com.pinc.android.MB360.insurance;

import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentHomeBinding;
import com.pinc.android.MB360.insurance.adminsetting.ui.AdminSettingViewModel;
import com.pinc.android.MB360.insurance.coverages.repository.CoveragesViewModel;
import com.pinc.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.pinc.android.MB360.insurance.ecards.repository.EcardViewModel;
import com.pinc.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.pinc.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.pinc.android.MB360.insurance.profile.ui.ProfileFragmentListServiceDirections;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGPAPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGTLPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.pinc.android.MB360.insurance.queries.repository.QueryViewModel;
import com.pinc.android.MB360.utilities.BadgesDrawable;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.PolicySpinnerAdapter;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.WellnessDashBoardActivity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment implements DashboardItemClickListener {

    FragmentHomeBinding binding;
    NavController navController;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    HospitalNetworkViewModel hospitalNetworkViewModel;
    MyClaimsViewModel myClaimsViewModel;
    CoveragesViewModel coveragesViewModel;
    QueryViewModel queryViewModel;
    EcardViewModel ecardViewModel;
    List<GroupPolicyData> policyData;
    LayerDrawable icon;
    String PRODUCT_CODE = "GMC";
    String oeGrpBasInfoSrNo = "";
    PolicySpinnerAdapter adapter;
    int temp_position = 0;
    TabLayout.OnTabSelectedListener tabSelectedListener;
    boolean userSelect = false;
    boolean SELECTED_FIRST_POLICY = true;

    //this integer helps to hold the position of the policy whenever the dialogue re-opens.
    int selectedIndex;


    //new list of dashboard
    List<DashBoardModel> dashBoardItemList = new ArrayList<>();

    //adapter for dashboard item
    DashboardItemsAdapter dashboardAdapter;

    AdminSettingViewModel adminSettingViewModel;

    Boolean TO_SHOW_HOSPITAL = true;
    DashBoardModel providerNetworkModel = new DashBoardModel();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        providerNetworkModel.setDashBoardTextDescription("");
        providerNetworkModel.setDashBoardImage(ContextCompat.getDrawable(requireContext(), R.drawable.ic_provider_network));
        providerNetworkModel.setDashBoardHeader(getString(R.string.provider_network));


        if (dashBoardItemList.isEmpty()) {
            dashBoardItemList.add(new DashBoardModel(getString(R.string.my_coverages), ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_coverage), ""));
            dashBoardItemList.add(new DashBoardModel(getString(R.string.my_claims), ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_claims), ""));
            dashBoardItemList.add(new DashBoardModel(getString(R.string.my_queries), ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_query), ""));
            dashBoardItemList.add(new DashBoardModel("Claim\nProcedures", ContextCompat.getDrawable(requireContext(), R.drawable.ic_claim_procedures), ""));
            dashBoardItemList.add(new DashBoardModel("Intimate\nClaim", ContextCompat.getDrawable(requireContext(), R.drawable.ic_intimate_claim), ""));
            dashBoardItemList.add(new DashBoardModel("Policy\nFeatures", ContextCompat.getDrawable(requireContext(), R.drawable.ic_policy_features), ""));
            dashBoardItemList.add(new DashBoardModel("FAQs", ContextCompat.getDrawable(requireContext(), R.drawable.ic_faq), ""));
        }

        dashboardAdapter = new DashboardItemsAdapter(requireContext(), dashBoardItemList, this);
        binding.dashboardCycle.setAdapter(dashboardAdapter);
        RecyclerView.ItemAnimator animator = binding.dashboardCycle.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        binding.selectPolicyChip.setOnClickListener(v -> {
            PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
            policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);
        });

        int mutipleSpan = 6; //least common multiple
        int maxSpan = 3;//max span count

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), mutipleSpan);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int actualSize = 0;

                if (position > 3) {
                    actualSize = 3;
                } else {
                    actualSize = 2;
                }

                if (actualSize < maxSpan) {
                    return mutipleSpan / actualSize;
                }
                return mutipleSpan / maxSpan;

            }
        });
        binding.dashboardCycle.setLayoutManager(layoutManager);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //has menu options (title bar for policies)
        MenuProvider menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


                inflater.inflate(R.menu.home_fragment_menu, menu);

                MenuItem item = menu.findItem(R.id.policy_change);
                icon = (LayerDrawable) item.getIcon();
                //set count on badge
                selectedPolicyViewModel.totalPolicyCount().observe(getViewLifecycleOwner(), totalCount -> {
                    setBadgeCount(getContext(), icon, String.valueOf(totalCount));
                });


            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.policy_change) {
                    //policy number Change Dialogue (so that they can See for what details he is changing for!)
                    PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
                    policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);
                }

                return true;


            }
        };
        //  requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner());

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                exitWarning();
            }
        });

        /** here we load the load session data from the dashboard activity */
        //passing require activity because view-models is scoped in dashboard activity
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        //view-model for selected policy to access its details
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        hospitalNetworkViewModel = new ViewModelProvider(requireActivity()).get(HospitalNetworkViewModel.class);
        myClaimsViewModel = new ViewModelProvider(requireActivity()).get(MyClaimsViewModel.class);
        queryViewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);
        coveragesViewModel = new ViewModelProvider(requireActivity()).get(CoveragesViewModel.class);
        ecardViewModel = new ViewModelProvider(requireActivity()).get(EcardViewModel.class);
        adminSettingViewModel = new ViewModelProvider(requireActivity()).get(AdminSettingViewModel.class);

        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
            if (loadSessionResponse != null) {
                try {
                    String oeGrpBasInfoSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo();
                    String groupChildSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getGroupchildsrno();
                    adminSettingViewModel.getAdminSetting(groupChildSrNo, oeGrpBasInfoSrNo);

                    if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                        for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                            switch (groupProduct.getProductCode().toLowerCase()) {
                                case "gmc":
                                    if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                        binding.ghiChip.setVisibility(View.GONE);
                                    }
                                    break;
                                case "gpa":
                                    if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                        binding.gpaChip.setVisibility(View.GONE);
                                    }
                                    break;
                                case "gtl":
                                    if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                        binding.gtlChip.setVisibility(View.GONE);
                                    }
                                    break;
                            }

                        }
                    }

                } catch (Exception e) {
                    //we don't get the loadSession.
                    e.printStackTrace();

                }
            }

            GroupPolicyData data = selectedPolicyViewModel.getSelectedPolicy().getValue();

            if (data == null) {
                if (loadSessionResponse != null && SELECTED_FIRST_POLICY) {
                    SELECTED_FIRST_POLICY = true;
                    DisplaySpinnerData("GMC");
                    Log.d("HOME-FRAGMENT", "onCreateView: " + loadSessionResponse.toString());
                    //setting all the three policies
                    //sort all policy according to oegrpbasinfo.
                    selectedPolicyViewModel.setGroupGMCPoliciesData(sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData()));
                    //selectedPolicyViewModel.setGroupGPAPoliciesData(sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData()));
                    //selectedPolicyViewModel.setGroupGTLPoliciesData(sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData()));

                    //getting tall the policies that are applicable to check!
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectChip(null);
                    TO_SHOW_HOSPITAL = true;

                }
            } else {


            }


        });


        selectedPolicyViewModel.getSelectedIndex().observe(getViewLifecycleOwner(), index -> {
            selectedIndex = index;
        });

        //if in-case we lost our activity then we can show a loading ui until the data is back
        loadSessionViewModel.getLoadingState().observe(requireActivity(), loading -> {
            if (loading) {
                showLoading();
                binding.refreshLayout.setRefreshing(true);
            } else {
                hideLoading();
                binding.refreshLayout.setRefreshing(false);

            }
        });

        //refresh
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSessions();
            }
        });

        selectedPolicyViewModel.getPolicyData().observe(getViewLifecycleOwner(), groupPolicyData -> {
            policyData = groupPolicyData;


        });


        //to get the coverages and spinner data setup
        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            Log.d("HOME-FRAGMENT", "PolicySelected: " + groupPolicyData.toString());

            //change the selection chips ui
            selectChip(groupPolicyData);
            setTextWithFancyAnimation(binding.selectedPolicyText, "Filter: " + groupPolicyData.getPolicyNumber());


            try {
                if (groupPolicyData.getProductCode().equalsIgnoreCase("GMC")) {
                    //show the hospital card
                    TO_SHOW_HOSPITAL = true;
                    addProviderNetwork();
                } else {
                    TO_SHOW_HOSPITAL = false;
                    //remove the hospital card
                    removeProviderNetwork();
                }
            } catch (Exception e) {
                e.printStackTrace();
                TO_SHOW_HOSPITAL = true;
            }

        });


        getCoverage();


        binding.ghiChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().isEmpty()) {
                    setPolicyWithChips("gmc");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }

        });
        binding.gpaChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData().isEmpty()) {
                    setPolicyWithChips("gpa");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.gtlChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData().isEmpty()) {
                    setPolicyWithChips("gtl");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }
        });


        //on clicked Wellness
        binding.titleMenu.rlTabWellness.setOnClickListener(view1 -> {
            Intent wellnessIntent = new Intent(requireActivity(), WellnessDashBoardActivity.class);
            startActivity(wellnessIntent);
            requireActivity().finish();
        });


        hospitalNetworkViewModel.getHospitalsCountData().observe(getViewLifecycleOwner(), hospital -> {

            DashBoardModel itemToEdit = dashBoardItemList.get(1);
            if (hospital != null) {

                if (hospital.getHospitalCount() == 1) {
                    //  itemToEdit.setDashBoardTextDescription(String.format("%s Hospitals", UtilMethods.PriceFormat(String.valueOf(hospital.getHospitalsCount().getV_COUNT()))));
                    providerNetworkModel.setDashBoardTextDescription(String.format("%s Hospital", UtilMethods.PriceFormat(String.valueOf(hospital.getHospitalCount()))));

                    if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                        dashBoardItemList.set(1, providerNetworkModel);
                        dashboardAdapter.notifyItemChanged(1);
                    }
                } else {
                    providerNetworkModel.setDashBoardTextDescription(String.format("%s Hospitals", UtilMethods.PriceFormat(String.valueOf(hospital.getHospitalCount()))));

                    if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                        dashBoardItemList.set(1, providerNetworkModel);
                        dashboardAdapter.notifyItemChanged(1);
                    }
                }
            } else {
                providerNetworkModel.setDashBoardTextDescription("0 Hospitals");
                if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
                    dashBoardItemList.set(1, providerNetworkModel);
                    dashboardAdapter.notifyItemChanged(1);
                }
            }


        });

        myClaimsViewModel.getMyClaimsData().observe(getViewLifecycleOwner(), claims -> {
            DashBoardModel itemToEdit = dashBoardItemList.get(2);
            if (claims != null) {
                if (claims.getClaimsInformation() != null) {

                    itemToEdit.setDashBoardTextDescription(String.format("%s Claims ", UtilMethods.PriceFormat(String.valueOf(claims.getClaimsInformation().getClaims().size()))));

                } else {


                    itemToEdit.setDashBoardTextDescription("0 Claim");

                }
            } else {


                itemToEdit.setDashBoardTextDescription("0 Claim");
            }
            dashBoardItemList.set(2, itemToEdit);
            dashboardAdapter.notifyItemChanged(2);
        });

        //admin settings
        adminSettingViewModel.getAdminSettingData().observe(getViewLifecycleOwner(), adminSettingResponse -> {
            if (adminSettingResponse != null) {
                String serverDate = adminSettingResponse.getGroup_Admin_Basic_Settings().getServerDate();
                String endDate = adminSettingResponse.getGroup_Window_Period_Info().getOpenEnroll_WP_Information_data().getWINDOW_PERIOD_END_DATE();
                Calendar calendarEnd = Calendar.getInstance();
                Calendar calendarStart = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

                try {
                    calendarEnd.setTime(Objects.requireNonNull(sdf.parse(endDate + " 23:59:59")));
                    calendarStart.setTime(Objects.requireNonNull(sdf.parse(serverDate + " 00:00:00")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //end date in millis
                long millis = calendarEnd.getTimeInMillis();

                //current date in millis
                long now = calendarStart.getTimeInMillis();

                //difference of millis
                long milisInFuture = millis - now;

                if (milisInFuture > 0) {
                    long diff = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = (hours / 24) + 1;
                    Log.d("days", "" + days);
                    String daysCount = String.valueOf(days);

                    binding.dayCountLayout.setVisibility(View.VISIBLE);
                    binding.daycount.setText(daysCount);

                    binding.daysLeft.setVisibility(View.VISIBLE);
                    binding.dependantImage.setVisibility(View.GONE);
                    binding.bottomtxt.setText("Add Dependant");
                    binding.enrollmentStatus.setText("OPEN");
                    binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.add_white));


                    binding.cardEnrollmentWindow.setOnClickListener(v -> {
                        // no enrollment for pinc
                        adminSettingResponse.getGroup_Admin_Basic_Settings().setAPP_ENROLLMENT_TYPE("2");
                        if (adminSettingResponse.getGroup_Admin_Basic_Settings().getAPP_ENROLLMENT_TYPE().equalsIgnoreCase("1")) {
                           /* NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentHomeFragment();
                            navController.navigate(actions);*/
                        } else if (adminSettingResponse.getGroup_Admin_Basic_Settings().getAPP_ENROLLMENT_TYPE().equalsIgnoreCase("2")) {
                           /* NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView("https://portal.mybenefits360.com/");
                            navController.navigate(actions);*/
                        }

                    });

                } else {

                    binding.cardEnrollmentWindow.setOnClickListener(v -> {
                        // no enrollment for pinc
                        adminSettingResponse.getGroup_Admin_Basic_Settings().setAPP_ENROLLMENT_TYPE("2");
                        if (adminSettingResponse.getGroup_Admin_Basic_Settings().getAPP_ENROLLMENT_TYPE().equalsIgnoreCase("1")) {
                           /* NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentHomeFragment();
                            navController.navigate(actions);*/
                        } else if (adminSettingResponse.getGroup_Admin_Basic_Settings().getAPP_ENROLLMENT_TYPE().equalsIgnoreCase("2")) {
                           /* NavDirections actions = HomeFragmentDirections.actionHomeFragmentToEnrollmentWebView("https://portal.mybenefits360.com/");
                            navController.navigate(actions);*/
                        }

                    });
                    binding.dayCountLayout.setVisibility(View.GONE);
                    binding.daycount.setText("");

                    binding.daysLeft.setVisibility(View.GONE);
                    binding.dependantImage.setVisibility(View.VISIBLE);
                    binding.bottomtxt.setText("Download Summary");
                    binding.enrollmentStatus.setText("CLOSED");
                    binding.addDependantImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_download_summary));

                }

                binding.bottomtxt.setVisibility(View.GONE);
                binding.addDependantImage.setVisibility(View.GONE);
            }

        });

        queryViewModel.getQueriesData().observe(getViewLifecycleOwner(), queryResponse -> {
            if (queryResponse != null) {
                if (queryResponse.getTotalRecords() != null) {
                    if (queryResponse.getTotalRecords().getCount() == 1) {
                        //binding.queryCount.setText(String.format(" %s Query ", UtilMethods.PriceFormat(String.valueOf(queryResponse.getTotalRecords().getCount()))));
                      /*  binding.queryCountShimmer.getRoot().setVisibility(View.GONE);
                        binding.queryCount.setVisibility(View.VISIBLE);
                        setTextWithFancyAnimation(binding.queryCount, String.format(" %s Query ", UtilMethods.PriceFormat(String.valueOf(queryResponse.getTotalRecords().getCount()))));
                    */

                        DashBoardModel itemToEdit = new DashBoardModel();
                        for (DashBoardModel dash : dashBoardItemList
                        ) {
                            if (dash.getDashBoardHeader().equalsIgnoreCase(getString(R.string.my_queries))) {
                                itemToEdit = dashBoardItemList.get(dashBoardItemList.indexOf(dash));
                                itemToEdit.setDashBoardTextDescription(String.format("%s Query ", UtilMethods.PriceFormat(String.valueOf(queryResponse.getTotalRecords().getCount()))));
                                dashboardAdapter.notifyItemChanged(dashBoardItemList.indexOf(dash));
                            }
                        }


                    } else {
                        // binding.queryCount.setText(String.format(" %s Queries ", UtilMethods.PriceFormat(String.valueOf(queryResponse.getTotalRecords().getCount()))));
                      /*  binding.queryCountShimmer.getRoot().setVisibility(View.GONE);
                        binding.queryCount.setVisibility(View.VISIBLE);
                        setTextWithFancyAnimation(binding.queryCount, String.format(" %s Queries ", UtilMethods.PriceFormat(String.valueOf(queryResponse.getTotalRecords().getCount()))));
                   */

                        DashBoardModel itemToEdit = new DashBoardModel();
                        for (DashBoardModel dash : dashBoardItemList
                        ) {
                            if (dash.getDashBoardHeader().equalsIgnoreCase(getString(R.string.my_queries))) {
                                itemToEdit = dashBoardItemList.get(dashBoardItemList.indexOf(dash));
                                itemToEdit.setDashBoardTextDescription(String.format("%s Queries ", UtilMethods.PriceFormat(String.valueOf(queryResponse.getTotalRecords().getCount()))));
                                dashboardAdapter.notifyItemChanged(dashBoardItemList.indexOf(dash));
                            }
                        }
                    }
                } else {
                 /*   binding.queryCountShimmer.getRoot().setVisibility(View.GONE);
                    binding.queryCount.setVisibility(View.VISIBLE);
                    setTextWithFancyAnimation(binding.queryCount, "0 Query");*/

                    DashBoardModel itemToEdit = new DashBoardModel();
                    for (DashBoardModel dash : dashBoardItemList
                    ) {
                        if (dash.getDashBoardHeader().equalsIgnoreCase(getString(R.string.my_queries))) {
                            itemToEdit = dashBoardItemList.get(dashBoardItemList.indexOf(dash));
                            itemToEdit.setDashBoardTextDescription("0 Query");
                            dashboardAdapter.notifyItemChanged(dashBoardItemList.indexOf(dash));
                        }
                    }

                }
            } else {
                DashBoardModel itemToEdit = new DashBoardModel();
                for (DashBoardModel dash : dashBoardItemList
                ) {
                    if (dash.getDashBoardHeader().equalsIgnoreCase(getString(R.string.my_queries))) {
                        itemToEdit = dashBoardItemList.get(dashBoardItemList.indexOf(dash));
                        itemToEdit.setDashBoardTextDescription("0 Query");
                        dashboardAdapter.notifyItemChanged(dashBoardItemList.indexOf(dash));
                    }
                }
            }
        });


        /*itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };*/


    }

    private void addProviderNetwork() {
        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
            //we already have the provider network
        } else {
            dashBoardItemList.add(1, providerNetworkModel);
            dashboardAdapter.notifyItemInserted(1);
            dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());

        }
    }

    private void removeProviderNetwork() {
        if (dashBoardItemList.get(1).getDashBoardHeader().equalsIgnoreCase(getString(R.string.provider_network))) {
            //we have to remove
            dashBoardItemList.remove(providerNetworkModel);
            dashboardAdapter.notifyItemRemoved(1);
            dashboardAdapter.notifyItemRangeChanged(0, dashBoardItemList.size());


        } else {
            // nothing to remove
        }

    }

    private void DisplaySpinnerData(String product_code) {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            try {
                switch (product_code) {
                    case "GMC":
                        List<GroupPolicyData> gmcList = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGMCPoliciesData(gmcList);
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gmcList.get(0));
                        setUpPolicySpinner(gmcList);
                        break;
                    case "GPA":
                        List<GroupPolicyData> gpaList = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());
                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(gpaList);
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gpaList.get(0));
                        setUpPolicySpinner(gpaList);
                        break;

                    case "GTL":
                        List<GroupPolicyData> gtlList = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());
                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(gtlList);
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlList.get(0));
                        setUpPolicySpinner(gtlList);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    private void setUpPolicySpinner(List<GroupPolicyData> policyList) {
        //prepare the dialogue box as per the tab is selected
        policyData = policyList;

    }


    private List<GroupPolicyData> sort(List<GroupPolicyData> list) {

        list.sort(Comparator.comparing(GroupPolicyData::getOeGrpBasInfSrNo));

        return list;
    }

    private void exitWarning() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.smslayout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));


        DisplayMetrics dm = new DisplayMetrics();

        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dialog.getWindow().setLayout((int) (width * .6), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        dialog.getWindow().setAttributes(params);
        AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
        final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
        AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
        lblSMS.setText(getString(R.string.my_benifits));
        lblSMSHeader.setText("Are you sure you want to Exit?");
        lblSMS.setText("PINC Insurance");
        btnSubmit.setText("Exit");
        btnCancel.setText("Cancel");
        smsContact.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(v -> {
            dialog.dismiss();
            requireActivity().finish();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void selectChip(GroupPolicyData groupPolicyData) {
        if (groupPolicyData != null) {
            switch (groupPolicyData.getProductCode().toLowerCase()) {
                case "gmc":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));

                    break;
                case "gpa":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    break;
                case "gtl":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    break;
            }
        } else {
            //selecting gmc default

            binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
            binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
            binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

            //text color
            binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
            binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
        }
    }

    private void getCoverage() {

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            PRODUCT_CODE = groupPolicyData.getProductCode();
            oeGrpBasInfoSrNo = groupPolicyData.getOeGrpBasInfSrNo();

            //to get the  coverages data we need some parameters from load session values
            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                try {
                    Log.d("HOME-FRAGMENT", "onCreateView: " + loadSessionResponse.toString());

                    String groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                    String employeeSrNo = "";


                    switch (PRODUCT_CODE) {
                        case "GMC":
                            List<GroupGMCPolicyEmployeeDatum> gmcPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData();

                            employeeSrNo = gmcPolicy.get(0).getEmployeeSrNo();
                            if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                //todo somethings wrong with the server-response.
                            }

                            coveragesViewModel.getCoveragePolicyData(groupChildSrvNo, oeGrpBasInfoSrNo);
                            coveragesViewModel.getCoverageDetails(groupChildSrvNo, oeGrpBasInfoSrNo, PRODUCT_CODE, employeeSrNo);


                            break;
                        case "GPA":
                            List<GroupGPAPolicyEmployeeDatum> gpaPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData();

                            employeeSrNo = gpaPolicy.get(0).getEmployeeSrNo();
                            if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                //todo somethings wrong with the server-response.
                            }


                            coveragesViewModel.getCoveragePolicyData(groupChildSrvNo, oeGrpBasInfoSrNo);
                            coveragesViewModel.getCoverageDetails(groupChildSrvNo, oeGrpBasInfoSrNo, PRODUCT_CODE, employeeSrNo);

                            break;
                        case "GTL":
                            List<GroupGTLPolicyEmployeeDatum> gtlPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData();

                            employeeSrNo = gtlPolicy.get(0).getEmployeeSrNo();
                            if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                //todo somethings wrong with the server-response.
                            }


                            coveragesViewModel.getCoveragePolicyData(groupChildSrvNo, oeGrpBasInfoSrNo);
                            coveragesViewModel.getCoverageDetails(groupChildSrvNo, oeGrpBasInfoSrNo, PRODUCT_CODE, employeeSrNo);

                            break;
                    }
                } catch (Exception e) {
                    //error
                    FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                    Throwable throwable = new Throwable(e);
                    crashlytics.recordException(throwable);
                    Toast.makeText(requireContext(), "Something went Wrong \nSending report to developers..", Toast.LENGTH_SHORT).show();
                }
            });

        });


        coveragesViewModel.getCoveragesDetailsData().

                observe(getViewLifecycleOwner(), coverageDetailsResponse ->

                {

                    //binding.txtRelations.setText(coveragesViewModel.getRelationGroupData().getValue());
                  /*  binding.txtRelationShimmer.getRoot().setVisibility(View.GONE);
                    binding.txtRelations.setVisibility(View.VISIBLE);*/


                    DashBoardModel itemToEdit = dashBoardItemList.get(0);
                    coveragesViewModel.getRelationGroupData().observe(getViewLifecycleOwner(), relationGroup -> {
                        if (relationGroup.isEmpty() || relationGroup.isBlank()) {
                            /* setTextWithFancyAnimation(binding.txtRelations, "-");*/

                            itemToEdit.setDashBoardTextDescription("-");
                            dashBoardItemList.set(0, itemToEdit);
                            dashboardAdapter.notifyItemChanged(0);
                        } else {
                            if (relationGroup.equalsIgnoreCase("e")) {
                                /*  setTextWithFancyAnimation(binding.txtRelations, "Employee");*/
                                itemToEdit.setDashBoardTextDescription("Employee");
                                dashBoardItemList.set(0, itemToEdit);
                                dashboardAdapter.notifyItemChanged(0);
                            } else {
                                /*  setTextWithFancyAnimation(binding.txtRelations, relationGroup);*/
                                itemToEdit.setDashBoardTextDescription(relationGroup);
                                dashBoardItemList.set(0, itemToEdit);
                                dashboardAdapter.notifyItemChanged(0);
                            }

                        }
                    });


                });


    }


    //load session refresh
    private void loadSessions() {

        Log.d("HOME-FRAGMENT", "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                loadSessionViewModel.loadSessionWithEmail(getEmail());
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                loadSessionViewModel.loadSessionWithID(getLoginID());
                break;
        }


       /* loadSessionViewModel.getLoadSessionData().observe(this, loadSessionResponse -> {

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

                //todo change the parameters

                // String dataRequest = "<DataRequest>" + "<groupchildsrno>1224</groupchildsrno>" + "<oegrpbasinfsrno>1356</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";
                String dataRequest = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<oegrpbasinfsrno>" + oeGrpBasInfoSrNo + "</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";

                //TODO call networkProvider
                hospitalNetworkViewModel.getHospitals(dataRequest);
                hospitalNetworkViewModel.getHospitalsCount(groupChildSrNo, oeGrpBasInfoSrNo);


                //myclaims
                String dataRequestMyClaims = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<employeesrno>" + empSrNo + "</employeesrno>" + "</DataRequest>";
                myClaimsViewModel.getMyClaims(dataRequestMyClaims);


            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/

    }


    private String getPhoneNumber() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_PHONE_NUMBER, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getLoginID() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_LOGIN_ID, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getLoginType() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
        String login_type = sharedPreferences.getString(AUTH_LOGIN_TYPE, null);
        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getEmail() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_EMAIL, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgesDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse instanceof BadgesDrawable) {
            badge = (BadgesDrawable) reuse;
        } else {
            badge = new BadgesDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


    private void showLoading() {
        //binding.progressLayout.setVisibility(View.VISIBLE);

        binding.progressLayout.setVisibility(View.GONE);
        binding.contentLayout.setVisibility(View.GONE);
        binding.homeShimmer.getRoot().setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        //binding.progressLayout.setVisibility(View.GONE);
        binding.contentLayout.setVisibility(View.VISIBLE);
        binding.homeShimmer.getRoot().setVisibility(View.GONE);
    }


    private void setTextWithFancyAnimation(TextView codeView, String value) {
        Animation translateIn = new TranslateAnimation(0, 0, codeView.getHeight(), 0);
        translateIn.setInterpolator(new OvershootInterpolator());
        translateIn.setDuration(500);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(200);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(translateIn);
        animationSet.reset();
        animationSet.setStartTime(0);

        codeView.setText(String.valueOf(value));
        codeView.clearAnimation();
        codeView.startAnimation(animationSet);
    }


    @Override
    public void onDashboardItemClicked(String menuName) {
        NavDirections actions;
        switch (menuName.toLowerCase()) {
            case "my coverages":
                actions = HomeFragmentDirections.actionHomeFragmentToCoverages();
                navController.navigate(actions);
                break;
            case "my claims":
                actions = HomeFragmentDirections.actionHomeFragmentToMyClaimsFragment();
                navController.navigate(actions);
                break;
            case "intimate\nclaim":
                actions = HomeFragmentDirections.actionHomeFragmentToClaimsFragment();
                navController.navigate(actions);
                break;
            case "provider network":
                actions = HomeFragmentDirections.actionHomeFragmentToProviderNetwork();
                navController.navigate(actions);
                break;
            case "my queries":
                actions = HomeFragmentDirections.actionHomeFragmentToMyQueriesFragment();
                navController.navigate(actions);
                break;
            case "policy\nfeatures":
                actions = HomeFragmentDirections.actionHomeFragmentToPolicyFeaturesFragment();
                navController.navigate(actions);
                break;
            case "claim\nprocedures":
                actions = HomeFragmentDirections.actionHomeFragmentToClaimsProcedureFragment();
                navController.navigate(actions);
                break;
            case "faqs":
                actions = HomeFragmentDirections.actionHomeFragmentToFaqFragment();
                navController.navigate(actions);
                break;

        }


    }

    private void setPolicyWithChips(String code) {
        try {
            loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
                if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                    for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                        switch (groupProduct.getProductCode().toLowerCase()) {
                            case "gmc":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.ghiChip.setVisibility(View.GONE);

                                }
                                break;
                            case "gpa":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.gpaChip.setVisibility(View.GONE);
                                }
                                break;
                            case "gtl":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.gtlChip.setVisibility(View.GONE);
                                }
                                break;
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                switch (code.toUpperCase()) {
                    case "GMC":
                        List<GroupPolicyData> gmcPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());

                        selectedPolicyViewModel.setGroupGMCPoliciesData(gmcPolicy);
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gmcPolicy.get(0));

                        break;
                    case "GPA":
                        List<GroupPolicyData> gpaPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());
                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(gpaPolicy);
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gpaPolicy.get(0));


                        break;
                    case "GTL":
                        List<GroupPolicyData> gtlPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());

                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(gtlPolicy);
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlPolicy.get(0));


                        break;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}