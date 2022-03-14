package com.example.realeastatepriceprediction;

import android.app.Activity;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static Boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean pincodeValidation(Activity activity, EditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.PLEASE_ENTER_PINCODE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 6) {
            Global.showCustomToast(activity, Constants.VALID_PINCODE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().trim().startsWith("0")) {
            Global.showCustomToast(activity, Constants.PINCODE_SHOULD_NOT_START, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().trim().startsWith("9")) {
            Global.showCustomToast(activity, Constants.PINCODE_SHOULD_NOT_START_9, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static int LongestStringSequence(String message) {
        int largestSequence = 0;
        char longestChar = '\0';
        int currentSequence = 1;
        char current = '\0';
        char next = '\0';
        try {
            for (int i = 0; i < message.length() - 1; i++) {
                current = message.charAt(i);
                next = message.charAt(i + 1);
                // If character's are in sequence , increase the counter
                if (current == next) {
                    currentSequence += 1;
                } else {
                    if (currentSequence > largestSequence) { // When sequence is
                        // completed, check if
                        // it is longest
                        largestSequence = currentSequence;
                        longestChar = current;
                    }
                    currentSequence = 1; // re-initialize counter
                }
            }
            if (currentSequence > largestSequence) { // Check if last string
                // sequence is longest
                largestSequence = currentSequence;
                longestChar = current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return largestSequence;
    }

    public static boolean isValidRemarks(String remarks) {
        if (remarks.trim().length() < 10 || remarks.trim().length() > 950)
            return false;
        return true;
    }


    public static boolean mobileNoValidation(Activity activity, TextInputEditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_MOBILE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 10) {
            Global.showCustomToast(activity, Constants.MOBILE_10_DIGITS, 0);
            editText.requestFocus();
            return false;
        }
        if (!editText.getText().toString().startsWith("9") && !editText.getText().toString().startsWith("8")
                && !editText.getText().toString().startsWith("7") && !editText.getText().toString().startsWith("6")) {
            Global.showCustomToast(activity, Constants.ENTER_VALID_MOBILE, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean inchargeName(Activity activity, TextInputEditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_INCHARGE_NAME, 0);
            editText.requestFocus();
            return false;
        }
        if (LongestStringSequence(editText.getText().toString()) > 4) {
            Global.showCustomToast(activity, Constants.SAME_CHARACTER_IS_REPEATING + "incharge name field", 0);
            return false;
        }
        if (editText.getText().toString().startsWith(" ")) {
            Global.showCustomToast(activity, Constants.INCHARGE_NAME_SHOULD_NOT_START_SPACE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().contains("  ")) {
            Global.showCustomToast(activity, Constants.INCHARGE_NAME_SHOULD_NOT_CONTAIN_DOUBLE_SPACE, 0);
        }
        if (editText.getText().toString().length() < 2) {
            Global.showCustomToast(activity, Constants.INCHARGE_NAME_MIN_2, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean organizationName(Activity activity, EditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_FNAME, 0);
            editText.requestFocus();
            return false;
        }
        if (LongestStringSequence(editText.getText().toString()) > 4) {
            Global.showCustomToast(activity, Constants.SAME_CHARACTER_IS_REPEATING + "organization name field", 0);
            return false;
        }
        if (editText.getText().toString().startsWith(" ")) {
            Global.showCustomToast(activity, Constants.NAME_SHOULD_NOT_START_SPACE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 2) {
            Global.showCustomToast(activity, Constants.FNAME_MIN_2, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean lastNameValidation(Activity activity, EditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_LNAME, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().startsWith(" ")) {
            Global.showCustomToast(activity, Constants.NAME_SHOULD_NOT_START_SPACE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 1) {
            Global.showCustomToast(activity, Constants.LNAME_MIN_1, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean ageValidation(Activity activity, EditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_AGE, 0);
            editText.requestFocus();
            return false;
        }
        if (Integer.parseInt(editText.getText().toString()) < 1 || Integer.parseInt(editText.getText().toString()) > 120) {
            Global.showCustomToast(activity, Constants.AGE_BETWEEN_1_AND_120, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean emailValidation(Activity activity, TextInputEditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_EMAIL, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().startsWith(" ")) {
            Global.showCustomToast(activity, Constants.EMAIL_SHOULD_NOT_START_SPACE, 0);
            editText.requestFocus();
            return false;
        }
        if (!isValidEmail(editText.getText().toString().trim())) {
            Global.showCustomToast(activity, Constants.VALID_EMAIL, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean addressValidation(Activity activity, EditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_ADDRESS, 0);
            editText.requestFocus();
            return false;
        }
        if (LongestStringSequence(editText.getText().toString()) > 4) {
            Global.showCustomToast(activity, Constants.SAME_CHARACTER_IS_REPEATING + "address field", 0);
            return false;
        }
        if (editText.getText().toString().startsWith(" ")) {
            Global.showCustomToast(activity, Constants.ADDRESS_SHOULD_NOT_START_SPACE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().contains("  ")) {
            Global.showCustomToast(activity, Constants.ADDRESS_SHOULD_NOT_HAVE_DOUBLE_SPACE, 0);

        }
        if (editText.getText().toString().length() < 25) {
            Global.showCustomToast(activity, Constants.ADDRESS_MIN_25, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean cityValidation(Activity activity, EditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_CITY, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().startsWith(" ")) {
            Global.showCustomToast(activity, Constants.CITY_SHOULD_NOT_START_SPACE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 2) {
            Global.showCustomToast(activity, Constants.CITY_MIN_2, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean stateValidation(Activity activity, EditText editText) {
        if (Global.isNull(editText.getText().toString())) {
            Global.showCustomToast(activity, Constants.ENTER_STATE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().startsWith(" ")) {
            Global.showCustomToast(activity, Constants.STATE_SHOULD_NOT_START_SPACE, 0);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 1) {
            Global.showCustomToast(activity, Constants.STATE_MIN_1, 0);
            editText.requestFocus();
            return false;
        }
        return true;
    }


    public static boolean gstNumberValdiation(Activity activity, EditText editText, Boolean flag) {
        // true means mandatory
        Matcher matcher;
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
        if (flag) {
            if (Global.isNull(editText.getText().toString())) {
                Global.showCustomToast(activity, Constants.EMPTY_GST_NUMBER, 0);
                return false;
            }

        }
        if (editText.getText().toString().length() > 0) {
            Pattern p = Pattern.compile(regex);
            matcher = p.matcher(editText.getText().toString());
            if (!matcher.matches()) {
                Global.showCustomToast(activity, Constants.WRONG_GST_NUMBER, 0);
                return false;
            }

        }

        return true;
    }

    public static boolean passwordValidation(Activity activity, TextInputEditText editText) {
        if (editText.getText().toString().length()<8){
            Global.showCustomToast(activity,"Minimum 8 characters are required",0);
            return false;
        }

        return true;
    }
}