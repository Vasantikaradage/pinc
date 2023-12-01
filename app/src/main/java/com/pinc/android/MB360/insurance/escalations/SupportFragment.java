package com.pinc.android.MB360.insurance.escalations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentPolicyFeaturesBinding;
import com.pinc.android.MB360.databinding.FragmentSupportBinding;
import com.pinc.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.pinc.android.MB360.insurance.escalations.repository.EscalationsViewModel;
import com.pinc.android.MB360.insurance.escalations.repository.responseclass.GroupEscalationInfo;
import com.pinc.android.MB360.insurance.escalations.repository.ui.EscalationAdapter;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGPAPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGTLPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.PolicySpinnerAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SupportFragment extends Fragment {


    FragmentSupportBinding binding;
    View view;
    NavController navController;

    //Escalation ViewModel
    EscalationsViewModel escalationsViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    EscalationAdapter adapter;
    SpinnerAdapter spinnerAdapter;

    String PRODUCT_CODE = "GMC";

    TabLayout.OnTabSelectedListener onTabSelectedListener;

    List<GroupPolicyData> policyData = new ArrayList<>();
    int selectedIndex;


    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSupportBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        escalationsViewModel = new ViewModelProvider(SupportFragment.this).get(EscalationsViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);


        //adding the tabs
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GHI"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GPA"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GTL"));

        onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getEscalationsWithTabs("GMC");
                        break;
                    case 1:
                        getEscalationsWithTabs("GPA");
                        break;
                    case 2:
                        getEscalationsWithTabs("GTL");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

        binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);


        escalationsViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

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


        binding.selectPolicyChip.setOnClickListener(v -> {
            PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
            policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getEscalations();

        binding.spinnerHolder.setOnClickListener(v -> {

            PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
            policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);

        });

        selectedPolicyViewModel.getPolicyData().observe(getViewLifecycleOwner(), policyData -> {

            this.policyData = policyData;
        });

        selectedPolicyViewModel.getSelectedIndex().observe(getViewLifecycleOwner(), index -> {
            this.selectedIndex = index;
        });

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            //change the selection chips ui
            selectChip(groupPolicyData);
            setTextWithFancyAnimation(binding.selectedPolicyText, "Filter: " + groupPolicyData.getPolicyNumber());

            binding.policySelectionText.setText(groupPolicyData.getPolicyNumber());
        });

    }

    private void getEscalationsWithTabs(String code) {
        try {
            String oegrpbasinfosrno = "";

            String employeeSrno = "";
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            assert loadSessionResponse != null;
            if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                    switch (groupProduct.getProductCode().toLowerCase()) {
                        case "gmc":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(0).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gpa":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(1).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gtl":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(2).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                    }

                }
            }
            String groupChildSrnNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();

            switch (code) {
                case "GMC":
                    List<GroupPolicyData> gmcPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(gmcPolicy);
                    selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gmcPolicy.get(0));

                    for (GroupPolicyData policy : gmcPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oegrpbasinfosrno = policy.getOeGrpBasInfSrNo();
                        }
                    }
                    //   escalationsViewModel.getEscalations(groupChildSrnNo, oegrpbasinfosrno);
                    break;
                case "GPA":
                    List<GroupPolicyData> gpaPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGPAPoliciesData(gpaPolicy);
                    selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gpaPolicy.get(0));

                    for (GroupPolicyData policy : gpaPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oegrpbasinfosrno = policy.getOeGrpBasInfSrNo();
                        }
                    }
                    //  escalationsViewModel.getEscalations(groupChildSrnNo, oegrpbasinfosrno);
                    break;

                case "GTL":
                    List<GroupPolicyData> gtlPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGTLPoliciesData(gtlPolicy);
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlPolicy.get(0));

                    for (GroupPolicyData policy : gtlPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oegrpbasinfosrno = policy.getOeGrpBasInfSrNo();
                        }
                    }

                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEscalations() {
        //to get the  policyFeatures data we need some parameters from load session values
        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), policy -> {

            String oeGrpBasInfSrNo = policy.getOeGrpBasInfSrNo();

            PRODUCT_CODE = policy.getProductCode();

            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

                try {


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


                    Log.d(LogTags.ESCALATION_ACTIVITY, "onCreateView: " + loadSessionResponse.toString());
                    if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                        for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                            switch (groupProduct.getProductCode().toLowerCase()) {
                                case "gmc":
                                    if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                        binding.tabs.viewPagerTabs.getTabAt(0).view.setEnabled(false);
                                        binding.tabs.viewPagerTabs.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    }
                                    break;
                                case "gpa":
                                    if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                        binding.tabs.viewPagerTabs.getTabAt(1).view.setEnabled(false);
                                        binding.tabs.viewPagerTabs.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    }
                                    break;
                                case "gtl":
                                    if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                        binding.tabs.viewPagerTabs.getTabAt(2).view.setEnabled(false);
                                        binding.tabs.viewPagerTabs.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    }
                                    break;
                            }

                        }
                    }

                    String groupChildSrvNo = "";

                    String employeeSrNo = "";


                    switch (PRODUCT_CODE) {
                        case "GMC":
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(onTabSelectedListener);
                            GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum;
                            groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);

                            binding.tabs.viewPagerTabs.getTabAt(0).select();

                            //queries for claims
                            employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();


                            List<GroupGMCPolicyEmployeeDatum> gmcPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData();

                            List<GroupPolicyData> gmcSpinnerList = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());

                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);
                            escalationsViewModel.getEscalations(groupChildSrvNo, oeGrpBasInfSrNo);
                            break;
                        case "GPA":
                            GroupGPAPolicyEmployeeDatum groupGPAPolicyEmployeeDatum;
                            groupGPAPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0);
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(onTabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(1).select();
                            //queries for claims
                            employeeSrNo = groupGPAPolicyEmployeeDatum.getEmployeeSrNo();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();


                            List<GroupPolicyData> gpaSpinnerList = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());


                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);
                            escalationsViewModel.getEscalations(groupChildSrvNo, oeGrpBasInfSrNo);

                            break;
                        case "GTL":

                            GroupGTLPolicyEmployeeDatum groupGTLPolicyEmployeeDatum;
                            groupGTLPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0);
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(onTabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(2).select();
                            //queries for claims
                            employeeSrNo = groupGTLPolicyEmployeeDatum.getEmployeeSrNo();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();


                            List<GroupGTLPolicyEmployeeDatum> gtlPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData();

                            List<GroupPolicyData> gtlSpinnerList = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());
                            int miniIndexgtl = 0;

                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);
                            escalationsViewModel.getEscalations(groupChildSrvNo, oeGrpBasInfSrNo);

                            break;
                        default:
                            Toast.makeText(getContext(), "Something Wrong Happened", Toast.LENGTH_SHORT).show();
                            //error
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        escalationsViewModel.getEscalationsData().observe(getViewLifecycleOwner(), escalationsResponse -> {
            if (escalationsResponse != null) {
                if (escalationsResponse.getGroupEscalationInfo().isEmpty()) {
                    binding.errorLayout.setVisibility(View.VISIBLE);
                    binding.escalationRecyclerView.setVisibility(View.GONE);
                } else {
                    binding.escalationRecyclerView.setVisibility(View.VISIBLE);
                    binding.errorLayout.setVisibility(View.GONE);
                    adapter = new EscalationAdapter(requireContext(), getEscalationList(escalationsResponse.getGroupEscalationInfo()));
                    binding.escalationRecyclerView.setAdapter(adapter);
                    adapter.notifyItemRangeChanged(0, escalationsResponse.getGroupEscalationInfo().size());


                }
            } else {
                binding.errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private List<GroupEscalationInfo> getEscalationList(List<GroupEscalationInfo> groupEscalationInfo) {

        List<GroupEscalationInfo> escalationList = new ArrayList<>();

        for (GroupEscalationInfo lstContac : groupEscalationInfo
        ) {
            if (lstContac.getDispEmail().equalsIgnoreCase("0") &&
                    lstContac.getDispMob().equalsIgnoreCase("0") &&
                    lstContac.getDispFax().equalsIgnoreCase("0") &&
                    lstContac.getDispAdd().equalsIgnoreCase("0")
            ) {
                //nothing to do
            } else {
                escalationList.add(lstContac);
            }

        }


        return escalationList;
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }


    /**
     * @link {{@link #sort(List)}}
     * to sort the list before setting up to the spinner.
     **/
    private List<GroupPolicyData> sort(List<GroupPolicyData> list) {

        list.sort(Comparator.comparing(GroupPolicyData::getOeGrpBasInfSrNo));

        return list;
    }

    private void getPolicyFeaturesSpinner(GroupPolicyData groupPolicyData) {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            String PRODUCT_CODE = groupPolicyData.getProductCode();
            String OE_GRP_BAS_INFO_SR_NO = groupPolicyData.getOeGrpBasInfSrNo();
            String GROUP_CHILD_SR_NO = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            String EMPLOYEE_SR_NO = "";

            escalationsViewModel.getEscalations(GROUP_CHILD_SR_NO, OE_GRP_BAS_INFO_SR_NO);


        });
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