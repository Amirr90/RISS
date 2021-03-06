package com.example.riss.AppUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.example.riss.R;
import com.example.riss.interfaces.IUserProfileInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static final String REF_USER = "Users";

    public static AlertDialog alertDialog;
    public static final String USER_NAME = "username";
    public static final String TOKEN = "token";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ADDRESS = "address";
    public static final String OCCUPATION = "occupation";
    public static final String EDUCATION = "education";
    public static final String AADHAR_NO = "aadharNo";
    public static final String UID = "uid";
    public static final String IMAGE = "image";
    public static final String IS_ACTIVE = "isActive";
    public static final String CREATION_TIME = "creationTime";
    public static final String LAST_SIGN_IN_TIME = "lastSignInTime";
    public static final int UPDATE_PROFILE_REQ_CODE = 001;


    public static final String USER_QUERY = "Users";

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final String FUND_ID = "fundId";
    public static final String SUPPORT = "support";
    public static final String FUNDS = "Funds";
    public static final String NAME = "name";
    public static final String FUND_IS = "fundId";
    public static final String LIKE = "like";
    public static final String NUMBER = "number";
    public static final String LIKED_IDS = "likedIds";
    public static final String TIMESTAMP = "timestamp";
    public static final String FUNDS_DURATION = "fund_duration";
    public static final String DONATION = "duration";
    public static final String QUERY_PUBLIC_REVIEW = "PublicReviews";
    public static final String QUERY_DISTRIBUTOR_LIST = "DistributorsList";
    public static final String SUPPORT_TYPE_DIRECT = "direct";
    public static final String SUPPORT_TYPE_BY_CREATING_FUND = "byCreatingFund";
    public static final String PAYMENT_STATUS_PENDING = "Pending";
    public static final String PAYMENT_STATUS_FAILED = "Failed";
    public static final String PAYMENT_STATUS_SUCCESS = "Success";
    public static final String PAYMENT_STATUS_ACCEPTED = "Accepted";
    public static final String PAYMENT_STATUS_APPROVED = "Approved";
    public static final String PAYMENT_STATUS_REJECTED = "Reject";
    public static final String MedicineDistribute = "MedicineDistribute";
    public static final String uid = "uid";


    public static final String KEY_FUND_ID = "fundId";
    public static final String FUND_STATISTICS = "FundStatistics";
    public static final String TotalFundAmount = "TotalFundAmount";
    public static final String TotalFundManger = "TotalFundManger";
    public static final String FundSupportPayment = "FundSupportPayment";
    public static final String isAadharVerified = "isAadharVerified";
    public static final String FundSupport = "FundSupport";
    public static final String totalInvested = "totalInvested";
    public static final String fundAmount_KEY = "fundAmount";
    public static final String support = "support";
    public static final String fundName = "fundName";

    public static final String VALUE = "value";


    public static void showAlertDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_lay, null));

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
        alertDialog.show();

    }

    public static void showAlertDialog(Activity activity, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_lay, null));

        builder.setCancelable(isCancelable);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
        alertDialog.show();

    }


    public static void hideAlertDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getLastSeen(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date past = format.parse(time);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());


            if (seconds < 60) {
                return seconds + " seconds ago";
            } else if (minutes < 60) {
                return minutes + " minutes ago";
            } else if (hours < 24) {
                return hours + " hours ago";
            } else {
                return days + " days ago";
            }
        } catch (Exception j) {
            j.printStackTrace();

            return j.getLocalizedMessage();
        }
    }

    public static String getLastSeen(long timestamp) {
        try {
            String time = String.valueOf(timestamp);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date past = format.parse(time);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());


            if (seconds < 60) {
                return seconds + " seconds ago";
            } else if (minutes < 60) {
                return minutes + " minutes ago";
            } else if (hours < 24) {
                return hours + " hours ago";
            } else {
                return days + " days ago";
            }
        } catch (Exception j) {
            j.printStackTrace();

            return j.getLocalizedMessage();
        }
    }

    public static void showSnackBar(String msg, View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    public static String getCountInRomanFormat(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    public static String getCountInRomanFormat(String num) {
        Number number = Integer.parseInt(num);
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    public static String getCurrencyFormat(double num) {
        String COUNTRY = "IN";
        String LANGUAGE = "en";
        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(num);
    }

    public static String getCurrencyFormat(long num) {
        String COUNTRY = "IN";
        String LANGUAGE = "en";
        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(num);
    }

    public static String getCurrencyFormat(String num) {
        Double number = Double.parseDouble(num);
        String COUNTRY = "IN";
        String LANGUAGE = "en";
        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(number);

    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    public static FirebaseFirestore getFirestoreReference() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        return firestore;
    }

    public static void addLike(String fundId) {
        Map<String, Object> map = new HashMap<>();
        map.put(LIKED_IDS, FieldValue.arrayUnion(getUid()));
        map.put(LIKE, FieldValue.increment(1));
        getFirestoreReference().collection(FUNDS).document(fundId).update(map);

    }

    public static void removeLike(String fundId) {
        Map<String, Object> map = new HashMap<>();
        map.put(LIKED_IDS, FieldValue.arrayRemove(getUid()));
        map.put(LIKE, FieldValue.increment(-1));
        getFirestoreReference().collection(FUNDS).document(fundId).update(map);
    }

    public static String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

    public static String getMobile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getPhoneNumber();
    }

    public static void checkUserProfile(final IUserProfileInterface iUserProfileInterface) {
        if (null != getUid())
            getFirestoreReference().collection(REF_USER).document(getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (null != documentSnapshot) {
                        if (documentSnapshot.contains(USER_NAME) && !documentSnapshot.getString(USER_NAME).trim().isEmpty()) {
                            iUserProfileInterface.isProfileCompleted(true);
                        } else {
                            try {
                                if (documentSnapshot.getBoolean("isAadharVerified")) {
                                    iUserProfileInterface.isProfileVerified(true);

                                } else iUserProfileInterface.isProfileVerified(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            iUserProfileInterface.isProfileCompleted(false);
                        }

                        iUserProfileInterface.profileData(documentSnapshot);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iUserProfileInterface.onError(e.getLocalizedMessage());
                }
            });
    }

    public static String getDateInDMY(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((int) timestamp);
        return formatter.format(calendar.getTime());
    }

    public static String[] getPlans() {
        String[] ITEMS = {
                "₹100 for 3 months",
                "₹500 for 6 months",
                "₹1,000 for 9 months",
                "₹2,500 for 12 months",
                "₹5,000 for 15 months",
                "₹10,000 for 18 months",
                "₹20,000 for 21 months",
                "₹50,000 for 24 months",
                "₹1,00,000 for 30 months",
        };
        return ITEMS;
    }

    public static String getInitialValue(int position) {
        String[] ITEMS = {"100", "500", "1000", "2500", "5000", "10000", "20000",
                "50000", "100000",
        };
        return ITEMS[position];
    }

    public static String getDuration(int position) {
        String[] ITEMS = {"3", "6", "9", "12", "15", "18", "21",
                "24", "30",
        };
        return ITEMS[position];
    }


    public static boolean isInternetConnected() {
        return true;
    }

    public static List<Integer> getColorListForGraph() {
        List<Integer> color = new ArrayList<>();
        color.add(Color.parseColor("#C0CA33"));
        color.add(Color.parseColor("#CDA67F"));
        color.add(Color.parseColor("#abd166"));
        color.add(Color.parseColor("#28B463"));
        color.add(Color.parseColor("#1F618D"));
        color.add(Color.parseColor("#F5B7B1"));
        color.add(Color.parseColor("#CCCCFF"));
        color.add(Color.parseColor("#6495ED"));
        color.add(Color.parseColor("#ff00bf"));
        color.add(Color.parseColor("#0080ff"));
        color.add(Color.parseColor("#40ff00"));
        color.add(Color.parseColor("#80ff00"));
        color.add(Color.parseColor("#ff9966"));
        color.add(Color.parseColor("#abd1ba"));
        color.add(Color.parseColor("#c2c2a3"));
        color.add(Color.parseColor("#CDA67F"));
        color.add(Color.parseColor("#FED70E"));
        color.add(Color.parseColor("#F4511E"));
        color.add(Color.parseColor("#F7DC6F"));
        color.add(Color.parseColor("#7CB342"));
        return color;
    }

    public static List<Integer> getColorList() {
        List<Integer> color = new ArrayList<>();
        color.add(R.color.color1);
        color.add(R.color.color2);
        color.add(R.color.color3);
        color.add(R.color.color4);
        color.add(R.color.color5);
        color.add(R.color.color6);
        color.add(R.color.color7);
        color.add(R.color.color8);
        color.add(R.color.color9);
        color.add(R.color.color10);
        color.add(R.color.color11);
        color.add(R.color.color12);
        color.add(R.color.color13);
        color.add(R.color.color14);
        color.add(R.color.color15);
        color.add(R.color.color15);
        color.add(R.color.color16);
        color.add(R.color.color17);
        color.add(R.color.color18);
        color.add(R.color.color19);
        color.add(R.color.color20);
        return color;
    }

    public static List<String> getData() {
        List<String> strings = new ArrayList<>();
        strings.add("10");
        strings.add("20");
        strings.add("30");
        strings.add("40");
        strings.add("50");
        strings.add("75");
        strings.add("100");

        return strings;
    }

    public static String getHandlingCharger(int position) {
        List<String> strings = new ArrayList<>();
        strings.add("2.5");
        strings.add("3.5");
        strings.add("5.0");
        strings.add("7.0");
        strings.add("10.0");
        strings.add("15.0");
        strings.add("20.0");

        return strings.get(position);
    }
}
