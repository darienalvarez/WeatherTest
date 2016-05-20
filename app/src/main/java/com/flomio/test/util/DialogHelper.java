package com.flomio.test.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.flomio.test.R;

/**
 * Created by Darien
 * on 5/19/16.
 */
public class DialogHelper {

    public static void showNoConnectionDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(R.string.no_connection);
        builder.setTitle(R.string.title_no_connection);
        builder.setPositiveButton(R.string.settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
        );
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public static void showErrorDialog(Context context, int messageResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(R.string.error);
        builder.setMessage(messageResource);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        builder.show();
    }

    public static ProgressDialog buildProgressDialog(Context context) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Loading info");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        return progress;
    }

    public static void showInfoDialog(Context context, String messageResource, final ActionCallback action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(R.string.info);
        builder.setMessage(messageResource);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (action != null) {
                            action.execute();
                        }
                        dialog.dismiss();

                    }
                }
        );

        builder.show();
    }

    public interface ActionCallback {
        void execute();
    }
}
