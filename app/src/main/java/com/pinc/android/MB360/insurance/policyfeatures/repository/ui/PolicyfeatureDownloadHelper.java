package com.pinc.android.MB360.insurance.policyfeatures.repository.ui;

import com.pinc.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponse;

public interface PolicyfeatureDownloadHelper {
    void onStartDownload(int position);

    void onFinishDownload(int position);

    void requestPermission(int position, PolicyFeaturesOuterModel policyFeaturesOuterModel);
}
