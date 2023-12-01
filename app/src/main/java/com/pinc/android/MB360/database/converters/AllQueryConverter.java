package com.pinc.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinc.android.MB360.insurance.queries.responseclass.AllQuery;
import com.pinc.android.MB360.insurance.queries.responseclass.Message;
import com.pinc.android.MB360.insurance.queries.responseclass.TotalRecords;

import java.lang.reflect.Type;
import java.util.List;

public class AllQueryConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static Message stringToMessage(String message) {
        return gson.fromJson(message, Message.class);
    }

    @TypeConverter
    public static String messageToString(Message message) {

        return gson.toJson(message);
    }


    @TypeConverter
    public static TotalRecords stringToTotalRecords(String totalRecords) {
        return gson.fromJson(totalRecords, TotalRecords.class);
    }

    @TypeConverter
    public static String totalRecordToString(TotalRecords totalRecords) {
        return gson.toJson(totalRecords);
    }



    @TypeConverter
    public static List<AllQuery> toList(String value) {
        Type listType = new TypeToken<List<AllQuery>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<AllQuery> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
