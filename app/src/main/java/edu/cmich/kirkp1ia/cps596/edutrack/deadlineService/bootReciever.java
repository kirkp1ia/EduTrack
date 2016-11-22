package edu.cmich.kirkp1ia.cps596.edutrack.deadlineService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by kirkp1ia on 11/21/16.
 */

public class bootReciever extends BroadcastReceiver{

    private static final String TAG = "Boot Reciever";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Booted!", Toast.LENGTH_LONG);
        Intent serviceLauncher = new Intent(context, DeadlineService.class);
        context.startService(serviceLauncher);
    }
}
