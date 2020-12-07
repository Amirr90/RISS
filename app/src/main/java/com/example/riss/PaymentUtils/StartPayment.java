package com.example.riss.PaymentUtils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.riss.AppUtils.Utils;
import com.example.riss.HomeScreen;
import com.example.riss.view.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.FundSupportPayment;
import static com.example.riss.AppUtils.Utils.IMAGE;
import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_SUCCESS;
import static com.example.riss.AppUtils.Utils.SUPPORT_TYPE_BY_CREATING_FUND;
import static com.example.riss.AppUtils.Utils.SUPPORT_TYPE_DIRECT;
import static com.example.riss.AppUtils.Utils.USER_NAME;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;

public class StartPayment {
    private static final String TAG = "StartPayment";
    Activity activity;
    PaymentCallback paymentCallback;

    private String userName;
    private String fundName;
    private String uid;
    private String fundId;
    private String amount;
    private String supportType; //Direct or By Creating fund
    private String txId;
    private String razorPayId;
    private String paymentStatus;
    private long timestamp;


    public String getRazorPayId() {
        return razorPayId;
    }

    public void setRazorPayId(String razorPayId) {
        this.razorPayId = razorPayId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSupportType() {
        return supportType;
    }

    public void setSupportType(String supportType) {
        this.supportType = supportType;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public StartPayment(Activity activity) {
        this.activity = activity;
    }

    public void initPayment(PaymentCallback paymentCallback) {
        this.paymentCallback = paymentCallback;
        initRazorPay();
    }

    private void initRazorPay() {

        Credentials credentials = new Credentials();
        Checkout.preload(activity);
        Checkout checkout = new Checkout();
        checkout.setKeyID(credentials.getKEY_TEST());

        String image = "https://digidoctor.in/assets/images/logonew.png";

        int amount = 100 * Integer.parseInt(getAmount());
        try {
            JSONObject options = new JSONObject();
            options.put("name", getFundName());
            options.put("description", "" + System.currentTimeMillis());
            options.put("image", image);
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "" + amount);//pass amount in currency subunits
            //options.put("prefill.email", getEmail());
            options.put("prefill.contact", Utils.getMobile());
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    public void updatePaymentStatus(String paymentId, String status) {
        if (getSupportType().equals(SUPPORT_TYPE_BY_CREATING_FUND)) {
            if (status.equalsIgnoreCase(PAYMENT_STATUS_SUCCESS)) {
                Map<String, Object> map = new HashMap<>();
                map.put("razorPayId", paymentId);
                Utils.getFirestoreReference().collection(FundSupportPayment).document(getFundId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        paymentCallback.onPaymentSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                        paymentCallback.onPaymentSuccess();
                        Toast.makeText(activity, "Failed to update status", Toast.LENGTH_SHORT).show();
                    }
                });
                /* paymentCallback.onPaymentSuccess();*/
            } else paymentCallback.onPaymentFailed();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("paymentStatus", status);
            map.put("razorPayId", paymentId);
            if (status.equalsIgnoreCase(PAYMENT_STATUS_SUCCESS)) {
                Utils.getFirestoreReference().collection(FundSupportPayment).document(getTxId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        paymentCallback.onPaymentSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                        paymentCallback.onPaymentSuccess();
                        Toast.makeText(activity, "Failed to update status", Toast.LENGTH_SHORT).show();
                    }
                });

                Map<String, Object> updateFundSupportMap = new HashMap<>();
                updateFundSupportMap.put("amount", Integer.parseInt(getAmount()));
                updateFundSupportMap.put("fundId", getFundId());
                updateFundSupportMap.put("fundName", getFundName());
                updateFundSupportMap.put("image", "");
                updateFundSupportMap.put("refId", null);
                updateFundSupportMap.put("timestamp", System.currentTimeMillis());
                updateFundSupportMap.put("userImage", "" + HomeFragment.user.getString(IMAGE));
                updateFundSupportMap.put("uid", getUid());
                updateFundSupportMap.put("userName", "" + HomeFragment.user.getString(USER_NAME));
                updateFundSupportMap.put("supportType", SUPPORT_TYPE_DIRECT);
                Utils.getFirestoreReference().collection(FUNDS).document(getFundId())
                        .collection("FundSupport").add(updateFundSupportMap);
            } else paymentCallback.onPaymentFailed();
        }
    }

}
