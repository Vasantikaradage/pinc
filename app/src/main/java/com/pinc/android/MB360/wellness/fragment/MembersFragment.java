package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentMembersBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.City;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;
import com.pinc.android.MB360.wellness.homehealthcare.ui.AddAddressListener;
import com.pinc.android.MB360.wellness.homehealthcare.ui.MembersAdapter;
import com.google.android.material.appbar.MaterialToolbar;

public class MembersFragment extends Fragment implements AddAddressListener {

    FragmentMembersBinding binding;
    View view;
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    MaterialToolbar toolbar;

    //adapters
    MembersAdapter adapter;

    NavController navController;


    public MembersFragment() {
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
        binding = FragmentMembersBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        // Overview
        binding.btnOverview.setOnClickListener(view -> {

            NavDirections actions = MembersFragmentDirections.actionMembersFragmentToOverviewHealthcareFragment();
            navController.navigate(actions);
        });

        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

//        String city = homeHealthCareViewModel.getSelectedCity().getValue() != null ? getCityFromUser() : homeHealthCareViewModel.getSelectedCity().getValue().getCity();


        getMembers();

        if (homeHealthCareViewModel.getSelectedCity().equals("") || homeHealthCareViewModel.getSelectedCity() == null) {
            Toast.makeText(getContext(), "Unable to get location \n todo open location dialogue ", Toast.LENGTH_SHORT).show();

        }



        binding.title.setOnClickListener(view1 -> {
            NavDirections actions = MembersFragmentDirections.actionMembersFragmentToCitiesFragmentHHC();
            navController.navigate(actions);
//            navController.navigateUp();
        });


        homeHealthCareViewModel.getSelectedCity().observe(getViewLifecycleOwner(), city -> {

            binding.txtCityName.setVisibility(View.VISIBLE);
            if (city != null)
                binding.txtCityName.setText("" + city.getCity());
        });

        return view;
    }

//    private void setToolbarTitle() {
//       switch ( homeHealthCareViewModel.getService()){
//           //todo name change toolbar
//
//           case "TRAINED ATTENDANT":
//
//               break;
//       }
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        City city = MembersFragmentArgs.fromBundle(getArguments()).getGetCity();

       if(city != null){

           homeHealthCareViewModel.setCity(city);
       }else {


       }

    }

    private void getMembers() {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            String empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            String groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                String extGroupSrNo = employeeCheckResponse.getExtGroupSrNo();


                homeHealthCareViewModel.getMembers(empIdNo, groupCode, extGroupSrNo);

            });

        });

        homeHealthCareViewModel.getMembersData().observe(getViewLifecycleOwner(), membersResponse -> {

            //setup the recyclerview
            adapter = new MembersAdapter(getContext(), membersResponse.getFamilyMembers(), this);
            binding.membersCycle.setAdapter(adapter);
            adapter.notifyItemRangeChanged(0, membersResponse.getFamilyMembers().size());

        });


    }

    @Override
    public void getMember(FamilyMember familyMember) {

        //here we add address
        homeHealthCareViewModel.setSelectedPerson(familyMember);
        NavDirections actions = MembersFragmentDirections.actionMembersFragmentToAddAddressFragment(familyMember);
        navController.navigate(actions);

    }

    @Override
    public void selectPackage(FamilyMember familyMember) {
        // this function means the address is already
        // added and need to go on next page with
        // correct service name
        homeHealthCareViewModel.setSelectedPerson(familyMember);

        switch (homeHealthCareViewModel.getService()) {

            case "TRAINED ATTENDANT":
                NavDirections actions = MembersFragmentDirections.actionMembersFragmentToTrainedAttendedFragment(familyMember);
                navController.navigate(actions);
                break;
            case "LONG TERM NURSING":
                NavDirections actionsLT = MembersFragmentDirections.actionMembersFragmentToLongTermFragment(familyMember);
                navController.navigate(actionsLT);
                break;
            case "SHORT TERM NURSING":
                NavDirections actionsST = MembersFragmentDirections.actionMembersFragmentToShortTermFragment(familyMember);
                navController.navigate(actionsST);
                break;
            case "PHYSIOTHERAPY":
                NavDirections actionsP = MembersFragmentDirections.actionMembersFragmentToPhysiotherapyFragment(familyMember);
                navController.navigate(actionsP);
                break;
            case "POST NATAL CARE":
                NavDirections actionsPNC = MembersFragmentDirections.actionMembersFragmentToPostNatalCareFragment(familyMember);
                navController.navigate(actionsPNC);
                break;
            case "ELDER CARE":
                NavDirections actionsEC = MembersFragmentDirections.actionMembersFragmentToElderCareFragment(familyMember);
                navController.navigate(actionsEC);
                break;
            case "DOCTOR SERVICES":
                NavDirections actionsDS = MembersFragmentDirections.actionMembersFragmentToDoctorServicesFragment(familyMember);
                navController.navigate(actionsDS);
                break;
            case "DIABETES MANAGEMENT":
                NavDirections actionsDM = MembersFragmentDirections.actionMembersFragmentToDiabetesManagementFragment(familyMember);
                navController.navigate(actionsDM);
                break;
        }
    }

