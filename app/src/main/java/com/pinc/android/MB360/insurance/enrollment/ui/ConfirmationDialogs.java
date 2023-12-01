package com.pinc.android.MB360.insurance.enrollment.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.pinc.android.MB360.R;

public class ConfirmationDialogs extends DialogFragment {
    private AppCompatButton confirm_button, reject_button;
    private DialogActions dialogActions;
    private Context context;
    private int position;
    private AppCompatTextView appCompatTextView;
    private String finalText = "";

    public ConfirmationDialogs(Context cont, DialogActions dialogActions, int position){
        context = cont;
        this.dialogActions = dialogActions;
        this.position = position;
    }

    public ConfirmationDialogs(Context cont, DialogActions dialogActions,  String Text){
        context = cont;
        this.dialogActions = dialogActions;
        this.finalText = Text;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_confirm_dialog, container, false);

        confirm_button = v.findViewById(R.id.confirm_button);
        reject_button = v.findViewById(R.id.reject_button);
        appCompatTextView = v.findViewById(R.id.appCompatTextView);

        getActivity().getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.nhborder));

        confirm_button.setOnClickListener(v1 -> {
            dialogActions.onConfirmed(position);
            dismiss();
        });

        reject_button.setOnClickListener(v1 -> {
            dialogActions.onRejected(position);
            dismiss();
        });

        DisplayMetrics dm = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getDialog().getWindow().setLayout((int) (width * .4), (int) (height * .3));

        if(!TextUtils.isEmpty(finalText))
            appCompatTextView.setText(finalText);

        return v;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
       // dialogActions.onRejected(position);
    }

    public interface DialogActions {
        void onConfirmed(int position);
        void onRejected(int position);
    }
}
