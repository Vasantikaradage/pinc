package com.pinc.android.MB360.insurance.profile.ui;

import com.pinc.android.MB360.insurance.profile.response.UserDocumentsDetail;

public interface DocumentOnClickListener {
    void onDocumentClicked(UserDocumentsDetail document);

    void onDocumentUploadClick(String docType);

}
