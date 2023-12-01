package com.pinc.android.MB360.insurance.utilities.ui;

import com.pinc.android.MB360.insurance.utilities.repository.responseclass.UTILITIESDATum;

public interface UtilDownloadHelper {
    void onUtilitiesClicked(int position);

    void onStartDownload(int position);

    void onFinishDownload(int position);

    void requestPermission(int position, UTILITIESDATum utility);

}
