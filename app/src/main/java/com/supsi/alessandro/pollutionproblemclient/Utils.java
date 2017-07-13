package com.supsi.alessandro.pollutionproblemclient;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Alessandro on 14/07/2017.
 */

public class Utils {
    public static void vibrate(long milliseconds) {
        Context context = PollutionApplication.getAppContext();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }
}
