package com.pinc.android.MB360.insurance.enrollment.interfaces;

import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;

public interface DependantListener {
    void onDependantSavedListener(DependantHelperModel dependant, int position);

    abstract void onTwinsAdded(DependantHelperModel twin1, DependantHelperModel twin2, int position,boolean edit);

    void onDependantEditedListener(DependantHelperModel dependant);

    void onDependantDeletedListener(DependantHelperModel dependant);
}
