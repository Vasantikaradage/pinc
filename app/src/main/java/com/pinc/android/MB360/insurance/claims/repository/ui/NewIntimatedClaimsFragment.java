package com.pinc.android.MB360.insurance.claims.repository.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentNewIntimatedClaimsBinding;
import com.pinc.android.MB360.insurance.claims.repository.ClaimsViewModel;
import com.pinc.android.MB360.insurance.claims.repository.requestclass.NewIntimateRequest;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.ClaimBeneficiary;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGPAPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGTLPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewIntimatedClaimsFragment extends Fragment {


    FragmentNewIntimatedClaimsBinding binding;
    View view;

    //viewModels
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    ClaimsViewModel claimsViewModel;
    NewIntimateRequest newIntimateRequest = new NewIntimateRequest();
    Boolean CLAIM_SUCCESS_SHOWN = false;
    ClaimIntimationListener claimIntimationListener;


    private int year, day, month;
    private Calendar c;

    public NewIntimatedClaimsFragment() {

    }

    public NewIntimatedClaimsFragment(ClaimIntimationListener claimIntimationListener) {
        // Required empty public constructor
        this.claimIntimationListener = claimIntimationListener;
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

        binding = FragmentNewIntimatedClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        claimsViewModel = new ViewModelProvider(this).get(ClaimsViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);


        claimsViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });


        getPersonData();
        setupPersonSpinner();

        return view;
    }

    //loading he person for intimation
    private void getPersonData() {

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            String PRODUCT_CODE = groupPolicyData.getProductCode();


            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

                try {


                    String groupChildSrvNo = "";
                    String oeGrpBasInfSrNo = "";
                    String employeeSrNo = "";


                    switch (PRODUCT_CODE) {
                        case "GMC":
                            GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum;
                            groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);

                            //queries for persons data
                            employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                            oeGrpBasInfSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                            //new intimation request
                            newIntimateRequest.setEmployeesrno(employeeSrNo);
                            newIntimateRequest.setGroupchildsrno(groupChildSrvNo);
                            newIntimateRequest.setOegrpbasinfsrno(oeGrpBasInfSrNo);

                            claimsViewModel.getPersons(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);

                            break;
                        case "GPA":
                            GroupGPAPolicyEmployeeDatum groupGPAPolicyEmployeeDatum;
                            groupGPAPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0);

                            //queries for persons data
                            employeeSrNo = groupGPAPolicyEmployeeDatum.getEmployeeSrNo();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                            oeGrpBasInfSrNo = groupGPAPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                            //new intimation request
                            newIntimateRequest.setEmployeesrno(employeeSrNo);
                            newIntimateRequest.setGroupchildsrno(groupChildSrvNo);
                            newIntimateRequest.setOegrpbasinfsrno(oeGrpBasInfSrNo);

                            claimsViewModel.getPersons(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);

                            break;
                        case "GTL":

                            GroupGTLPolicyEmployeeDatum groupGTLPolicyEmployeeDatum;
                            groupGTLPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0);

                            //queries for persons data
                            employeeSrNo = groupGTLPolicyEmployeeDatum.getEmployeeSrNo();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                            oeGrpBasInfSrNo = groupGTLPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                            //new intimation request
                            newIntimateRequest.setEmployeesrno(employeeSrNo);
                            newIntimateRequest.setGroupchildsrno(groupChildSrvNo);
                            newIntimateRequest.setOegrpbasinfsrno(oeGrpBasInfSrNo);

                            claimsViewModel.getPersons(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
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
    }

    private void setupPersonSpinner() {
        //get the data from the post call.
        claimsViewModel.getPersonsData().observe(getViewLifecycleOwner(), personData -> {
            if (personData != null) {
                if (personData.getClaimBeneficiary() != null) {
                    List<ClaimBeneficiary> personsArray = new ArrayList<>();
                    ClaimBeneficiary personDummy = new ClaimBeneficiary();
                    personDummy.setPersonName("Intimate for");
                    personDummy.setPersonSrNo("0");
                    personDummy.setRelationName("NONE");
                    personsArray.add(personDummy);
                    for (ClaimBeneficiary person : personData.getClaimBeneficiary()
                    ) {

                        personsArray.add(person);
                    }

                    ClaimsSpinnerAdapter spinnerAdapter = new ClaimsSpinnerAdapter(getContext(), personsArray);

                    binding.personSpinner.setAdapter(spinnerAdapter);


                } else {
                    //TODO SHOW ERROR
                    Toast.makeText(requireActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            } else {
                //TODO SHOW ERROR
                Toast.makeText(requireActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //initiate the new intimate claims.
        binding.btnIntimateNow.setOnClickListener(v -> {
            startNewIntimation();
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.etEstmatedReport.removeTextChangedListener(this);
                binding.etEstmatedReport.setText(UtilMethods.PriceFormat(s.toString().replace(",", "")));
                binding.etEstmatedReport.setSelection(binding.etEstmatedReport.getText().length());
                binding.errorEstdReport.setVisibility(View.GONE);
                binding.etEstmatedReport.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        binding.etEstmatedReport.addTextChangedListener(textWatcher);


        binding.etHospLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.errorHospLoc.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etNOH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.errorNOH.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etDiagnosis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.errorDiagnosis.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //dates for DOA
        binding.etDOA.setOnFocusChangeListener((v, b) -> {
            binding.errorDOA.setVisibility(View.GONE);
            if (b) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                binding.scroll1.setSmoothScrollingEnabled(true);
                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog2 = new DatePickerDialog(getActivity(), datePickerListener,
                        year, month, day);
                c.add(Calendar.YEAR, -80);
                dialog2.getDatePicker().setMinDate(c.getTimeInMillis());
// dialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog2.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                        (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                binding.etNOH.requestFocus();
                            }
                        });
                dialog2.show();
            }
        });


        //reset functions
        binding.btnReset.setOnClickListener(v -> {
            resetIntimation();
        });

        //spinner selection listener for person
        binding.personSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // select the name of the person who has started the claims
                ClaimBeneficiary person = (ClaimBeneficiary) adapterView.getSelectedItem();
                newIntimateRequest.setPersonsrno(person.getPersonSrNo());
                if (i == 0) {
                    binding.llDetLayout.setVisibility(View.GONE);
                } else {
                    binding.llDetLayout.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.personSpinner.setSelection(0);
            }
        });

    }

    public DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            // set selected date into textview
            Calendar calendar = Calendar.getInstance();
            calendar.set(selectedYear, selectedMonth, selectedDay);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String strDate = format.format(calendar.getTime());
            binding.etDOA.setText(strDate);
            binding.etNOH.requestFocus();
        }
    };

    private void resetIntimation() {
        binding.etDiagnosis.setText(null);
        binding.personSpinner.setSelection(0);
        binding.etEstmatedReport.setText(null);
        binding.etDOA.setText(null);
        binding.errorDOA.setText(null);
        binding.etNOH.setText(null);
        binding.etHospLoc.setText(null);


        binding.errorEstdReport.setVisibility(View.GONE);
        binding.errorDiagnosis.setVisibility(View.GONE);
        binding.errorDOA.setVisibility(View.GONE);
        binding.errorHospLoc.setVisibility(View.GONE);
        binding.errorNOH.setVisibility(View.GONE);
        binding.errorSelect.setVisibility(View.GONE);

    }

    private void startNewIntimation() {

        //new intimation request body
       /* {
            "groupchildsrno":"1024",
                "oegrpbasinfsrno":"1047",
                "employeesrno":"61787",
                "personsrno":"176800",
                "diagnosis":"FEVER",
                "claimamount":"50000",
                "doalikelydoa":"24/08/2021",
                "hospitalname":"KIMS HOSPITAL",
                "hospitallocation":"PUNE"
        }*/

        newIntimateRequest.setDiagnosis(binding.etDiagnosis.getText().toString().trim());
        newIntimateRequest.setClaimamount(binding.etEstmatedReport.getText().toString().trim().replace(",", ""));
        newIntimateRequest.setDoalikelydoa(binding.etDOA.getText().toString().trim());
        newIntimateRequest.setHospitalname(binding.etNOH.getText().toString().trim());
        newIntimateRequest.setHospitallocation(binding.etHospLoc.getText().toString().trim());

        Log.d("NEW-INTIMATION", "startNewIntimation: " + newIntimateRequest.toString());


        if (verifyIntimation() && intimationClaimValues()) {

            //new intimation start
            claimsViewModel.startIntimation(newIntimateRequest).observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    if (result.getStatus()) {
                        if (!CLAIM_SUCCESS_SHOWN) {
                            CLAIM_SUCCESS_SHOWN = true;
                            claimFeedback(result.getMessage());
                            resetIntimation();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Something went Wrong! try again later.", Toast.LENGTH_LONG).show();
                        resetIntimation();
                    }
                }

            });
        } else {

        }

    }

    public void claimFeedback(String feedback) {
        /*Mobile no does not exists in database or Wrong Format mobile no*/
        final Dialog alertDialog = new Dialog(requireActivity());
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));

        LayoutInflater mLayoutInflater = getLayoutInflater();
        View alertLayout = mLayoutInflater.inflate(R.layout.dialog_internet_error, null);
        alertDialog.setContentView(alertLayout);
        ImageView alertIcon = alertLayout.findViewById(R.id.alertIcon);
        alertIcon.setImageResource(R.drawable.ic_mark_as_solved);
        Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setOnDismissListener(dialogInterface -> {
            CLAIM_SUCCESS_SHOWN = false;
            claimIntimationListener.onClaimIntimatedListener();
        });

        TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
        alertMessage.setText(feedback);
        alertDialog.show();
    }

    private boolean intimationClaimValues() {
        String claims_amount = binding.etEstmatedReport.getText().toString().replace(",", "");
        long max_value = 10000000;
        long min_value = 1;


        if (claims_amount != null || !claims_amount.isEmpty() || !claims_amount.equals("")) {
            try {
                long actual_value = Long.parseLong(claims_amount);
                if (actual_value > max_value) {
                    binding.errorEstdReport.setText("Max amount can be claimed is ₹1,00,00,000");
                    binding.errorEstdReport.setVisibility(View.VISIBLE);

                    binding.errorDiagnosis.setVisibility(View.GONE);
                    binding.errorDOA.setVisibility(View.GONE);
                    binding.errorHospLoc.setVisibility(View.GONE);
                    binding.errorNOH.setVisibility(View.GONE);
                    binding.errorSelect.setVisibility(View.GONE);
                    return false;
                } else if (actual_value < min_value) {
                    binding.errorEstdReport.setText("Amount should be greater than 0");
                    binding.errorEstdReport.setVisibility(View.VISIBLE);

                    binding.errorDiagnosis.setVisibility(View.GONE);
                    binding.errorDOA.setVisibility(View.GONE);
                    binding.errorHospLoc.setVisibility(View.GONE);
                    binding.errorNOH.setVisibility(View.GONE);
                    binding.errorSelect.setVisibility(View.GONE);
                    return false;
                } else {
                    return true;
                }

            } catch (Exception e) {
                Log.e(LogTags.CLAIM_ACTIVITY, "INTIMATION VALUE CHECK FAILED: ", e);
                return false;
            }
        } else {
            return true;
        }

    }

    private boolean verifyIntimation() {
        if (newIntimateRequest.getDiagnosis().isEmpty()) {
            binding.errorDiagnosis.setVisibility(View.VISIBLE);
            binding.errorDOA.setVisibility(View.GONE);
            binding.errorEstdReport.setVisibility(View.GONE);
            binding.errorHospLoc.setVisibility(View.GONE);
            binding.errorNOH.setVisibility(View.GONE);
            binding.errorSelect.setVisibility(View.GONE);
            return false;
        } else if (newIntimateRequest.getClaimamount().isEmpty()) {
            binding.errorDiagnosis.setVisibility(View.GONE);
            binding.errorDOA.setVisibility(View.GONE);
            binding.errorEstdReport.setVisibility(View.VISIBLE);
            binding.errorHospLoc.setVisibility(View.GONE);
            binding.errorNOH.setVisibility(View.GONE);
            binding.errorSelect.setVisibility(View.GONE);
            return false;
        } else if (newIntimateRequest.getDoalikelydoa().isEmpty()) {
            binding.errorDiagnosis.setVisibility(View.GONE);
            binding.errorDOA.setVisibility(View.VISIBLE);
            binding.errorEstdReport.setVisibility(View.GONE);
            binding.errorHospLoc.setVisibility(View.GONE);
            binding.errorNOH.setVisibility(View.GONE);
            binding.errorSelect.setVisibility(View.GONE);
            return false;
        } else if (newIntimateRequest.getHospitalname().isEmpty()) {
            binding.errorDiagnosis.setVisibility(View.GONE);
            binding.errorDOA.setVisibility(View.GONE);
            binding.errorEstdReport.setVisibility(View.GONE);
            binding.errorHospLoc.setVisibility(View.GONE);
            binding.errorNOH.setVisibility(View.VISIBLE);
            binding.errorSelect.setVisibility(View.GONE);
            return false;
        } else if (newIntimateRequest.getHospitallocation().isEmpty()) {
            binding.errorDiagnosis.setVisibility(View.GONE);
            binding.errorDOA.setVisibility(View.GONE);
            binding.errorEstdReport.setVisibility(View.GONE);
            binding.errorHospLoc.setVisibility(View.VISIBLE);
            binding.errorNOH.setVisibility(View.GONE);
            binding.errorSelect.setVisibility(View.GONE);
            return false;
        } else if (binding.personSpinner.getSelectedItemPosition() == 0) {
            binding.errorDiagnosis.setVisibility(View.GONE);
            binding.errorDOA.setVisibility(View.GONE);
            binding.errorEstdReport.setVisibility(View.GONE);
            binding.errorHospLoc.setVisibility(View.GONE);
            binding.errorNOH.setVisibility(View.GONE);
            binding.errorSelect.setVisibility(View.VISIBLE);
            return false;
        } else {
            return true;
        }

    }
}