//    private void getAddress() {
//
//        final BottomSheetDialog dialog = new BottomSheetDialog(requireActivity());
//        dialog.setContentView(R.layout.dialog_address);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        dialog.setCanceledOnTouchOutside(false);
////                ImageView iv_close = dialog.findViewById(R.id.iv_close);
//        EditText etmobile = dialog.findViewById(R.id.etmobileno);
//        EditText etemailid = dialog.findViewById(R.id.etemailid);
//        EditText etflatno = dialog.findViewById(R.id.etflatno);
//        EditText etlocation = dialog.findViewById(R.id.etlocation);
//        EditText etlandmark = dialog.findViewById(R.id.etlandmark);
//        EditText etpincode = dialog.findViewById(R.id.etpincode);
//        CheckBox cbtermsofuse = dialog.findViewById(R.id.chkAgreeTerms);
//        TextView btnsaveupdateaddress = dialog.findViewById(R.id.btnsaveupdateaddress);
////                iv_close.setOnClickListener(v -> dialog.cancel());
//        btnsaveupdateaddress.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.greenlightbg2));
//
//        String html = " I agree to <a href=\"http://www.google.com\">Terms of Use</a>";
//        Spanned result;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
//        } else {
//            result = Html.fromHtml(html);
//        }
//        cbtermsofuse.setText(result);
//        cbtermsofuse.setMovementMethod(LinkMovementMethod.getInstance());
//
//        if (Build.VERSION.SDK_INT < 21)
//            CompoundButtonCompat.setButtonTintList(cbtermsofuse, ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.grey1)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
//        else
//            cbtermsofuse.setButtonTintList(ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.grey1)));//setButtonTintList is accessible directly on API>19
//
//        cbtermsofuse.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                if (Build.VERSION.SDK_INT < 21)
//                    CompoundButtonCompat.setButtonTintList(cbtermsofuse, ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.greencolorprimaryDark)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
//                else
//                    cbtermsofuse.setButtonTintList(ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.greencolorprimaryDark)));//setButtonTintList is accessible directly on API>19
//            } else {
//                if (Build.VERSION.SDK_INT < 21)
//                    CompoundButtonCompat.setButtonTintList(cbtermsofuse, ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.grey1)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
//                else
//                    cbtermsofuse.setButtonTintList(ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.grey1)));//setButtonTintList is accessible directly on API>19
//            }
//        });
//
//        btnsaveupdateaddress.setOnClickListener(v -> {
//            if (etmobile.getText().length() != 10) {
//                Toast.makeText(requireActivity(), "Please correct mobile no.", Toast.LENGTH_SHORT).show();
//            } else if (!cbtermsofuse.isChecked()) {
//                if (Build.VERSION.SDK_INT < 21)
//                    CompoundButtonCompat.setButtonTintList(cbtermsofuse, ColorStateList.valueOf(getResources().getColor(R.color.bg_row_background)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
//                else
//                    cbtermsofuse.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg_row_background)));//setButtonTintList is accessible directly on API>19
//            } else {
////                        verifymobile(""+personList.get(position).getPersonSRNo(),
////                                ""+etmobile.getText().toString().trim(), dialog);
////                        packageInterface.scheduleAppointment(packages, lstPerson);
//                dialog.cancel();
//            }
//        });
//
//        dialog.show();
//    }

}