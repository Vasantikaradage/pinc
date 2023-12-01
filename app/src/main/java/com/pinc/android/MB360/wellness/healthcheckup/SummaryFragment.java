package com.pinc.android.MB360.wellness.healthcheckup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentSummaryBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.PaymentActivity;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui.SummaryPersonAdapter;


public class SummaryFragment extends Fragment {

    FragmentSummaryBinding binding;
    View view;
    NavController navController;

    //PackageLT ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    //Adapter
    SummaryPersonAdapter adapter;

    String familySrNo;
    String groupSrNo;
    String empIdNo;

    public SummaryFragment() {
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
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);

        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);


        binding.btnconfirmnow.setOnClickListener(view -> {

            ConfirmNow(""+binding.tvYoupaidAmount.getText().toString());

        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSummary();
    }


    private void getSummary() {

//        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
//
//            String city = "Mumbai";
//
//            packagesViewModel.getDiagnosticCenter(city);
//        });

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                familySrNo = employeeCheckResponse.getExtFamilySrNo();
                groupSrNo = employeeCheckResponse.getExtGroupSrNo();
                String groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

                packagesViewModel.getSummary(familySrNo,groupCode);
            });
        });


        packagesViewModel.getSummaryData().observe(getViewLifecycleOwner(), summaryResponse -> {

            if (!summaryResponse.getMessage().getStatus()) {

                binding.cartLL.setVisibility(View.GONE);
                binding.noHistoryImage.setVisibility(View.VISIBLE);

            }else{

                binding.cartLL.setVisibility(View.VISIBLE);
                binding.noHistoryImage.setVisibility(View.GONE);
            }


            if (summaryResponse != null) {

                if (summaryResponse.getSummary().getCompanySponseredPerson().isEmpty()){
                    binding.llcompanysponsored.setVisibility(View.GONE);
                }else if(summaryResponse.getSummary().getTotal().equals("0")){
                    binding.llselfsponsored.setVisibility(View.GONE);
                }

                adapter = new SummaryPersonAdapter(requireActivity(), summaryResponse.getSummary().getSelfSponseredPerson());
                binding.rvpaidmembers.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, summaryResponse.getSummary().getSelfSponseredPerson().size());

                if (!summaryResponse.getMessage().getStatus()) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("" + summaryResponse.getMessage().getMessage());
                }

                binding.tvtotal.setText("\u20B9 "+UtilMethods.PriceFormat(summaryResponse.getSummary().getTotal()));
                binding.tvPaidAmount.setText("\u20B9 "+UtilMethods.PriceFormat(summaryResponse.getSummary().getPaid()));
                binding.tvYoupaidAmount.setText("\u20B9 "+UtilMethods.PriceFormat(summaryResponse.getSummary().getYoupay()));

            } else {
                binding.messageTextView.setVisibility(View.VISIBLE);
                binding.messageTextView.setText("Sorry, data not available");
            }

            if (summaryResponse.getSummary().getTotal().equals("0")){

                binding.btnconfirmnow.setEnabled(false);
                binding.btnconfirmnow.setClickable(false);
                binding.btnconfirmnow.setFocusable(false);
            }
        });
    }


    private void ConfirmNow(String youpay){

        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(width, height);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvconfirmtext = dialog.findViewById(R.id.tvconfirmtext);
        TextView tvconfirmsubtext = dialog.findViewById(R.id.tvconfirmsubtext);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);

        Button btnyes = dialog.findViewById(R.id.btnyes);
        Button btnno = dialog.findViewById(R.id.btnno);

        if (youpay.equals("0")) {
            tvconfirmtext.setText("Do you wish to confirm you appointment?");
            tvconfirmsubtext.setVisibility(View.GONE);
        } else
            tvconfirmtext.setText("You will be redirected to our secure payment gateway in order to process your payment of  " + youpay + " and schedule your Health Checkups. Do you wish to continue?");

        btnyes.setOnClickListener(view -> {
            dialog.dismiss();
//            PaymentNow(youpay);
            Intent paymentIntent = new Intent(getActivity(), PaymentActivity.class);
            paymentIntent.putExtra("youpay",""+youpay.replace("," , ""));
            paymentIntent.putExtra("familySrNo",""+familySrNo);
            paymentIntent.putExtra("groupSrNo",""+groupSrNo);
            paymentIntent.putExtra("empIdNo",""+empIdNo);
            startActivity(paymentIntent);
        });

        btnno.setOnClickListener(view -> dialog.dismiss());

        iv_close.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

}