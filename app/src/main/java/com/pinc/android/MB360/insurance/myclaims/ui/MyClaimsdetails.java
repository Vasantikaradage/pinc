package com.pinc.android.MB360.insurance.myclaims.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentMyClaimsdetailsBinding;
import com.pinc.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.pinc.android.MB360.insurance.myclaims.responseclass.Claims;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimAilmentInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimCashLessInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimChargesInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimFileInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimHospitalInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimIncidentInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimMemberInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimPaymentInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimProcessInformation;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.pinc.android.MB360.utilities.UtilMethods;

import java.text.MessageFormat;

public class MyClaimsdetails extends Fragment {


    FragmentMyClaimsdetailsBinding binding;
    View view;
    MyClaimsViewModel myClaimsViewModel;
    LoadSessionViewModel loadSessionViewModel;
    String claims;

    public MyClaimsdetails() {
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
        binding = FragmentMyClaimsdetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        myClaimsViewModel = new ViewModelProvider(requireActivity()).get(MyClaimsViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);


        myClaimsViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBarLayout.setVisibility(View.VISIBLE);
                binding.claimLayout.setVisibility(View.GONE);
            } else {
                binding.progressBarLayout.setVisibility(View.GONE);
                binding.claimLayout.setVisibility(View.VISIBLE);

            }


        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        claims = MyClaimsdetailsArgs.fromBundle(getArguments()).getClaims();


