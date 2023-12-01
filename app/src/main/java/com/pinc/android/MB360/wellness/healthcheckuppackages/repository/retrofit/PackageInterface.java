package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit;

import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;

public interface PackageInterface {

    void getPackageDetails(Packages packages);

    void scheduleAppointment(Packages packages, Person person);

//    delete function

//    void deletePerson(String personSrNo);

}
