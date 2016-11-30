package com.sahapad.sahapad_restaurant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.sahapad.sahapad_restaurant.R;

/**
 * Created by Pc on 2/11/2559.
 */
public class MyAlert {
    public static void myDialog(Context context,
                                String strTitle,
                                String strMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.danger);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}