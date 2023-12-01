package com.pinc.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupAdminBasicSettings {
    @SerializedName("PolicyDefinition_data")
    @Expose
    private PolicyDefinitionData PolicyDefinition_data;
    @SerializedName("ENROLMENT_TYPE")
    @Expose
    private String ENROLMEN_TYPE;
    @SerializedName("Server_Date")
    @Expose
    private String ServerDate;
    @SerializedName("ENROLLMENT_THROUGH_MB")
    @Expose
    private String ENROLLMENT_THROUGH_MB;

    public String getAPP_ENROLLMENT_TYPE() {
        return APP_ENROLLMENT_TYPE;
    }

    public void setAPP_ENROLLMENT_TYPE(String APP_ENROLLMENT_TYPE) {
        this.APP_ENROLLMENT_TYPE = APP_ENROLLMENT_TYPE;
    }

    @SerializedName("APP_ENROLLMENT_TYPE")
    @Expose
    private String APP_ENROLLMENT_TYPE;

    public PolicyDefinitionData getPolicyDefinition_data() {
        return PolicyDefinition_data;
    }

    public void setPolicyDefinition_data(PolicyDefinitionData policyDefinition_data) {
        PolicyDefinition_data = policyDefinition_data;
    }

    public String getENROLMEN_TYPE() {
        return ENROLMEN_TYPE;
    }

    public void setENROLMEN_TYPE(String ENROLMEN_TYPE) {
        this.ENROLMEN_TYPE = ENROLMEN_TYPE;
    }

    public String getServerDate() {
        return ServerDate;
    }

    public void setServerDate(String serverDate) {
        ServerDate = serverDate;
    }

    public String getENROLLMENT_THROUGH_MB() {
        return ENROLLMENT_THROUGH_MB;
    }

    public void setENROLLMENT_THROUGH_MB(String ENROLLMENT_THROUGH_MB) {
        this.ENROLLMENT_THROUGH_MB = ENROLLMENT_THROUGH_MB;
    }

    @Override
    public String toString() {
        return "GroupAdminBasicSettings{" +
                "PolicyDefinition_data=" + PolicyDefinition_data +
                ", ENROLMEN_TYPE='" + ENROLMEN_TYPE + '\'' +
                ", ServerDate='" + ServerDate + '\'' +
                ", ENROLLMENT_THROUGH_MB='" + ENROLLMENT_THROUGH_MB + '\'' +
                '}';
    }
}
