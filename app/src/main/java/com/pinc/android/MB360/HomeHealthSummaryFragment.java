package com.pinc.android.MB360;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinc.android.MB360.databinding.FragmentHomeHealthSummaryBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;


import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;

public class HomeHealthSummaryFragment extends Fragment {

    FragmentHomeHealthSummaryBinding binding;
    View view;

    LoadSessionViewModel loadSessionViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    NavController navController;


    public HomeHealthSummaryFragment() {
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
        binding = FragmentHomeHealthSummaryBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();


        //setCity
        binding.txtCityName.setText((homeHealthCareViewModel.getSelectedCity().getValue().getCity() == null) ? "Mumbai" : homeHealthCareViewModel.getSelectedCity().getValue().getCity());
//        binding.txtCityName.setText("Mumbai");

        //set person info
        FamilyMember person = homeHealthCareViewModel.getSelectedPerson().getValue();
        binding.txtDependantName.setText(person.getPersonName());
        String personage = person.getRelationName() + "(" + person.getAge() + " years)";
        binding.txtRelationAge.setText(personage);


        //Selected dates logic


        //selected packages trained attendant
        homeHealthCareViewModel.getTrainedAttendantPackage().observe(getViewLifecycleOwner(), homeHealthCarePackage -> {
            //selected package details
            if (homeHealthCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {
                    case "TRAINED ATTENDANT":
                        binding.catLabel.setVisibility(View.GONE);
                        binding.txtCatValue.setVisibility(View.GONE);

                        binding.hourValue.setText(homeHealthCarePackage.getHhcNaHours());
                        binding.durationValue.setText(homeHealthCarePackage.getHhcNaDurations());
                        binding.nursingCountValue.setText(homeHealthCarePackage.getHhcNaNacount());

                        Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                        if (appointment != null) {

                            if (appointment.getDate_condition() == 1) {
                                //daily
                                binding.txtNoMonthLabel.setText("No. of Days");
                                binding.txtDuration.setText(appointment.getDate_preference());

                            } else if (appointment.getDate_condition() == 2) {
                                //Monthly
                                binding.txtNoMonthLabel.setText("No. of Months");
                                //todo calculate the better dates to show
                                binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                            } else if (appointment.getDate_condition() == 3) {
                                //date range
                                binding.txtNoMonthLabel.setText("No. of Months");
                                binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                            }
                            binding.txtNoMonthValue.setText(appointment.getCount());

                        }

                        //calculation for amount
                        try {
                            //amount calculations
                            binding.textPackageRate.setText(UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
                            if (appointment.getCount() != null) {
                                int count = Integer.parseInt(appointment.getCount());
                                double total = Integer.parseInt(homeHealthCarePackage.getPkgPriceMb()) * count;
                                binding.textTotalAmount.setText(UtilMethods.PriceFormat(String.valueOf(total)));
                            } else {
                                binding.btnSchedule.setEnabled(false);
                                binding.textTotalAmount.setText("-");
                                binding.txtNoMonthValue.setText("-");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        break;
                    default:
                        binding.btnSchedule.setEnabled(false);
                }
            }

        });

        //selected packages long Term nursing
        homeHealthCareViewModel.getLongtermNursingPackage().observe(getViewLifecycleOwner(), homeHealthCarePackage -> {
            //selected package details
            if (homeHealthCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {
                    case "LONG TERM NURSING":
                        binding.catLabel.setVisibility(View.VISIBLE);
                        binding.txtCatValue.setVisibility(View.VISIBLE);
                        binding.txtCatValue.setText(homeHealthCarePackage.getHhcLtCategory());

                        binding.hourValue.setText(homeHealthCarePackage.getHhcLtHours());
                        binding.durationValue.setText(homeHealthCarePackage.getHhcLtDurations());
                        binding.nursingCountValue.setText(homeHealthCarePackage.getHhcLtNacount());

                        Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                        if (appointment != null) {

                            if (appointment.getDate_condition() == 1) {
                                //daily
                                binding.txtNoMonthLabel.setText("No. of Days");
                                binding.txtDuration.setText(appointment.getDate_preference());

                            } else if (appointment.getDate_condition() == 2) {
                                //Monthly
                                binding.txtNoMonthLabel.setText("No. of Months");
                                //todo calculate the better dates to show
                                binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                            } else if (appointment.getDate_condition() == 3) {
                                //date range
                                binding.txtNoMonthLabel.setText("No. of Months");
                                binding.txtDuration.setText(appointment.getFrom_date() + "\n           To           \n" + appointment.getTo_date());
                            }
                            binding.txtNoMonthValue.setText(appointment.getCount());

                        }

                        //calculation for amount
                        try {
                            //amount calculations
                            binding.textPackageRate.setText(UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
                            if (appointment.getCount() != null) {

                                int count = Integer.parseInt(appointment.getCount());
                                double total = Integer.parseInt(homeHealthCarePackage.getPkgPriceMb()) * count;
                                binding.textTotalAmount.setText(UtilMethods.PriceFormat(String.valueOf(total)));
                            } else {

                                binding.btnSchedule.setEnabled(false);
                                binding.textTotalAmount.setText("-");
                                binding.txtNoMonthValue.setText("-");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        binding.btnSchedule.setEnabled(false);
                }
            }

        });

        //selected packages short Term nursing
        homeHealthCareViewModel.getShorttermNursingPackage().observe(getViewLifecycleOwner(), homeHealthCarePackage -> {
            //selected package details
            if (homeHealthCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {

                    case "SHORT TERM NURSING":

                        binding.hourValue.setVisibility(View.GONE);
                        binding.HoursLayout.setVisibility(View.GONE);
                        binding.durationValue.setVisibility(View.GONE);
                        binding.durationLabel.setVisibility(View.GONE);
                        binding.nursingCountValue.setVisibility(View.GONE);
                        binding.nursingCountLabel.setVisibility(View.GONE);
                        binding.txtNoMonthValue.setVisibility(View.GONE);
                        binding.txtNoMonthLabel.setVisibility(View.GONE);
                        binding.viewline.setVisibility(View.GONE);

                        Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                        if (appointment != null) {

                            binding.txtCatValue.setText(""+appointment.getService());
                            binding.txtDuration.setText(""+appointment.getDate_preference());
//
                        }

                        //calculation for amount
                        try {
                            //amount calculations
                            binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
                            binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
//                            if (appointment.getCount() != null) {
//                                int count = Integer.parseInt(appointment.getCount());
//                                double total = Integer.parseInt(homeHealthCarePackage.getPkgPriceMb()) * count;
//                                binding.textTotalAmount.setText(UtilMethods.PriceFormat(String.valueOf(total)));
//                            } else {
//                                binding.btnSchedule.setEnabled(false);
//                                binding.textTotalAmount.setText("-");
//                                binding.txtNoMonthValue.setText("-");
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        binding.btnSchedule.setEnabled(false);
                }
            }

        });

        //selected packages Doctor Services
        homeHealthCareViewModel.getDoctorServicesPackage().observe(getViewLifecycleOwner(), homeHealthCarePackage -> {
            //selected package details
            if (homeHealthCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {
                    case "DOCTOR SERVICES":
//                        binding.catLabel.setVisibility(View.GONE);
                        binding.txtCatValue.setVisibility(View.VISIBLE);
                        binding.txtCatValue.setText("" + homeHealthCarePackage.getCategory());


                        binding.hourValue.setVisibility(View.GONE);
                        binding.HoursLayout.setVisibility(View.GONE);
                        binding.durationValue.setVisibility(View.GONE);
                        binding.durationLabel.setVisibility(View.GONE);
                        binding.nursingCountValue.setVisibility(View.GONE);
                        binding.nursingCountLabel.setVisibility(View.GONE);
                        binding.txtNoMonthValue.setVisibility(View.GONE);
                        binding.txtNoMonthLabel.setVisibility(View.GONE);
                        binding.viewline.setVisibility(View.GONE);


                        Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                        if (appointment != null) {

                            binding.txtDuration.setText(""+appointment.getDate_preference());
                        }

                        //calculation for amount
                        try {
                            //amount calculations
                            binding.textPackageRate.setText(UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
                            binding.textTotalAmount.setText(UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
//                            if (appointment.getCount() != null) {
//                                int count = Integer.parseInt(appointment.getCount());
//                                double total = Integer.parseInt(homeHealthCarePackage.getPkgPriceMb()) * count;
//                                binding.textTotalAmount.setText(UtilMethods.PriceFormat(String.valueOf(total)));
//                            } else {
//                            binding.btnSchedule.setEnabled(false);
//                            binding.textTotalAmount.setText("-");
//                            binding.txtNoMonthValue.setText("-");
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        binding.btnSchedule.setEnabled(false);
                }
            }

        });

        //selected packages physiotherapy
        homeHealthCareViewModel.getPhysiotherapyPackage().observe(getViewLifecycleOwner(), homeHealthCarePackage -> {
            //selected package details
            if (homeHealthCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {
                    case "PHYSIOTHERAPY":
//                        binding.catLabel.setVisibility(View.GONE);

                        binding.hourValue.setVisibility(View.GONE);
                        binding.HoursLayout.setVisibility(View.GONE);
                        binding.durationValue.setVisibility(View.GONE);
                        binding.durationLabel.setVisibility(View.GONE);
                        binding.nursingCountValue.setVisibility(View.GONE);
                        binding.nursingCountLabel.setVisibility(View.GONE);
                        binding.txtNoMonthValue.setVisibility(View.GONE);
                        binding.txtNoMonthLabel.setVisibility(View.GONE);
                        binding.viewline.setVisibility(View.GONE);

                        binding.txtCatValue.setVisibility(View.VISIBLE);
                        binding.txtCatValue.setText("" + homeHealthCarePackage.getCategory());

//                        binding.hourValue.setText(homeHealthCarePackage.getHhcLtHours());
//                        binding.durationValue.setText(homeHealthCarePackage.getHhcLtDurations());
//                        binding.nursingCountValue.setText(homeHealthCarePackage.getHhcLtNacount());
//
                        Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                        if (appointment != null) {

                            if (appointment.getDate_condition() == 1) {
                                //daily
//                                binding.txtNoMonthLabel.setText("No. of Days");
                                binding.txtDuration.setText(appointment.getDate_preference());

                            } else if (appointment.getDate_condition() == 2) {
                                //Monthly
//                                binding.txtNoMonthLabel.setText("No. of Months");
                                //todo calculate the better dates to show
                                binding.txtDuration.setText(appointment.getFrom_date() + "\n   To       \n" + appointment.getTo_date());
                            }

                        }

                        //calculation for amount
                        try {
                            //amount calculations
                            binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
                            binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
//                            if (appointment.getCount() != null) {
//                                int count = Integer.parseInt(appointment.getCount());
//                                double total = Integer.parseInt(homeHealthCarePackage.getPkgPriceMb()) * count;
//                                binding.textTotalAmount.setText(UtilMethods.PriceFormat(String.valueOf(total)));
//                            } else {
//                            binding.btnSchedule.setEnabled(false);
//                            binding.textTotalAmount.setText("-");
//                            binding.txtNoMonthValue.setText("-");
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        binding.btnSchedule.setEnabled(false);
                }
            }

        });

        //selected packages Diabetes Management
        homeHealthCareViewModel.getDiabetesManagementPackage().observe(getViewLifecycleOwner(), homeHealthCarePackage -> {
            //selected package details
            if (homeHealthCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {
                    case "DIABETES MANAGEMENT":
                        binding.catLabel.setVisibility(View.GONE);
                        binding.txtCatValue.setVisibility(View.GONE);
//                        binding.txtCatValue.setText("" + homeHealthCarePackage.getCategory());

                        binding.hourValue.setVisibility(View.GONE);
                        binding.HoursLayout.setVisibility(View.GONE);
                        binding.durationValue.setVisibility(View.GONE);
                        binding.durationLabel.setVisibility(View.GONE);
                        binding.nursingCountValue.setVisibility(View.GONE);
                        binding.nursingCountLabel.setVisibility(View.GONE);
                        binding.txtNoMonthValue.setVisibility(View.GONE);
                        binding.txtNoMonthLabel.setVisibility(View.GONE);
                        binding.viewline.setVisibility(View.GONE);


                        Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                        if (appointment != null) {

//                            binding.txtDuration.setText(""+appointment.getDate_preference());
                            binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(appointment.getPrice()));
                            binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(appointment.getPrice()));
                        }

                        //calculation for amount
                        try {
                            //amount calculations
//                            binding.textPackageRate.setText(UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
//                            binding.textTotalAmount.setText(UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
//
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        binding.btnSchedule.setEnabled(false);
                }
            }

        });


        //selected packages post natal
        homeHealthCareViewModel.getPostNatalPackage().observe(getViewLifecycleOwner(), homeHealthCarePackage -> {
            //selected package details
            if (homeHealthCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {
                    case "POST NATAL CARE":
//                        binding.catLabel.setVisibility(View.GONE);
                        binding.txtCatValue.setVisibility(View.VISIBLE);
                        binding.txtCatValue.setText("" + homeHealthCarePackage.getCategory());

                        binding.hourValue.setVisibility(View.GONE);
                        binding.HoursLayout.setVisibility(View.GONE);
                        binding.durationValue.setVisibility(View.GONE);
                        binding.durationLabel.setVisibility(View.GONE);
                        binding.nursingCountValue.setVisibility(View.GONE);
                        binding.nursingCountLabel.setVisibility(View.GONE);
                        binding.txtNoMonthValue.setVisibility(View.GONE);
                        binding.txtNoMonthLabel.setVisibility(View.GONE);
                        binding.viewline.setVisibility(View.GONE);

//                        binding.hourValue.setText(homeHealthCarePackage.getHhcLtHours());
//                        binding.durationValue.setText(homeHealthCarePackage.getHhcLtDurations());
//                        binding.nursingCountValue.setText(homeHealthCarePackage.getHhcLtNacount());
//
                        Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                        if (appointment != null) {

                            if (appointment.getDate_condition() == 1) {
                                //daily
//                                binding.txtNoMonthLabel.setText("No. of Days");
                                binding.txtDuration.setText(appointment.getDate_preference());

                            } else if (appointment.getDate_condition() == 2) {
                                //Monthly
//                                binding.txtNoMonthLabel.setText("No. of Months");
                                //todo calculate the better dates to show
                                binding.txtDuration.setText(appointment.getFrom_date() + "\n   To       \n" + appointment.getTo_date());
                            } else if (appointment.getDate_condition() == 3) {
                                //date range
//                                binding.txtNoMonthLabel.setText("No. of Months");
                                binding.txtDuration.setText(appointment.getFrom_date() + "\n   To       \n" + appointment.getTo_date());
                            }
//                            binding.txtNoMonthValue.setText(appointment.getCount());

                        }

                        //calculation for amount
                        try {
                            //amount calculations
                            binding.textPackageRate.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
                            binding.textTotalAmount.setText("\u20B9 " + UtilMethods.PriceFormat(homeHealthCarePackage.getPkgPriceMb()));
//                            if (appointment.getCount() != null) {
//                                int count = Integer.parseInt(appointment.getCount());
//                                double total = Integer.parseInt(homeHealthCarePackage.getPkgPriceMb()) * count;
//                                binding.textTotalAmount.setText(UtilMethods.PriceFormat(String.valueOf(total)));
//                            } else {
//                            binding.btnSchedule.setEnabled(false);
//                            binding.textTotalAmount.setText("-");
//                            binding.txtNoMonthValue.setText("-");
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        binding.btnSchedule.setEnabled(false);
                }
            }

        });

        //selected eldercare
        homeHealthCareViewModel.getElderCarePackage().observe(getViewLifecycleOwner(), elderCarePackage -> {

            if (elderCarePackage != null) {

                switch (homeHealthCareViewModel.getService()) {
                    case "ELDER CARE":
                        binding.txtCatValue.setVisibility(View.VISIBLE);
                        binding.txtCatValue.setText("" + elderCarePackage.getCategory());
                        binding.textPackageRate.setText("" + requireContext().getString(R.string.rs) + UtilMethods.PriceFormat(elderCarePackage.getPkgPriceMb()));

                        binding.nursingCountLayout.setVisibility(View.GONE);
                        binding.HoursLayout.setVisibility(View.GONE);
                        binding.durationLayout.setVisibility(View.GONE);
                        binding.packageDurationLayout.setVisibility(View.GONE);
                        binding.textTotalAmount.setText("" + requireContext().getString(R.string.rs) + UtilMethods.PriceFormat(elderCarePackage.getPkgPriceMb()));

                        break;
                }

            }

        });


        //appointment (queries related)
        binding.btnSchedule.setOnClickListener(v -> {
            String remark = binding.remarkEditText.getText().toString();


            switch (homeHealthCareViewModel.getService()) {
                case "TRAINED ATTENDANT":
                    Appointment appointment = homeHealthCareViewModel.getAppointmentRequest().getValue();
                    appointment.setRemarks(remark);
                    homeHealthCareViewModel.scheduleAppointment(appointment).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {


                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.TRAINED_ATTENDANT, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "LONG TERM NURSING":

                    homeHealthCareViewModel.scheduleAppointmentLT(homeHealthCareViewModel.getAppointmentRequest().getValue()).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.LONGTERM_NURSING, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "SHORT TERM NURSING":

                    homeHealthCareViewModel.scheduleAppointmentST(homeHealthCareViewModel.getAppointmentRequest().getValue()).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.SHORTTERM_NURSING, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "DOCTOR SERVICES":

                    homeHealthCareViewModel.scheduleAppointmentDS(homeHealthCareViewModel.getAppointmentRequest().getValue()).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.DOCTOR_SERVICES, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;


                case "PHYSIOTHERAPY":

                    homeHealthCareViewModel.scheduleAppointmentPT(homeHealthCareViewModel.getAppointmentRequest().getValue()).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.PHYSIOTHERAPY, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "DIABETES MANAGEMENT":

                    homeHealthCareViewModel.scheduleAppointmentDM(homeHealthCareViewModel.getAppointmentRequest().getValue()).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.DIABETES_MANAGEMENT_ACTIVITY, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;


                case "POST NATAL CARE":

                    homeHealthCareViewModel.scheduleAppointmentNC(homeHealthCareViewModel.getAppointmentRequest().getValue()).observe(getViewLifecycleOwner(), messageResponse -> {

                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.POST_NATAL, "onSchedule: ERROR OCCURRED");

                        }

                    });

                    break;

                case "ELDER CARE":
                    homeHealthCareViewModel.scheduleAppointmentEC(homeHealthCareViewModel.getAppointmentRequest().getValue()).observe(getViewLifecycleOwner(), messageResponse -> {
                        if (messageResponse != null) {

                            if (messageResponse.getMessage().getStatus()) {

                                final Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.schedule_status);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);

                                TextView tvrescheduletext = dialog.findViewById(R.id.msgStatus);
                                tvrescheduletext.setText("Appointment booked Successfully!");
                                Button button = dialog.findViewById(R.id.done);

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                    //navigate
                                    NavDirections navDirections = HomeHealthSummaryFragmentDirections.actionHomeHealthSummaryFragmentToOngoingFragment();
                                    navController.navigate(navDirections);
                                });

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        homeHealthCareViewModel.resetMessageResponse();
                                    }
                                });

                                dialog.show();

                            } else {

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
                                tvrescheduletext.setText(messageResponse.getMessage().getMessage());
                                Button button = dialog.findViewById(R.id.done);
                                button.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare_red));

                                button.setOnClickListener(view -> {
                                    dialog.dismiss();
                                });

                                dialog.show();

                            }

                        } else {
                            Log.e(LogTags.ELDER_CARE, "onSchedule: ERROR OCCURRED");

                        }
                    });
            }

        });

        return view;
    }
}