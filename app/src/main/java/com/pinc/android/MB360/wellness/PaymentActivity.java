package com.pinc.android.MB360.wellness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupUpdatePaymentRequest;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    //Package ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    HealthCheckupUpdatePaymentRequest healthCheckupUpdatePaymentRequest = new HealthCheckupUpdatePaymentRequest();

    String youpay = "";
    String familySrNo;
    String groupSrNo;
    String empIdNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();

    }

    public void init(){

        initViews();
        initValues();
    }

    public void initViews(){

        //viewModel scoped
        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(this).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(this).get(PackagesViewModel.class);
    }

    public void initValues(){

        youpay = getIntent().getExtras().getString("youpay").replace("\u20B9 " , "");
        familySrNo = getIntent().getExtras().getString("familySrNo");
        groupSrNo = getIntent().getExtras().getString("groupSrNo");
        empIdNo = getIntent().getExtras().getString("empIdNo");

        Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "youpay : " + youpay  );
        Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "familySrNo : " + familySrNo  );
        Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "groupSrNo : " + groupSrNo  );
        Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "empIdNo : " + empIdNo  );

        PaymentNow(Integer.parseInt(youpay));

    }

    public void PaymentNow(int youpay) {

//        final Activity activity = this;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_P85Qujlx0rs3IX");
        checkout.setImage(R.mipmap.ic_launcher_round);

//        double finalamount = Float.parseFloat(amount)*100;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "MyBenefits");
            options.put("description", "Payment ");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://wellness.mybenefits360.com/mybenefits/assets/img/logo-master-small.png");
            options.put("currency", "INR");
            options.put("amount", ""+ (100 * youpay));

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@test.com");
            preFill.put("contact", "8888888888");

            options.put("prefill", preFill);


            checkout.open(this, options);

        } catch(Exception e) {
            Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "Error in starting Razorpay Checkout : " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData data) {

        Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onPaymentSuccess: " + s );
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();

        String paymentId = data.getPaymentId();
        String signature = data.getSignature();
        String orderId = data.getOrderId();

        confirmApp(paymentId, signature, orderId);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData data) {

        Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onPaymentError: " + s );
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void  confirmApp(String paymentId, String signature, String orderId) {

                healthCheckupUpdatePaymentRequest.setEmpIdNo(""+empIdNo);
                healthCheckupUpdatePaymentRequest.setExtGroupSrNo("" + groupSrNo);
                healthCheckupUpdatePaymentRequest.setFamilySrNo("" + familySrNo);
                healthCheckupUpdatePaymentRequest.setPaymentId("" + paymentId);
                healthCheckupUpdatePaymentRequest.setOrderId("" + orderId);
                healthCheckupUpdatePaymentRequest.setSignature("" + signature);
                healthCheckupUpdatePaymentRequest.setOrderReferenceNumber("745710");

        packagesViewModel.putUpdatePayment(healthCheckupUpdatePaymentRequest).observe((LifecycleOwner) this, messageResponseUpdatePayment -> {
            Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "Update Payment : " + messageResponseUpdatePayment.toString());

            finish();
        });

    }


}