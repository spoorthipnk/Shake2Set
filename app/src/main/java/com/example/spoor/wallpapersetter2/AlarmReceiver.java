package com.example.spoor.wallpapersetter2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by spoor on 1/25/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent backgroundIntent = new Intent(context,ShakeDetectionService.class);
        context.startService(backgroundIntent);
    }
}
