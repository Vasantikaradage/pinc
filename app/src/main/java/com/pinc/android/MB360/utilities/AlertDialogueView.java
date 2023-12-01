package com.pinc.android.MB360.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.AlertDialogBinding;
import com.pinc.android.MB360.databinding.PolicyChangeDialogueBinding;

public class AlertDialogueView {

    Context context;
    Activity activity;
    AlertDialogBinding binding;
    private AlertDialog view;

    public AlertDialogueView(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void showAlert(String alert) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        binding = AlertDialogBinding.inflate(LayoutInflater.from(activity.getApplicationContext()));

        builder.setView(binding.getRoot());
        builder.setCancelable(true);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wmlp = view.getWindow().getAttributes();
        wmlp.windowAnimations = com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        view.show();


        binding.alertIcon.setImageResource(R.drawable.ic_popupal);
        binding.btnDismissText.setText("Okay!");
        binding.tvAlertMessage.setText(alert);

        binding.btnDismiss.setOnClickListener(view1 -> view.dismiss());

    }


}
