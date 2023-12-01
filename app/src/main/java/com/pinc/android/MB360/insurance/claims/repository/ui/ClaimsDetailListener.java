package com.pinc.android.MB360.insurance.claims.repository.ui;

import com.pinc.android.MB360.insurance.claims.repository.responseclass.ClaimsInfo;

public interface ClaimsDetailListener {
    void onClaimsClickedListener(int position, ClaimsInfo intimatedClaim);
}
