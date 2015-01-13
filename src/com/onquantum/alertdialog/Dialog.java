package com.onquantum.alertdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Dialog {
		public static void showAlertDialog(final Activity activity,final String title, final String message, final String buttonLabel) {
	        activity.runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	        		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	        		builder.setTitle(title)
	        			.setMessage(message)
	        			.setNegativeButton(buttonLabel, new DialogInterface.OnClickListener() {
	        				@Override
	        				public void onClick(DialogInterface dialog, int which) {
	        					// TODO Auto-generated method stub
	        					dialog.cancel();
	        				}
	        			});
	        		AlertDialog dialog = builder.create();
	        		dialog.show();
	            }
	        });
		}
}
