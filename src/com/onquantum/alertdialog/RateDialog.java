package com.onquantum.alertdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class RateDialog {
	
	private static final String RATE_DIALOG_PREFERENCES = "rate_dialog_preferences";
    private static final String PREF_SUCCESS_RATE = "pref_success_rate";
    private static final String PREF_ENABLE_RATE= "pref_disable_rate";
    private static final String PREF_EVENT_COUNT = "pref_event_count";
    private static final String PREF_REMINDER_RATE = "pref_reminder_rate";

    public static void showRateDialog(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = (Context)activity;
                SharedPreferences sharedPreferences = context.getSharedPreferences(RATE_DIALOG_PREFERENCES,Context.MODE_PRIVATE);
                boolean isAvailable = sharedPreferences.getBoolean(PREF_ENABLE_RATE, true);
                boolean isSuccessRate = sharedPreferences.getBoolean(PREF_SUCCESS_RATE, false);
                int showCount = sharedPreferences.getInt(PREF_EVENT_COUNT, 0);

                if (!isAvailable) {
                	Log.i("info"," Alert Dialog is disabled");
                	return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(PREF_EVENT_COUNT, ++showCount);
                editor.commit();

                Log.i("info"," Rate dialog call count = " + showCount);
                String appName = getAppName(activity);

                AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);

                String title = activity.getString(activity.getResources().getIdentifier("rate_title","string",activity.getApplicationContext().getPackageName()));
                String message = activity.getString(activity.getResources().getIdentifier("rate_message", "string", activity.getApplicationContext().getPackageName()));

                builder.setTitle(String.format(title, appName));
                builder.setMessage(String.format(message,appName));

                String positiveLabel = activity.getString(activity.getResources().getIdentifier("rate", "string", activity.getApplicationContext().getPackageName()));
                String neutralLabel = activity.getString(activity.getResources().getIdentifier("rate_later", "string", activity.getApplicationContext().getPackageName()));
                String negativeLabel = activity.getString(activity.getResources().getIdentifier("rate_cancel", "string", activity.getApplicationContext().getPackageName()));

                builder.setPositiveButton(positiveLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String appPackage = ((Context)activity).getPackageName();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
                        activity.startActivity(intent);
                        rateApplication(activity);
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton(neutralLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rateLater(activity.getApplicationContext());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(negativeLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setEnableRateDialog(activity, false);
                        dialog.dismiss();
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        rateLater(activity);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private static String getAppName(Context context) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = pm.getApplicationInfo( context.getPackageName(), 0);
            return (String) (applicationInfo != null ? pm.getApplicationLabel(applicationInfo) : " ");
        } catch (final PackageManager.NameNotFoundException e) {
            return " ";
        }
    }

    private static void rateApplication(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RATE_DIALOG_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_SUCCESS_RATE, true);
        editor.commit();
        setEnableRateDialog(context, false);
    }

    private static void rateLater(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RATE_DIALOG_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(PREF_REMINDER_RATE, System.currentTimeMillis());
        editor.commit();
    }

    public static void setEnableRateDialog(Context context, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RATE_DIALOG_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_ENABLE_RATE, value);
        editor.commit();
    }
}