        getMyClaimsDetails();


    }

    private void getMyClaimsDetails() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        String dataRequest = "";
        if (loadSessionResponse != null) {

            try {
                dataRequest = "<DataRequest>" +
                        "<groupchildsrno>" + loadSessionResponse.getGroupInfoData().getGroupchildsrno() + "</groupchildsrno>" +
                        "<oegrpbasinfsrno>" + loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo() + "</oegrpbasinfsrno>" +
                        "<claimsrno>" + claims + "</claimsrno>" +
                        "</DataRequest>";
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        myClaimsViewModel.getMyClaimsDetails(dataRequest, String.valueOf(claims)).observe(getViewLifecycleOwner(), claimsDetails -> {
            if (claimsDetails != null) {

                binding.MyClaimCard.setVisibility(View.VISIBLE);
                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.titleTxt3.setVisibility(View.VISIBLE);
                binding.rv3.setVisibility(View.VISIBLE);
                binding.lv2.setVisibility(View.VISIBLE);
                binding.lvCopayment.setVisibility(View.VISIBLE);


                ClaimInformation claimInformation = claimsDetails.getClaimInformation();
                ClaimMemberInformation claimMemberInformation = claimInformation.getClaimMemberInformation();
                ClaimIncidentInformation claimIncidentInformation = claimsDetails.getClaimInformation().getClaimIncidentInformation();
                ClaimHospitalInformation claimHospitalInformation = claimInformation.getClaimHospitalInformation();
                ClaimAilmentInformation claimAilmentInformation = claimInformation.getClaimAilmentInformation();
                ClaimCashLessInformation claimCashLessInformation = claimInformation.getClaimCashlessInformation();
                ClaimChargesInformation claimChargesInformation = claimInformation.getClaimChargesInformation();
                ClaimFileInformation claimFileInformation = claimInformation.getClaimFileInformation();
                ClaimProcessInformation claimProcessInformation = claimInformation.getClaimProcessInformation();
                ClaimPaymentInformation claimPaymentInformation = claimInformation.getClaimPaymentInformation();

                //member info
                binding.tvBeneficiaryValue.setText(claimMemberInformation.getBeneficiaryName());
                binding.tvRelationValue.setText(claimMemberInformation.getRelation());
                binding.tvValue.setText(claimMemberInformation.getGender());
                binding.tvAgeValue.setText(claimMemberInformation.getAge());
                binding.tvTPAValue.setText(claimMemberInformation.getTPAId());
                binding.tvDOBValue.setText(claimMemberInformation.getDateOfBirth());

                //claim info
                binding.tvClaimNumberValue.setText(claimIncidentInformation.getClaimNo());
                binding.titleTxt2Date.setText(claimIncidentInformation.getClaimDate());
                binding.tvValue2.setText(claimHospitalInformation.getHospitalName());
                binding.tvDOAValue2.setText(claimHospitalInformation.getDateOfAdmission());
                binding.tvDODValue.setText(claimHospitalInformation.getDateOfDischarge());

                //cashless info
                binding.tvDOAValue1.setText(claimCashLessInformation.getCashlessSentDate());
                binding.tvDODValue1.setText(claimCashLessInformation.getCashlessAmount());

                //claim charge
                binding.tvClaimNumberValue11.setText(UtilMethods.PriceFormat(claimChargesInformation.getFinalBillAmount()));
                binding.tvValue66.setText(claimChargesInformation.getDeductionReasons());
                binding.tvValue3.setText(claimChargesInformation.getNonPayableExpenses());

                //claim ailment
                binding.tvAilmentValue.setText(claimAilmentInformation.getAilment());

                //file
                binding.tvAmountValue11.setText(claimFileInformation.getFileReceivedDate());

                //process
                String claim_type = claimProcessInformation.getTypeOfClaim() + "_" + claimProcessInformation.getClaimStatus();
                binding.tvAmountValue.setText(MessageFormat.format("{0}", UtilMethods.PriceFormat(claimProcessInformation.getClaimReportedAmount())));

                //payment
                String claim_paid_date = claimProcessInformation.getClaimPaidDate();
                String claimRejectedOn = claimProcessInformation.getClaimRejectedDate();
                String claimDeficient = claimFileInformation.getDeficiencies();
                String strChequeNo = claimPaymentInformation.getBankOrChequeNo();
                String CoPaymentDeductions = claimChargesInformation.getCoPaymentDeductions();
                String ClaimClosureReasons = claimProcessInformation.getClaimClosureReasons();
                String claimrejectedReasons = claimProcessInformation.getClaimDenialReasons();
                String strPaidAmount = claimProcessInformation.getClaimPaidAmount();
                String strcashAuth = claimProcessInformation.getClaimPaidDate();
                String strDeductReasons = "";
                String strDeductions = "";
                String NonPayableExpenses = "";
                NonPayableExpenses = claimChargesInformation.getNonPayableExpenses();
                String claimDefStatus = claimProcessInformation.getClaimOutstandingStatus();
                String coPaymentDeductions = claimChargesInformation.getCoPaymentDeductions();
                int progressAnim = 6000;


                int intClaimAmt = 0;
                int intReportAmt = 0;

                switch (claim_type.toString().toLowerCase()) {
                    case "reimbursement_rejected":
                        binding.titleTxt3.setText(R.string.claim_reject);

                        binding.llLastCard1.setVisibility(View.GONE);

                        binding.tvKey2.setText("Deficiencies");
                        binding.tvValue1.setText(claimDeficient);

                        binding.tvKey4.setText(R.string.reject_reason);
                        binding.tvValue4.setText(claimrejectedReasons);

                        binding.tvKey3.setText(R.string.reject_on);
                        binding.tvValue3.setPadding(0, 0, 0, 0);
                        binding.tvValue3.setText(claimRejectedOn);

                        binding.rsAmount.setVisibility(View.GONE);

                        binding.img3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.close_white));
                        binding.rv3.setBackgroundResource(R.drawable.claim_rejected_back);

                        binding.btnShowMore.setVisibility(View.GONE);

                        break;

                    /*Paid Amount&&Paid Date&&Cheque Number/NEFT Details&&Decutions&&Deduction Reasons*/
                    case "reimbursement_paid":

                        binding.img3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_mark_as_solved));
                        binding.rv3.setBackgroundResource(R.drawable.claim_reported_back);

                        binding.titleTxt3.setText(R.string.claim_pay);

                        binding.tvClaimNumber1.setText(R.string.paid_amt);
                        binding.tvClaimRs.setVisibility(View.VISIBLE);
                        binding.tvClaimNumberValue1.setText(UtilMethods.PriceFormat(strPaidAmount)
                        );

                        binding.tvAmount1.setText(R.string.paid_date);
                        binding.tvAmountValue1.setText(strcashAuth);

                        binding.tvKey2.setText(R.string.chq_no);
                        binding.tvValue1.setText(strChequeNo);

                        binding.tvKey3.setText("Not Payable Expenses");

//                                    intClaimAmt = Integer.parseInt(strPaidAmount.replace(",", ""));
                        intClaimAmt = Integer.parseInt(strPaidAmount);
                        strDeductions = String.valueOf(intReportAmt - intClaimAmt);
                        if (NonPayableExpenses.equals("-") | NonPayableExpenses.equals(""))
                            binding.tvValue3.setText("-");
                        else
                            binding.tvValue3.setText(MessageFormat.format("{0} ",
                                    NonPayableExpenses));

                        binding.coPayRs.setVisibility(View.VISIBLE);
                        binding.lvCopayment.setVisibility(View.VISIBLE);

                        binding.tvCoPayment.setText("Co-Payment Deductions");

                        if (CoPaymentDeductions.equals("-") || CoPaymentDeductions.equals("") || CoPaymentDeductions.isEmpty()) {
                            binding.tvCoPaymentValue.setText("-");
                        } else if (CoPaymentDeductions.contains("-")) {
                                       /* tvCoPaymentValue.setText(MessageFormat.format("{0} ",
                                                CoPaymentDeductions));*/ //hide by maddy 02/03/2022
                            binding.tvCoPaymentValue.setText("-"); // add by maddy 02/03/2022
                        } else {
                            binding.tvCoPaymentValue.setText(MessageFormat.format("{0} ",
                                    UtilMethods.PriceFormat(CoPaymentDeductions.replace(",", ""))));
                        }

                        binding.tvKey4.setText(R.string.deduct_reasons);
                        binding.tvValue4.setText(strDeductReasons);
                        break;

                    case "cashless_outstanding":
                    case "cashless_paid":
                    case "cashless_rejected":
                    case "cashless_denied":


                        binding.titleTxt3.setText(R.string.claim_pay);

                        binding.img3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_mark_as_solved));
                        binding.rv3.setBackgroundResource(R.drawable.claim_reported_back);


                        binding.tvClaimNumber1.setText(R.string.paid_amt);
                        binding.tvClaimRs.setVisibility(View.VISIBLE);
                        binding.tvClaimNumberValue1.setText(UtilMethods.PriceFormat(strPaidAmount)
                        );

                        binding.tvAmount1.setText(R.string.paid_date);
                        binding.tvAmountValue1.setText(strcashAuth);

                        binding.tvKey2.setText(R.string.chq_no);
                        binding.tvValue1.setText(strChequeNo);

                        binding.tvKey3.setText("Not Payable Expenses");

