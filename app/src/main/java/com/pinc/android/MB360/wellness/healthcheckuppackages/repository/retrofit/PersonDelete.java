package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit;

import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;

public interface PersonDelete {
     void personDelete(int packagePosition,String personSrNo, int position, Person person);
}
