package com.example.tamz2test;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.text.InputFilter;
import android.text.Spanned;

import androidx.appcompat.app.AlertDialog;

import com.google.mlkit.vision.text.Text;

public class Utils {
    public static String notificationChannelName = "serviceNotification";

    public static InputFilter getDigitFilter() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                if (dstart == 0 && source.length() > 0 && source.charAt(0) == '0') {
                    return "";
                }

                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    public static int currentTimeToTimestamp(int months) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.MONTH, months);

        return (int) (c.getTimeInMillis() / 1000L);
    }

    public static int componentTimeToTimestamp(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return (int) (c.getTimeInMillis() / 1000L);
    }

    public static String timestampToString(int timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp * 1000L);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return String.format("%d/%d/%d", year, month, day);
    }

    public static int getMileageFromPicture(Text visionText) {
        int result = 0;
        for (Text.TextBlock txt : visionText.getTextBlocks()) {
            try {
                if (result < Integer.parseInt(txt.getText())) {
                    result = Integer.parseInt(txt.getText());
                }
            } catch (NumberFormatException exception) {

            }
        }

        return result;
    }

    public static void showErrorDialog(String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean getIsDue(int months, int serviceDate) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(serviceDate * 1000L);
        c.add(Calendar.MONTH, months);

        return ((int) (c.getTimeInMillis() / 1000L)) <= currentTimeToTimestamp(0) ;
    }
}
