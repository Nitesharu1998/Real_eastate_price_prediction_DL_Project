package com.example.realeastatepriceprediction;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class ShareOptions {

    public static void shareOnSMS(Activity activity, String mobile, String msg) {
        try {
            Uri uri = Uri.parse("smsto:" + mobile);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            sendIntent.putExtra("sms_body", msg);
            activity.startActivity(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareOnEmail(Activity activity, String emailID, String subj, String msg) {
        try {
            Intent emailintent = new Intent(Intent.ACTION_SENDTO);
            emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailID});
            emailintent.setData(Uri.parse("mailto:"));
            emailintent.putExtra(Intent.EXTRA_SUBJECT, subj);
            emailintent.putExtra(Intent.EXTRA_TEXT, msg);
//            emailintent.setType("message/rfc822");
            activity.startActivity(Intent.createChooser(emailintent, "Choose an Email client to which you want to share details:"));
        } catch (Exception e) {
            e.printStackTrace();
            Global.showCustomToast(activity, Constants.NO_EMAIL_CLIENT_INSTALLED, 0);
        }
    }

    public static void shareOnWhatsapp(Activity activity, String mobile, String msg) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(whatsappUrl(mobile, msg)));
            activity.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String whatsappUrl(String mobile, String msg) {

        Uri builtUri;
        final String BASE_URL = "https://api.whatsapp.com/";
        final String WHATSAPP_PHONE_NUMBER = "91" + mobile;
        final String PARAM_PHONE_NUMBER = "phone";
        final String PARAM_TEXT = "text";
        String newUrl = BASE_URL + "send";

        if (Global.isNull(mobile)) {
            builtUri = Uri.parse(newUrl).buildUpon()
                    .appendQueryParameter(PARAM_TEXT, msg)
                    .build();
        } else {
            builtUri = Uri.parse(newUrl).buildUpon()
                    .appendQueryParameter(PARAM_PHONE_NUMBER, WHATSAPP_PHONE_NUMBER)
                    .appendQueryParameter(PARAM_TEXT, msg)
                    .build();
        }
        return buildWhatsappUrl(builtUri).toString();
    }

    private static boolean isWhatsApp(Activity mActivity) {
        try {
            PackageManager pm = mActivity.getPackageManager();
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Global.showCustomToast(mActivity, Constants.WHATSAPP_NOT_INSTALLED, 0);
        }
        return false;
    }

    private static URL buildWhatsappUrl(Uri myUri) {
        URL finalUrl = null;
        try {
            finalUrl = new URL(myUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }
}
