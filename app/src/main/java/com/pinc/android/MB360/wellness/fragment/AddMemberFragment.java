package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentAddMemberBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.monthpicker.DatePickerTheme;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.healthcheckup.repository.requestclass.DependentRequest;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Relation;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui.RelationSpinnerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddMemberFragment extends Fragment {
    FragmentAddMemberBinding binding;
    View view;
    NavController navController;
    DependentRequest dependentRequest = new DependentRequest();

    //PackageLT ViewModel
    LoadSessionViewModel loadSessionViewModel;
    PackagesViewModel packagesViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    PrimeCalendar DOBDate = new CivilCalendar();

    RelationSpinnerAdapter adapter;

    private boolean selected=false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AddMemberFragment() {
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
        binding = FragmentAddMemberBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        // Spinner Custom Adapter
        binding.spinnerRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Relation relation = (Relation) adapterView.getItemAtPosition(i);

                dependentRequest.setRelationID("" + relation.getRelationId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);

        binding.btnAddMember.setOnClickListener(view -> {

            confirmAddMember();
        });

        binding.etdateofbirth.setOnClickListener(view -> {

            DOBDate();
        });

        binding.etdateofbirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    binding.etage.setText(UtilMethods.getAge(binding.etdateofbirth.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setupSpinner();


//        if (isSelected()) {
//
//            binding.ckdateofbirth.setChecked(true);
//            binding.etdateofbirth.setVisibility(View.GONE);
//
//        } else {
//            binding.ckdateofbirth.setChecked(false);
//            binding.etdateofbirth.setVisibility(View.VISIBLE);
//        }
//
        binding.ckdateofbirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Log.d("TAG=>isChecked", "True");//replace your own stuffs here
                binding.dobTV.setVisibility(View.GONE);
                binding.etdateofbirth.setVisibility(View.GONE);
//                binding.etdateofbirth.getText().clear();
                binding.etage.getText().clear();


                }else {
                    Log.d("TAG=>isChecked", "false");//replace your own stuffs here
                    binding.dobTV.setVisibility(View.VISIBLE);
                    binding.etdateofbirth.setVisibility(View.VISIBLE);
                }
            }
        });
//
        return view;
    }

    public void DOBDate() {


        Calendar calendar = Calendar.getInstance();
        SingleDayPickCallback callback = fromDate -> {

            DOBDate = fromDate.clone();
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd/MM/yyyy");
//            SimpleDateFormat timeRangeFormat = new SimpleDateFormat("hh:mm a");

//            TimePickerDialog timePickerDialog = new TimePickerDialog(requireActivity(), (timePicker, hour, min) -> {

            binding.etdateofbirth.setText(dateRangeFormat.format(DOBDate.getTime()));
//                binding.timeTV1.setText(timeRangeFormat.format(PrefDate.getTime()));
//
//                PrefDate.setHourOfDay(hour);
//                PrefDate.setMinute(min);
//                PrefDate.setMillisecond(0);
//
//            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

//            timePickerDialog.show();


        };
        PrimeCalendar today = new CivilCalendar();
        PrimeCalendar tomorrow = new CivilCalendar();
        tomorrow.add(Calendar.DATE, 0);


        LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

        PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                .pickSingleDay(callback)
                .initiallyPickedSingleDay(DOBDate)
                .maxPossibleDate(today)
                .applyTheme(themeFactory)
                .build();

        datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
    }


    private void confirmAddMember() {

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                dependentRequest.setFamilySrNo(employeeCheckResponse.getExtFamilySrNo());
//                dependentRequest.setFamilySrNo("5625");

            });
        });

        dependentRequest.setPersonName("" + binding.etname.getText().toString());
        dependentRequest.setAge("" + binding.etage.getText().toString());

        if(binding.ckdateofbirth.isChecked()){
            dependentRequest.setDateOfBirth("10/10/1990");
        }else {
            dependentRequest.setDateOfBirth("" + binding.etdateofbirth.getText().toString());
        }

        dependentRequest.setGender("Male");

        packagesViewModel.dependent(dependentRequest).observe(getViewLifecycleOwner(), messageResponseDependent -> {
            Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "confirmAppointment: " + messageResponseDependent.toString());

        });

        Toast.makeText(getActivity(), "Add Member Successfully", Toast.LENGTH_LONG).show();
        requireActivity().onBackPressed();
    }

    private void setupSpinner() {

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                packagesViewModel.getAllRelation(employeeCheckResponse.getExtFamilySrNo()).observe(getViewLifecycleOwner(), allRelationResponse -> {

//                    ArrayAdapter adRelation = new ArrayAdapter(getContext(), R.layout.spinner_item, R.id.tvspinneritem, allRelationResponse.getRelations());
//                    binding.spinnerRelation.setAdapter(adRelation);

                    adapter = new RelationSpinnerAdapter(getContext(), allRelationResponse.getRelations());
                    binding.spinnerRelation.setAdapter(adapter);


                });
//                dependentRequest.setFamilySrNo("5625");

            });
        });

    }

}
