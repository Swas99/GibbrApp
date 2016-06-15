package com.swastik.gibbrtestapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;

/**
 * Created by Swastik on 15-06-2016.
 */
public class CommonUtil
{

    public static void showAlertMessage(String title, String message,
                                        Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }


    public static int ConvertToPx(Context c,int dip)
    {
        Resources r = c.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

    public static Point getWindowSize(Activity callingActivity)
    {
        Point windowSize = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            callingActivity.getWindowManager().getDefaultDisplay().getSize(windowSize);
        }
        else
        {
            windowSize.x = callingActivity.getWindowManager().getDefaultDisplay().getWidth();
            windowSize.y = callingActivity.getWindowManager().getDefaultDisplay().getHeight();
        }
        return windowSize;
    }

    public static int getScreenWidth(Activity callingActivity)
    {
        return getWindowSize(callingActivity).x;
    }

    public static int getScreenHeight(Activity callingActivity)
    {
        return getWindowSize(callingActivity).y;
    }


}
