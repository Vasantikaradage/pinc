package com.pinc.android.MB360.wellness.healthcheckup;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentPrefDateBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.monthpicker.DatePickerTheme;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.requestclass.ScheduleRequest;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.DiagnosticCenter;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PrefDateFragment extends Fragment {

    FragmentPrefDateBinding binding;
    View view;
    NavController navController;
    boolean isReschedule;
    boolean isPaid;
    String oldApptInfoSrNo;
    String diagnosticSrNo;

    PrimeCalendar PrefDate = new CivilCalendar();

    PrimeCalendar PrefDate1 = new CivilCalendar();
    PrimeCalendar PrefDate2 = new CivilCalendar();
    PrimeCalendar PrefDate3 = new CivilCalendar();


    //data needed to schedule
    Person person;
    Packages packages;
    DiagnosticCenter diagnosticCenter;

    //PackageLT ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    PrimeCalendar sunday;
    List<PrimeCalendar> weekends = new ArrayList<>();

    public PrefDateFragment() {
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
        binding = FragmentPrefDateBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //  to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);

        confirmBtnValidation();

        binding.lldate1.setOnClickListener(view -> {
            setPreferDate1();
        });

        binding.lldate2.setOnClickListener(view -> {
            setPreferDate2();
        });

        binding.lldate3.setOnClickListener(view -> {
            setPreferDate3();

        });

        // Summary
        binding.btnConfirm.setOnClickListener(view -> {

            if (!TextUtils.equals(binding.tvdate1.getText().toString().trim(), "Select Date") &&
                    !TextUtils.equals(binding.tvdate2.getText().toString().trim(), "Select Date") &&
                    !TextUtils.equals(binding.tvdate3.getText().toString().trim(), "Select Date")) {

                final Dialog dialog = new Dialog(requireActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_appt_schedule);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                ImageView iv_close = dialog.findViewById(R.id.iv_close);
                Button btnyes = dialog.findViewById(R.id.btnyes);
                Button btnno = dialog.findViewById(R.id.btnno);

                btnyes.setOnClickListener(view1 -> {
                    dialog.dismiss();

                    confirmAppointment();

                });

                btnno.setOnClickListener(view1 -> dialog.dismiss());

                iv_close.setOnClickListener(view1 -> dialog.dismiss());

                dialog.show();

            } else {
                Toast.makeText(requireActivity(), "Please select preferred dates", Toast.LENGTH_SHORT).show();
            }

        });


        int weeks = 1000;
        for (int i = 0; i < (weeks * 7); i = i + 7) {
            sunday = new CivilCalendar();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            weekends.add(sunday);
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //needed data to schedule the health checkup
        packages = PrefDateFragmentArgs.fromBundle(getArguments()).getGetPackages();
        person = PrefDateFragmentArgs.fromBundle(getArguments()).getGetPerson();
        diagnosticCenter = PrefDateFragmentArgs.fromBundle(getArguments()).getGetDiagnosticCenter();
        isReschedule = PrefDateFragmentArgs.fromBundle(getArguments()).getIsReschedule();
        isPaid = PrefDateFragmentArgs.fromBundle(getArguments()).getIsPaid();
        oldApptInfoSrNo = PrefDateFragmentArgs.fromBundle(getArguments()).getIsOldApptInfoSrNo();
    }

    public void setPreferDate1() {

        if (PrefDate2 != null) {
            weekends.add(PrefDate2);
            weekends.remove(PrefDate1);

        }
        if (PrefDate3 != null) {
            weekends.add(PrefDate3);
            weekends.remove(PrefDate1);
        }


        Calendar calendar = Calendar.getInstance();
        SingleDayPickCallback callback = fromDate -> {
            PrefDate1 = fromDate.clone();
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd/MM/yyyy");
            binding.tvdate1.setText(dateRangeFormat.format(PrefDate1.getTime()));
            confirmBtnValidation();

        };
        PrimeCalendar today = new CivilCalendar();
        PrimeCalendar tomorrow = new CivilCalendar();
        tomorrow.add(Calendar.DATE, 3);


        LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

        PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(tomorrow)
                .pickSingleDay(callback)
                .disabledDays(weekends)
                .applyTheme(themeFactory)
                .minPossibleDate(tomorrow)
                .build();

        datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
    }

    public void setPreferDate2() {

        if (PrefDate1 != null) {
            weekends.add(PrefDate1);
            weekends.remove(PrefDate2);
        }
        if (PrefDate3 != null) {
            weekends.add(PrefDate3);
            weekends.remove(PrefDate2);
        }



        Calendar calendar = Calendar.getInstance();
        SingleDayPickCallback callback = fromDate -> {

            PrefDate2 = fromDate.clone();
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd/MM/yyyy");
            binding.tvdate2.setText(dateRangeFormat.format(PrefDate2.getTime()));
            confirmBtnValidation();
        };
        PrimeCalendar today = new CivilCalendar();
        PrimeCalendar tomorrow = new CivilCalendar();
        tomorrow.add(Calendar.DATE, 3);

        LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

        PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(tomorrow)
                .pickSingleDay(callback)
                .disabledDays(weekends)
                .minPossibleDate(tomorrow)
                .applyTheme(themeFactory)
                .build();

        datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
    }

    public void setPreferDate3() {

        if (PrefDate2 != null) {
            weekends.add(PrefDate2);
            weekends.remove(PrefDate3);
        }
        if (PrefDate1 != null) {
            weekends.add(PrefDate1);
            weekends.remove(PrefDate1);
        }


        Calendar calendar = Calendar.getInstance();
        SingleDayPickCallback callback = fromDate -> {

            PrefDate3 = fromDate.clone();
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd/MM/yyyy");

            binding.tvdate3.setText(dateRangeFormat.format(PrefDate3.getTime()));
            confirmBtnValidation();

        };
        PrimeCalendar today = new CivilCalendar();
        PrimeCalendar tomorrow = new CivilCalendar();
        tomorrow.add(Calendar.DATE, 3);

        LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

        PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(tomorrow)
                .pickSingleDay(callback)
                .disabledDays(weekends)
                .minPossibleDate(tomorrow)
                .applyTheme(themeFactory)
                .build();

        datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
    }


    private void confirmAppointment() {
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                scheduleRequest.setGroupCode(loadSessionResponse.getGroupInfoData().getGroupcode());
                scheduleRequest.setGroupName(loadSessionResponse.getGroupInfoData().getGroupname());
                scheduleRequest.setEmployeeIdentificationNo(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo());
                
                scheduleRequest.setGroupSrNo(Integer.parseInt(employeeCheckResponse.getExtGroupSrNo()));
                scheduleRequest.setFamilySrNo(employeeCheckResponse.getExtFamilySrNo());

                scheduleRequest.setDiagcenterNo("" + diagnosticCenter.getCenterSrNo());
                scheduleRequest.setOldApptInfoSrNo("" + oldApptInfoSrNo);
            });
        });

        scheduleRequest.setPrefDate1("" + binding.tvdate1.getText().toString());
        scheduleRequest.setPrefDate2("" + binding.tvdate2.getText().toString());
        scheduleRequest.setPrefDate3("" + binding.tvdate3.getText().toString());
        scheduleRequest.setPrefTime1("8:30 AM");
        scheduleRequest.setPrefTime2("8:30 AM");
        scheduleRequest.setPrefTime3("8:30 AM");

        scheduleRequest.setRejtApptSrNo("-1");

        scheduleRequest.setPersonSrNo(String.valueOf(person.getPersonSRNo()));
        scheduleRequest.setDiagcenterNo(scheduleRequest.getDiagcenterNo());
        scheduleRequest.setIsPersonOrFamily("2");
        scheduleRequest.setISRescheduled(isReschedule ? "1" : "0");
        //TODO himanshu setOldApptInfoSrNo
        scheduleRequest.setOldApptInfoSrNo("0");
        //TODO himanshu setPaidOrNot
        scheduleRequest.setPaidOrNot(isPaid ? "1" : "0");
        scheduleRequest.setPackageSrNo(packages.getPackageSrNo());


        packagesViewModel.scheduleHealthCheckup(scheduleRequest).observe(getViewLifecycleOwner(), messageResponse -> {
            Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "confirmAppointment: " + messageResponse.toString());

            if (messageResponse.getMessage().getStatus().equals(true)) {
                showConfirmed();
            } else {

                showReject(messageResponse.getMessage().toString());
            }
        });

        Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "Schedule Request : " + scheduleRequest);


    }

    private void showConfirmed() {

        try {

            final Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.schedule_status);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            ImageView imageView = dialog.findViewById(R.id.image_confirm);
            imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_confirm));
            TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
            TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
            appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textcolorblue));
            Button button = dialog.findViewById(R.id.done);

            button.setOnClickListener(view -> {
                dialog.dismiss();

                //navigate
                NavDirections navDirections = PrefDateFragmentDirections.actionPrefDateFragmentToHealthCheckupFragment();
                navController.navigate(navDirections);
            });

            dialog.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void showReject(String msg) {

        try {

            final Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.schedule_status);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            ImageView imageView = dialog.findViewById(R.id.image_confirm);
            imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rejectnew));
            TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
            TextView appointmentText = dialog.findViewById(R.id.rlAppointment);
            appointmentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
            tvrescheduletext.setTextColor(ContextCompat.getColor(requireContext(), R.color.textred));
            tvrescheduletext.setText(msg);
            Button button = dialog.findViewById(R.id.done);
            button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

            button.setOnClickListener(view -> {
                dialog.dismiss();
            });

            dialog.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void confirmBtnValidation(){

        if (!TextUtils.equals(binding.tvdate1.getText().toString().trim(), "Select Date") &&
                !TextUtils.equals(binding.tvdate2.getText().toString().trim(), "Select Date") &&
                !TextUtils.equals(binding.tvdate3.getText().toString().trim(), "Select Date")) {

            binding.btnConfirm.setEnabled(true);
            binding.btnConfirm.setClickable(true);
            binding.btnConfirm.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenlightbg2));
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_green));

        }else{

            binding.btnConfirm.setEnabled(false);
            binding.btnConfirm.setClickable(false);
            binding.btnConfirm.setFocusable(false);
            binding.btnConfirm.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey1));
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_green));
        }
    }

}