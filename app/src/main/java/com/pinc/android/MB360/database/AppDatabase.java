package com.pinc.android.MB360.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.pinc.android.MB360.database.dao.ClaimProcedureDao;
import com.pinc.android.MB360.database.dao.EnrollmentWindowCountDao;
import com.pinc.android.MB360.database.dao.IntimateClaimsDao;
import com.pinc.android.MB360.database.dao.CoveragesDao;
import com.pinc.android.MB360.database.dao.LoadSessionDao;
import com.pinc.android.MB360.database.dao.MyClaimsDao;
import com.pinc.android.MB360.database.dao.PolicyFeatureDao;
import com.pinc.android.MB360.database.dao.ProviderNetworkDao;
import com.pinc.android.MB360.database.dao.EscalationsDao;
import com.pinc.android.MB360.database.dao.FaqDao;
import com.pinc.android.MB360.database.dao.ProfileDao;
import com.pinc.android.MB360.database.dao.QueryDao;
import com.pinc.android.MB360.database.dao.UtilitiesDao;
import com.pinc.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;

import com.pinc.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;
import com.pinc.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.pinc.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;
import com.pinc.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
import com.pinc.android.MB360.insurance.hospitalnetwork.reponseclass.DocumentElementCount;
import com.pinc.android.MB360.insurance.hospitalnetwork.reponseclass.Hospitals;
import com.pinc.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;
import com.pinc.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimsDetails;
import com.pinc.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponseOffline;
import com.pinc.android.MB360.insurance.profile.response.ProfileResponse;
import com.pinc.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.pinc.android.MB360.insurance.queries.responseclass.QueryResponse;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.pinc.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;


@Database(entities = {FaqResponse.class,
        EscalationsResponse.class,
        UtilitiesResponse.class,
        PolicyFeaturesResponseOffline.class,
        ProfileResponse.class,
        QueryResponse.class,
        QueryDetails.class,
        ClaimsResponse.class,
        LoadPersonsIntimationResponse.class,
        CoverageResponse.class,
        CoverageDetailsResponse.class,
        ClaimsProcedureLayoutInfoResponse.class,
        ClaimProcedureTextResponse.class,
        ClaimProcedureLayoutInstructionInfo.class,
        ClaimProcedureEmergencyContactResponse.class,
        ClaimProcedureImageResponse.class,
        DocumentElement.class,
        ClaimsDetails.class,
        Hospitals.class,
        HospitalInformation.class,
        DocumentElementCount.class,
        LoadSessionResponse.class,
        AdminSettingResponse.class


}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "PINC";

    public abstract FaqDao faqDao();

    public abstract EscalationsDao escalationsDao();

    public abstract UtilitiesDao utilitiesDao();
    public abstract PolicyFeatureDao policyFeatureDao();

    public abstract ProfileDao profileDao();

    public abstract QueryDao queryDao();

    public abstract IntimateClaimsDao cLaimsDao();

    public abstract CoveragesDao coverageDao();

    public abstract ClaimProcedureDao claimProcedureLayoutDao();

    public abstract MyClaimsDao documentElementDao();

    public abstract ProviderNetworkDao documentElementCountDao();

    public  abstract LoadSessionDao loadSessionDao();

    public  abstract EnrollmentWindowCountDao enrollmentWindowCountDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void clearAllTables() {

    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return null;
    }
}


