package com.pinc.android.MB360.database.converters.cliamProcedureConverters;

import androidx.room.TypeConverter;

;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcLayoutInfo;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.Message;

import java.lang.reflect.Type;
import java.util.List;

public class ClaimProcedureLayoutInfoConverter {

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
    public static List<ClaimProcLayoutInfo> toList(String value) {
        Type listType = new TypeToken<List<ClaimProcLayoutInfo>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<ClaimProcLayoutInfo> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
