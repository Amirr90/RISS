package com.example.riss.AppUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.riss.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static AlertDialog alertDialog;
    public static final String USER_NAME = "username";
    public static final String TOKEN = "token";
    public static final String MOBILE = "mobile";
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


    public static void showAlertDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_lay, null));

        builder.setCancelable(true);
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

    public static String getCurrencyFormat(long num) {
        String COUNTRY = "IN";
        String LANGUAGE = "en";
        return NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(num);


    }

    public static String getCurrencyFormat(String num) {
        Long number = Long.parseLong(num);
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

    public static void addLike(String fundId, final Activity activity, ArrayList<String> likedIdsList) {
        /*Map<String, Object> map = new HashMap<>();
        map.put(LIKED_IDS, likedIdsList);
        map.put(LIKE, FieldValue.increment(1));
        likedIdsList.add(getUid());
        getFirestoreReference().collection(FUNDS).document(fundId)
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();
            }
        });*/
        Map<String, Object> map = new HashMap<>();
        map.put(LIKED_IDS, FieldValue.arrayUnion(getUid()));
        map.put(LIKE, FieldValue.increment(1));
        getFirestoreReference().collection(FUNDS).document(fundId).update(map);

    }

    public static void removeLike(String fundId, final Activity activity, ArrayList<String> likedIdsList) {

       /* for (int a = 0; a < likedIdsList.size(); a++) {
            if (likedIdsList.get(a).equalsIgnoreCase(getUid())) {
                likedIdsList.remove(a);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put(LIKED_IDS, likedIdsList);
        map.put(LIKE, FieldValue.increment(-1));
        getFirestoreReference().collection(FUNDS).document(fundId)
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Liked", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();
            }
        });*/

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
}