//                                    intClaimAmt = Integer.parseInt(strPaidAmount.replace(",", ""));
                        intClaimAmt = Integer.parseInt(strPaidAmount);
                        strDeductions = String.valueOf(intReportAmt - intClaimAmt);
                        if (NonPayableExpenses.equals("-") || NonPayableExpenses.equals(""))
                            binding.tvValue3.setText("-");
                        else
                            binding.tvValue3.setText(MessageFormat.format("{0} ",
                                    UtilMethods.PriceFormat(NonPayableExpenses)));


                        binding.coPayRs.setVisibility(View.VISIBLE);
                        binding.lvCopayment.setVisibility(View.VISIBLE);

                        binding.tvCoPayment.setText("Co-Payment Deductions");

                        if (CoPaymentDeductions.equals("0") || CoPaymentDeductions.equals("-") || CoPaymentDeductions.equals("") || CoPaymentDeductions.isEmpty()) {
                            binding.tvCoPaymentValue.setText("-");
                        } else if (CoPaymentDeductions.contains("-")) {
                            binding.tvCoPaymentValue.setText("-");
                        } else {
                            binding.tvCoPaymentValue.setText(MessageFormat.format("{0} ",
                                    UtilMethods.PriceFormat(CoPaymentDeductions.replace(",", ""))));
                        }
                        binding.tvKey4.setText(R.string.deduct_reasons);
                        binding.tvValue4.setText(strDeductReasons);
                        //Log.d("strDeductReasons",strDeductReasons);
