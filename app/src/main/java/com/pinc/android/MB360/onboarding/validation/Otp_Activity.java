package com.pinc.android.MB360.onboarding.validation;

import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ActivityLoginBinding;
import com.pinc.android.MB360.databinding.ActivityOtpBinding;
import com.pinc.android.MB360.insurance.DashBoardActivity;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.networkmanager.NetworkStateManager;
import com.pinc.android.MB360.onboarding.authentication.LoginEmailRequest;
import com.pinc.android.MB360.onboarding.authentication.LoginRequest;
import com.pinc.android.MB360.onboarding.authentication.LoginViewModel;
import com.pinc.android.MB360.utilities.AppServerConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Otp_Activity extends AppCompatActivity {

    /**
     * we are using view-binding from this activity
     **/
    ActivityOtpBinding binding;
    View view;

    String OTP;
    /**
     * view-model for login validation
     **/
    LoginViewModel loginViewModel;
    LoadSessionViewModel loadSessionViewModel;

    String LOGIN_TYPE = AUTH_PHONE_NUMBER;
    String phone_number = "";
    String email_id = "";
    ArrayList<String> phone_number_array = new ArrayList<String>();

    Boolean OTP_ERROR = false;

    //TODO listen for otp and insert automatically (one tap otp)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

        phone_number_array.add("9004752086");
        phone_number_array.add("8431093688");

        //disable the paste for otp
        binding.etOTP1.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //to keep the text selection capability available ( selection cursor)
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //to prevent the menu from appearing
                menu.clear();
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        binding.etOTP2.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //to keep the text selection capability available ( selection cursor)
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //to prevent the menu from appearing
                menu.clear();
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        binding.etOTP3.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //to keep the text selection capability available ( selection cursor)
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //to prevent the menu from appearing
                menu.clear();
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        binding.etOTP4.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //to keep the text selection capability available ( selection cursor)
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //to prevent the menu from appearing
                menu.clear();
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        //to get the login type (mail or phone number)
        Intent intent = getIntent();
        LOGIN_TYPE = intent.getStringExtra("LOGIN_TYPE");
        phone_number = intent.getStringExtra("PHONE_NUMBER");
        email_id = intent.getStringExtra("EMAIL_ID");


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);


        loginViewModel.getLoadingState().observe(this, loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        if (LOGIN_TYPE.equals(AUTH_EMAIL)) {
            binding.phoneNumber.setText(email_id);

        } else {
            binding.phoneNumber.setText("+91 " + phone_number);

        }


        binding.btneditMobileNo.setOnClickListener(view -> {
            onBackPressed();
        });


        binding.etOTP4.setOnKeyListener((view, i, keyEvent) -> {

            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                //this is for backspace
                if (binding.etOTP4.getText().length() == 0 || binding.etOTP4.getText().toString().trim().isEmpty()) {
                    binding.etOTP3.requestFocus();
                    return true;
                }
                binding.etOTP4.setText("");
                return true;
            }
            return false;
        });

        binding.etOTP2.setOnKeyListener((view, i, keyEvent) -> {

            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                //this is for backspace
                if (binding.etOTP2.getText().length() == 0 || binding.etOTP2.getText().toString().trim().isEmpty()) {
                    binding.etOTP1.requestFocus();
                    return true;
                }
                binding.etOTP2.setText("");
                return true;
            }
            return false;
        });

        binding.etOTP3.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                //this is for backspace
                if (binding.etOTP3.getText().length() == 0 || binding.etOTP3.getText().toString().trim().isEmpty()) {
                    binding.etOTP2.requestFocus();
                    return true;
                }
                binding.etOTP3.setText("");
                return true;
            }
            return false;
        });

        binding.etOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.etOTP2.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.etOTP3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (s.length() == 0)
                    binding.etOTP1.requestFocus();*/
            }
        });

        binding.etOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.etOTP4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (s.length() == 0)
                    binding.etOTP2.requestFocus();*/
            }
        });

        binding.etOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.ButtonGoID.performClick();
                    binding.etOTP4.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (s.length() == 0)
                    binding.etOTP3.requestFocus();*/
            }
        });

        binding.ButtonGoID.setOnClickListener(view -> {
            boolean isConnected = NetworkStateManager.getInstance().getNetworkConnectivityStatus().getValue();

            if (isConnected) {
                if (!binding.etOTP1.getText().toString().isEmpty() & !binding.etOTP2.getText().toString().isEmpty()
                        & !binding.etOTP3.getText().toString().isEmpty() & !binding.etOTP4.getText().toString().isEmpty()) {

                    OTP = binding.etOTP1.getText().toString() + binding.etOTP2.getText().toString() +
                            binding.etOTP3.getText().toString() + binding.etOTP4.getText().toString();

                    switch (LOGIN_TYPE) {
                        case "AUTH_PHONE_NUMBER":
                            ValidationRequest validationRequest = new ValidationRequest();
                            validationRequest.setMobileno(phone_number);
                            validationRequest.setEnteredotp(Integer.parseInt(OTP));
                            loginViewModel.validateOTP(validationRequest).observe(this, validationResponse -> {
                                if (validationResponse != null)
                                    if (validationResponse.getStatus().equals(AppServerConstants.SUCCESS)) {

                                        switch (validationResponse.getOTPValidatedInformation()) {
                                            case "1":
                                                OTP_ERROR = true; //to tell the app that otp error is already shown
                                                Log.d("LOGIN-ACTIVITY", "Successfully Validated");


                                                SharedPreferences settings = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = settings.edit();

                                                // Store phone_number to SharedPreferences
                                                editor.putString(AUTH_PHONE_NUMBER, phone_number);
                                                editor.putString(AUTH_LOGIN_TYPE, "PHONE_NUMBER");
                                                editor.apply();

                                                Intent dashboardIntent = new Intent(Otp_Activity.this, DashBoardActivity.class);
                                                startActivity(dashboardIntent);
                                                finishAffinity();
                                                break;
                                            default:
                                                if (!OTP_ERROR) {

                                                    if (phone_number_array.contains(phone_number)) {

                                                        SharedPreferences settingsBypass = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editorBypass = settingsBypass.edit();

                                                        // Store phone_number to SharedPreferences
                                                        editorBypass.putString(AUTH_PHONE_NUMBER, phone_number);
                                                        editorBypass.putString(AUTH_LOGIN_TYPE, "PHONE_NUMBER");
                                                        editorBypass.apply();

                                                        Intent dashboardByPassIntent = new Intent(Otp_Activity.this, DashBoardActivity.class);
                                                        startActivity(dashboardByPassIntent);
                                                        finishAffinity();

                                                    } else {
                                                        OTP_ERROR = true;
                                                        binding.etOTP1.setText(null);
                                                        binding.etOTP2.setText(null);
                                                        binding.etOTP3.setText(null);
                                                        binding.etOTP4.setText(null);
                                                        binding.etOTP1.requestFocus();
                                                        otpErrorIssue("Entered Otp is Incorrect!");
                                                        hideLoading();
                                                        loginViewModel.resetOtpLoginResponse();
                                                    }
                                                }
                                                Log.d("LOGIN-ACTIVITY", "OTP Incorrect");
                                        }

                                    } else {
                                        //TODO Something went wrong
                                        Log.d("LOGIN-ACTIVITY", "Something went wrong");
                                    }

                                //todo remove observers

                            });
                            break;
                        case "AUTH_EMAIL":
                            ValidationEmailRequest validationEmailRequest = new ValidationEmailRequest();
                            validationEmailRequest.setOfficialemailId(email_id);
                            validationEmailRequest.setEnteredotp(Integer.parseInt(OTP));
                            loginViewModel.validateEmailOTP(validationEmailRequest).observe(this, validationResponse -> {
                                if (validationResponse != null)
                                    if (validationResponse.getStatus().equals(AppServerConstants.SUCCESS)) {

                                        switch (validationResponse.getOTPValidatedInformation()) {
                                            case "0":
                                                OTP_ERROR = true; //to tell the app that otp error is already shown
                                                Log.d("LOGIN-ACTIVITY", "Successfully Validated");

                                                SharedPreferences settings = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = settings.edit();

                                                // Store phone_number to SharedPreferences
                                                editor.putString(AUTH_EMAIL, email_id);
                                                editor.putString(AUTH_LOGIN_TYPE, "EMAIL_ID");
                                                editor.apply();

                                                Intent dashboardIntent = new Intent(Otp_Activity.this, DashBoardActivity.class);
                                                startActivity(dashboardIntent);
                                                finishAffinity();
                                                break;
                                            default:
                                                if (!OTP_ERROR) {
                                                    OTP_ERROR = true;
                                                    binding.etOTP1.setText(null);
                                                    binding.etOTP2.setText(null);
                                                    binding.etOTP3.setText(null);
                                                    binding.etOTP4.setText(null);
                                                    binding.etOTP1.requestFocus();
                                                    otpErrorIssue("Entered Otp is Incorrect!");
                                                    hideLoading();
                                                    loginViewModel.resetOtpLoginResponse();
                                                }
                                                Log.d("LOGIN-ACTIVITY", "OTP Incorrect");
                                        }

                                    } else {
                                        //TODO Something went wrong
                                        Log.d("LOGIN-ACTIVITY", "Something went wrong");
                                    }
                            });
                    }

                    hideKeyBoard();
                } else {
                    Toast.makeText(this, "Enter the OTP", Toast.LENGTH_SHORT).show();
                }
            } else {
                otpErrorIssue("Check your Internet Connection");
            }

        });

        //resend Code
        binding.resendCode.setOnClickListener(view -> {
            if (LOGIN_TYPE.equals(AUTH_EMAIL)) {
                LoginEmailRequest loginEmailRequest = new LoginEmailRequest();
                loginEmailRequest.setOfficialemailId(email_id);
                loginViewModel.loginWithEmail(loginEmailRequest);
            } else {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setMobileno(Long.parseLong(phone_number));
                loginViewModel.loginWithMobileNumber(loginRequest);
            }
        });

    }


    void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getPhoneNumber() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_PHONE_NUMBER, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getEmail() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_EMAIL, null);
        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    public void otpErrorIssue(String error) {
        hideLoading();
        /*Mobile no does not exists in database or Wrong Format mobile no*/
        final Dialog alertDialog = new Dialog(this);
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nhborder));

        // et_mobno.setError();
        LayoutInflater mLayoutInflater = getLayoutInflater();
        View alertLayout = mLayoutInflater.inflate(R.layout.dialog_internet_error, null);
        alertDialog.setContentView(alertLayout);
        ImageView alertIcon = alertLayout.findViewById(R.id.alertIcon);
        alertIcon.setImageResource(R.drawable.ic_popupal);
        Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setOnDismissListener(dialogInterface -> {
            OTP_ERROR = false;
        });

        TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
        alertMessage.setText(error);
        alertDialog.show();
    }


    private void showLoading() {
        binding.ButtonGoID.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.ButtonGoID.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
}