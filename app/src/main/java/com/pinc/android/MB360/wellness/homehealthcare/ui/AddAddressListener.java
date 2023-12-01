package com.pinc.android.MB360.wellness.homehealthcare.ui;

import com.pinc.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;

public interface AddAddressListener {
    void getMember(FamilyMember familyMember);

    void selectPackage(FamilyMember familyMember);
}
