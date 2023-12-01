package com.pinc.android.MB360.utilities;

import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;
import static com.pinc.android.MB360.utilities.AppLocalConstant.USER_AGREEMENT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.onboarding.SplashScreenActivity;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class UtilMethods {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    public static final String DASH_STRING = "/";


    public static String PriceFormat(String price) {
        try {
            Double priceDouble = Double.parseDouble(price);
            NumberFormat formatter = new DecimalFormat(AppLocalConstant.NUMBER_FORMAT);
            return formatter.format(priceDouble);
        } catch (NumberFormatException exception) {
            if (!price.contains(",")) {
                Log.e(LogTags.COMMA_CONVERSION, "PriceFormat: ", exception);
            }
            return price;
        }

    }

    public static String getAge(String DOB) {

        int mtodayYear, mtodayMonth, mtodayDay;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        Date theDate = null;
        try {
            theDate = format.parse(DOB);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("Exception", e.toString());
        }

        Calendar myCal = new GregorianCalendar();
        myCal.setTime(theDate);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

//        String data[] = todaysdate.split("/");
//        mtodayYear = Integer.parseInt(data[2]);
//        mtodayMonth = Integer.parseInt(data[1]);
//        mtodayDay = Integer.parseInt(data[0]);

//        dob.set(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH) + 1, myCal.get(Calendar.DAY_OF_MONTH));
//        today.set(mtodayYear, mtodayMonth, mtodayDay);

        int age = today.get(Calendar.YEAR) - myCal.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < myCal.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static Calendar parseDateString(String date) {
        Calendar calendar = Calendar.getInstance();
        DATE_FORMAT.setLenient(false);
        try {
            calendar.setTime(DATE_FORMAT.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    public static boolean isValidDate(String birthday) {
        Calendar calendar = parseDateString(birthday);
        int year = calendar.get(Calendar.YEAR);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        return year >= thisYear;
    }


    public static Boolean standardDateValidate(String date) {
        if (date == null || !date.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$"))
            return false;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static void stripUnderlines(AppCompatTextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

    }

    public static void makeLinks(TextView textView, List<Pair<String, View.OnClickListener>> links) {
        try {

            SpannableString spannableString = new SpannableString(textView.getText().toString());
            int startIndexState = 0;

            for (Pair<String, View.OnClickListener> link : links) {
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        widget.invalidate();
                        assert link.second != null;
                        link.second.onClick(widget);
                    }
                };
                assert link.first != null;
                int startIndexOfLink = textView.getText().toString().indexOf(link.first, startIndexState);

                spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(spannableString, TextView.BufferType.SPANNABLE);
        } catch (Exception e) {
            FirebaseCrashlytics crashlytics;
            crashlytics = FirebaseCrashlytics.getInstance();
            Throwable throwable = new Throwable(e);
            crashlytics.recordException(throwable);
        }
    }


    /**
     * this is the test function used
     * for the response used in enrollment
     * testing..
     **/
    public static String getAssetJsonData(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    //capitalize the first word
    public static String capitalizeFirstWord(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String checkSpecialCharacters(String string) {
        try {
            string = string.replaceAll("[?#$%&^*~`\"';:,<.>/|\\-_%]", "");
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return string;
        }

    }

    public static void logout(Activity activity) {

        //delete all tables
        //clear the encryptionPreference
        AppDatabase.getInstance(activity).clearAllTables();

        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Store false value to say that user already saw the walk-through to SharedPreference
        // preferences.setEncryptedBoolString(USER_AGREEMENT, false);
        // editor.putBoolean(USER_AGREEMENT, false);
        editor.putString(AUTH_LOGIN_ID, null);
        editor.putString(AUTH_EMAIL, null);
        editor.putString(AUTH_PHONE_NUMBER, null);
        editor.putString(AUTH_LOGIN_TYPE, null);

        editor.apply();

        Intent logoutIntent = new Intent(activity, SplashScreenActivity.class);
        activity.startActivity(logoutIntent);
        activity.finish();


    }

}
