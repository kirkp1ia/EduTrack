package edu.cmich.kirkp1ia.cps596.edutrack.deadlineService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kirkp1ia on 11/21/16.
 */

public class bootReciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceLauncher = new Intent(context, DeadlineService.class);
        context.startService(serviceLauncher);
    }
}
