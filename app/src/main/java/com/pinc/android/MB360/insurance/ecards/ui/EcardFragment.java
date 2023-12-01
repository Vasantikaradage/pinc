package com.pinc.android.MB360.insurance.ecards.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentEcardBinding;
import com.pinc.android.MB360.insurance.ecards.repository.EcardViewModel;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

public class EcardFragment extends Fragment {


    FragmentEcardBinding binding;
    View view;

    //viewModel
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    EcardViewModel ecardViewModel;
    NavController navController;

    // creating object of WebView
    WebView printWeb;

    // object of print job
    PrintJob printJob;

    // a boolean to check the status of printing
    boolean printBtnPressed = false;


    public EcardFragment() {
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
        binding = FragmentEcardBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        ecardViewModel = new ViewModelProvider(this).get(EcardViewModel.class);


        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        getEcard();


        binding.btnSavePdf.setOnClickListener(v -> {
            if (printWeb != null) {
                // Calling createWebPrintJob()
                PrintTheWebPage(printWeb);
            } else {
                // Showing Toast message to user
                Toast.makeText(requireContext(), "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
            }

        });


        ecardViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        ecardViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.btnSavePdf.setVisibility(View.GONE);


            } else {
                binding.errorLayout.setVisibility(View.GONE);
                binding.btnSavePdf.setVisibility(View.GONE);
            }
        });


        return view;

    }

    private void getEcard() {

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

                try {
                    String employee_sr = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
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


                    ecardViewModel.getEcard(dataRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });


        ecardViewModel.getEcardData().observe(getViewLifecycleOwner(), ecardResponse -> {
            if (ecardResponse != null) {
                Log.d(LogTags.E_CARD_ACTIVITY, ecardResponse.toString());
                if (ecardResponse.getStatus().equals(AppServerConstants.SUCCESS)) {


                    binding.ecardWebview.setWebViewClient(new WebViewClient() {
                        @Override

                        public void onPageFinished(WebView view, String url) {

                            super.onPageFinished(view, url);

                            // initializing the printWeb Object

                            binding.progressBar.setVisibility(View.GONE);
                            printWeb = view;

                        }
                    });

                    if (ecardResponse.getEcardInformation().contains("E-card under process")) {
                        binding.errorTextECard.setText("E-card under process");
                        binding.webViewHolder.setVisibility(View.GONE);
                        binding.errorLayout.setVisibility(View.VISIBLE);

                    } else {
                        String url = "";

                        if (ecardResponse.getEcardInformation().contains("/mybenefits")) {
                            //if we get this locally from our system (my benefits).
                            url = BuildConfig.DOWNLAOD_BASE_URL + ecardResponse.getEcardInformation();
                        }
                        url = url + ecardResponse.getEcardInformation();

                        Intent opendata = new Intent();
                        opendata.setAction(Intent.ACTION_VIEW);
                        //requireActivity().onBackPressed();

                        opendata.setData(Uri.parse(url));

                        startActivity(opendata);

                    }

                }
            }

        });
    }


    private void PrintTheWebPage(WebView webView) {

        // set printBtnPressed true
        printBtnPressed = true;

        // Creating  PrintManager instance
        PrintManager printManager = (PrintManager) requireContext()
                .getSystemService(Context.PRINT_SERVICE);

        // setting the name of job
        String jobName = getString(R.string.app_name) + " webpage" + webView.getUrl();

        // Creating  PrintDocumentAdapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);


        // Create a print job with name and adapter instance
        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @Override
    public void onResume() {
        super.onResume();
        printBtnPressed = false;
        navController.navigateUp();
    }
}