package com.pinc.android.MB360.database.converters.PolicyFeatureConverter;

import androidx.room.TypeConverter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinc.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponseOffline;

import java.lang.reflect.Type;
import java.util.List;

public class PolicyFeatureResponseConverter {

    @TypeConverter
    public static PolicyFeaturesResponseOffline toPolicyFeatureResponse(String value) {
        Gson gson = new Gson();
    /*    Type listType = new TypeToken<PolicyFeaturesResponse>() {
        }.getType();
        value = "{" + value + "}";
        return new Gson().fromJson(value, listType);*/

        return gson.fromJson(value, PolicyFeaturesResponseOffline.class);
    }

    @TypeConverter
    public static String toString(PolicyFeaturesResponseOffline policyFeaturesResponseOffline) {
        Gson gson = new Gson();
        String json = gson.toJson(policyFeaturesResponseOffline);
        return json;
    }

}
