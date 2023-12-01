package com.pinc.android.MB360;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinc.android.MB360.databinding.FragmentDoctorConsultantBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.doctorconsultant.repository.DCViewModel;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DoctorPackages;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.UserAgreementRequest;
import com.pinc.android.MB360.wellness.doctorconsultant.ui.DoctorPackagesAdapter;
import com.pinc.android.MB360.wellness.doctorconsultant.ui.OnPackageBuyListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DoctorConsultantFragment extends Fragment implements OnPackageBuyListener {

    FragmentDoctorConsultantBinding binding;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    DCViewModel dcViewModel;
    DoctorPackagesAdapter adapter;
    String empSrNo = "";
    UserAgreementRequest userAgreementRequest = new UserAgreementRequest();
    String extPersonSrNo = "";
    String extEmployeeSrNo = "";
    DashboardWellnessViewModel dashboardWellnessViewModel;
    Boolean BOTTOM_SHEET_SHOW = false;


    public DoctorConsultantFragment() {
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

        binding = FragmentDoctorConsultantBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dcViewModel = new ViewModelProvider(requireActivity()).get(DCViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);


        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {
                userAgreementRequest.setGroupChildSrNo(employeeCheckResponse.getExtGroupSrNo());
            });
            dcViewModel.getPackages(empSrNo, "1")
                    .observe(getViewLifecycleOwner(), dcPacks -> {

                        extPersonSrNo = dcPacks.getData().get(0).getExtPerSrNo();
                        extEmployeeSrNo = dcPacks.getData().get(0).getExtEmployeeSrNo();
                        userAgreementRequest.setExtEmpSrNo(dcPacks.getData().get(0).getExtEmployeeSrNo());

                        userAgreementRequest.setVendorSrNo("1");
                        userAgreementRequest.setGroupCode(loadSessionResponse.getGroupInfoData().getGroupcode());

                        adapter = new DoctorPackagesAdapter(dcPacks.getData(), requireContext(), this);
                        binding.dcRecyclerView.setAdapter(adapter);
                    });
        });

        dcViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void callPackageAgreement(Context context) {


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.buy_now_package_bottom_view);

        TextView buyNowUserAgeConfirmBtn = bottomSheetDialog.findViewById(R.id.buyNowUserAgeConfirmBtn);
        CheckBox checkBoxBtn = bottomSheetDialog.findViewById(R.id.checkBoxBtn);
        ImageView closeImg = bottomSheetDialog.findViewById(R.id.closeImg);
        AppCompatTextView buyNowTermsCondition = bottomSheetDialog.findViewById(R.id.buyNowTermsCondition);

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        buyNowTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bottomSheetDialog.dismiss();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mybenefits360.com/wellness/termsOfUse.aspx"));
                v.getContext().startActivity(browserIntent);
            }
        });

        buyNowUserAgeConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bottomSheetDialog.dismiss();
                if (checkBoxBtn.isChecked()) {

                    dcViewModel.acceptUserAgreement(userAgreementRequest).observe(getViewLifecycleOwner(), acceptUserAgreement -> {
                        if (acceptUserAgreement != null && acceptUserAgreement.getStatus()) {
                            bottomSheetDialog.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(v.getContext(), "Please accept Terms of Use", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                BOTTOM_SHEET_SHOW = false;
            }
        });
        BOTTOM_SHEET_SHOW = true;
        bottomSheetDialog.show();
    }

    @Override
    public void onPackageBuyListener(DoctorPackages packages) {
        dcViewModel.isUserAgreed(empSrNo, "1").observe(getViewLifecycleOwner(), userAgreement -> {
            Log.d(LogTags.DOCTOR_CONSULTANT, "onPackageBuyListener: " + userAgreement.toString());
            if (userAgreement != null) {
                if (userAgreement.getIsDCAgree()) {
                    dcViewModel.buyDcPackage(extPersonSrNo, extPersonSrNo, packages.getPackagePlanSrNo());
                } else {
                    if (!BOTTOM_SHEET_SHOW) {
                        callPackageAgreement(requireContext());
                    }
                }
            }
        });
    }
}