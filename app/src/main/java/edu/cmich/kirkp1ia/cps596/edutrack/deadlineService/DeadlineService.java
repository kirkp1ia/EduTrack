package edu.cmich.kirkp1ia.cps596.edutrack.deadlineService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

import edu.cmich.kirkp1ia.cps596.edutrack.ActivityViewDeadline;
import edu.cmich.kirkp1ia.cps596.edutrack.R;
import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

/**
 * Created by kirkp1ia on 11/21/16.
 */

public class DeadlineService extends Service {

    private static final String TAG = "Deadline Service";

    private DeadlineNotifierThread deadlineNotifier;

    @Override
    public void onCreate() {
        if (this.deadlineNotifier == null) {
            this.deadlineNotifier = new DeadlineNotifierThread();
            Thread workerThread = new Thread(this.deadlineNotifier);
            workerThread.start();
        } else if (!this.deadlineNotifier.isRunning()) {
            Thread workerThread = new Thread(this.deadlineNotifier);
            workerThread.start();
        }
    }

    @Override
    public void onDestroy() {
        if (this.deadlineNotifier != null) {
            this.deadlineNotifier.terminate();
            this.deadlineNotifier = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DeadlineNotifierThread implements Runnable {

        private volatile boolean running = false;

        @Override
        public void run() {
            this.running = true;
            try {
                new File(getApplication().getFilesDir(), "deadlines/serviceRunning.txt").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (this.running) {

                try {
                    Log.d(TAG, "hi");
                    this.getDueDeadlinesToday(getApplicationContext());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        private void getDueDeadlinesToday(Context context) throws FileNotFoundException {
            NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            File upcomingDeadlines = new File(context.getFilesDir(), getString(R.string.path__deadlines_upcoming));
            Scanner scnr = new Scanner(upcomingDeadlines).useDelimiter("\\Z");
            try {
                JSONObject json = new JSONObject(scnr.next());
                JSONArray deadlinesInFuture = json.getJSONArray("deadlines");
                scnr.close();

                for (int i = 0; i < deadlinesInFuture.length(); i ++) {
                    int deadlineIndex = deadlinesInFuture.getJSONArray(i).getInt(1);
                    long due = deadlinesInFuture.getJSONArray(i).getLong(0);

                    Deadline d = null;
                    try {
                        d = new Deadline(getApplicationContext(), deadlineIndex);
                    } catch (FileNotFoundException e) {
                        continue;
                    }

                    long dueIn = due - Calendar.getInstance().getTimeInMillis();

                    // if due in 12 hours
                    long twelveHours = Long.valueOf(getString(R.string.millis_in_twelve_hours));
                    long twelveMin100Millis = twelveHours - 100L;
                    boolean lessThan12Hours = dueIn < twelveHours;
                    boolean greaterThan12Min100Millis = dueIn > twelveMin100Millis;

                    long twoHours = Long.valueOf(getString(R.string.millis_in_two_hours));
                    long twoMin100Millis = twoHours - 100L;
                    boolean lessThan2Hours = dueIn < twoHours;
                    boolean greaterThan2Min100Millis = dueIn > twoMin100Millis;

                    // If this is due before 12 hours but after 12 hours minus 100 milliseconds
                    // I do this because I only want to notify once but It might not evaluate this
                    //    line right at 12 hours. there may be a couple milliseconds delay.
                    //    Same with 2 hours.
                    if (lessThan12Hours && !d.alerted12) {
                        NotificationCompat.Builder notificationBuilder = this.getNotification(d, false);
                        notifyManager.notify(0, notificationBuilder.build());
                        d.alerted12 = true;
                        try {
                            d.saveChanges(context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (lessThan2Hours && !d.alerted2) {
                        NotificationCompat.Builder notificationBuilder = this.getNotification(d, false);
                        notifyManager.notify(0, notificationBuilder.build());
                        d.alerted2 = true;
                        try {
                            d.saveChanges(context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (dueIn <= 0 && !d.alertedDue) {
                        NotificationCompat.Builder notificationBuilder = this.getNotification(d, true);
                        notifyManager.notify(0, notificationBuilder.build());
                        d.alertedDue = true;
                        try {
                            d.saveChanges(context);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private NotificationCompat.Builder getNotification(Deadline d, boolean due) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
            notificationBuilder.setSmallIcon(R.mipmap.planner_icon);
            if (!due) {
                notificationBuilder.setContentTitle("Due: " + d.getDescription());
                notificationBuilder.setContentText(d.getDeadline().toString());
            } else {
                notificationBuilder.setContentTitle("Due: " + d.getDescription());
                notificationBuilder.setContentText("Now");
            }

            Intent viewIntent = new Intent(getApplicationContext(), ActivityViewDeadline.class);
            viewIntent.putExtra("deadline_id", d.getId());
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(ActivityViewDeadline.class);
            stackBuilder.addNextIntent(viewIntent);
            PendingIntent viewPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(viewPendingIntent);

            return notificationBuilder;
        }

        public void terminate() {
            this.running = false;
        }

        public boolean isRunning() {
            return this.running;
        }
    }
}
