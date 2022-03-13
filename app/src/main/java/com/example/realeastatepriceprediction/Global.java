package com.example.realeastatepriceprediction;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.Key;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Global {


    public static String NoInternet = "Check your Internet connection..";
    public static String WentWrong = "Something went Wrong.Try after some time..";
    public static String Server = "Server Error";
    public static String TryAgainafter = "Unable to connect to server. Please try after sometime";
    public static String GuestUser = "Guest user not Authorised";
    public static int SelectedTabPosition = 0;
    private Context context;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static ArrayList<String> tabname_home = new ArrayList<>();

    public Global(Context context) {
        this.context = context;
    }


    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void sout(String msg) {
        try {
            if (BuildConfig.DEBUG) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showCustomToast(Activity activity, String message, int length) {
        try {
            if (activity != null) {
                if (!Global.isNull(message)) {
                    if (length == 0) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCustomToast(Context context, String message, int length) {
        try {
            if (context != null) {
                if (!Global.isNull(message)) {
                    if (length == 0) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dialog(Context context, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public static void setTextview(TextView textview, String msg) {
        try {
            if (Global.isNull(msg)) {
                msg = "";
            }
            if (textview != null) {
                textview.setText(msg);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNull(String val) {
        return val == null || val.equalsIgnoreCase(null) || val.trim().equalsIgnoreCase("") || val.trim().equalsIgnoreCase("null") || val.trim() == "" || val.trim() == "null";
    }

    public static boolean checkArryList(Collection<?> array) {
        if (array != null && array.size() > 0) {
            return true;
        }
        return false;
    }

    public static String encryptURL(String url) {
        byte[] data = new byte[0];
        try {
            data = url.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

    public static String DecryptURL(String url) {
        byte[] data = Base64.decode(url, Base64.DEFAULT);
        String decodedString = null;
        try {
            decodedString = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedString;
    }

    public static String getRemString(String mname) {
        String finalString = "";
        try {
            finalString = mname.replaceAll("/", "");
        } catch (Exception e) {
            e.printStackTrace();
            finalString = mname;
        }
        return finalString;
    }

    public static void SetText(TextView txtview, String msg) {
        try {
            if (msg == null) {
                msg = "";
            }

            if (txtview != null) {
                txtview.setText("" + msg);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDecimalValue(String val) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = "";
        try {
            yourFormattedString = formatter.format(Integer.valueOf(val));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return yourFormattedString;
    }

    public static void setEditText(EditText editText, String msg) {
        try {
            if (Global.isNull(msg)) {
                msg = "";
            }
            if (editText != null) {
                editText.setText(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setButtonText(Button button, String msg) {
        try {
            if (Global.isNull(msg)) {
                msg = "";
            }
            if (button != null) {
                button.setText(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DisplayLogo(TextView textView, String string) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(string);
    }



    public static String GOOGLE_PKG_NAME = "com.google.android.gms";
    public static String GOOGLE_CLASS_NAME = "com.google.android.gms.auth.api.phone.ui.UserConsentPromptActivity";


    public static String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static boolean checkEqualIgnoreCase(String str1, String str2) {
        return !isNull(str1) && !isNull(str2) && str1.equalsIgnoreCase(str2);
    }

    public static String checkAndGetStringValue(String str1, String str2) {
        return !isNull(str1) ? str1 : str2;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static boolean isOutOfStock(String mStock) {
        int stock = !isNull(mStock) ? Integer.parseInt(mStock) : 0;
        return stock <= 0;
    }







}