/*
                                    if (strDeductReasons.length() > 100)
                                        btnShowMore.setVisibility(View.VISIBLE);
                                    else
                                        btnShowMore.setVisibility(View.GONE);
                                    */
                        break;

                    case "reimbursement_closed":

                        binding.titleTxt3.setText(R.string.claim_closed);
                        binding.tvClaimNumber1.setText(R.string.claim_close_reason);
                        binding.img3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.close_white));
                        binding.rv3.setBackgroundResource(R.drawable.claim_rejected_back);

                        binding.tvClaimNumberValue1.setText(claimRejectedOn);

                        binding.tvKey66.setText(R.string.claim_close_on);
                        binding.tvValue66.setText(ClaimClosureReasons);

                        binding.lvCopayment.setVisibility(View.GONE);
                        binding.tvClaimRs.setVisibility(View.GONE);
                        binding.lv2.setVisibility(View.INVISIBLE);
                        binding.lv3.setVisibility(View.GONE);
                        binding.lv4.setVisibility(View.GONE);
                        binding.lv5.setVisibility(View.GONE);
                        break;

                    case "reimbursement_outstanding":
                        if (!claimDefStatus.equals("Deficient")) {

                            ConstraintLayout.LayoutParams conlay =
                                    new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);

                            conlay.bottomToBottom = R.id.rv2;
                            progressAnim = 3000;
                            binding.MyClaimCard.setVisibility(View.GONE);
                            binding.rv3.setVisibility(View.GONE);
                            binding.progressBar2.setVisibility(View.GONE);
                            binding.titleTxt3.setVisibility(View.GONE);

                        } else {

                            binding.titleTxt3.setText(R.string.claim_deficient);
                            binding.tvKey2.setVisibility(View.VISIBLE);
                            binding.tvValue1.setVisibility(View.VISIBLE);

                            binding.tvKey2.setText(R.string.def_detail);
                            binding.tvValue1.setText(claimDeficient);
                            binding.rv3.setBackgroundResource(R.drawable.claim_outstanding_back);
                            binding.progressBar2.setVisibility(View.VISIBLE);


                            binding.tvCoPayment.setText(R.string.def_raised_on);
                            if (coPaymentDeductions.isEmpty() || coPaymentDeductions.equals(null) || coPaymentDeductions.equals("0")) {
                                binding.tvCoPaymentValue.setText("-");
                            } else {
                                binding.tvCoPaymentValue.setText(coPaymentDeductions);
                            }
                            binding.llLastCard1.setVisibility(View.GONE);
                            binding.lv4.setVisibility(View.GONE);
                            binding.lv5.setVisibility(View.GONE);
                            binding.lvCopayment.setVisibility(View.VISIBLE);

                        }
                        break;
                    case "cashless_under process - deficiency":
                        binding.MyClaimCard.setVisibility(View.GONE);
                        binding.progressBar2.setVisibility(View.GONE);
                        binding.titleTxt3.setVisibility(View.GONE);
                        binding.rv3.setVisibility(View.GONE);


                        break;
                }

            } else {
                Toast.makeText(requireActivity(), "Something went Wrong!", Toast.LENGTH_SHORT).show();
            }

        });

    }


}