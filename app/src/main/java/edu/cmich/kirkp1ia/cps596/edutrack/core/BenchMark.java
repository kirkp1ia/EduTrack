package edu.cmich.kirkp1ia.cps596.edutrack.core;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by kirkp1ia on 11/5/16.
 */

public class BenchMark {

    private static final String TAG = "BenchMark";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    private Calendar deadline;

    public BenchMark(String description, String deadline) {
        this.description = description;
        this.setDeadline(deadline);
    }

    public BenchMark(String description, long deadline) {
        this.description = description;
        this.deadline = Calendar.getInstance();
        this.deadline.setTimeInMillis(deadline);
    }

    private void setDeadline(String deadline) {
        String date = deadline.split(" ")[0];
        String time = deadline.split(" ")[1];
        String am_pm = deadline.split(" ")[2];

        Log.d(TAG, date);
        Log.d(TAG, time);
        Log.d(TAG, am_pm);

        int m = Integer.valueOf(date.split("/")[0]) - 1;
        int d = Integer.valueOf(date.split("/")[1]);
        int y = Integer.valueOf(date.split("/")[2]);

        int hour = Integer.valueOf(time.split(":")[0]);
        int minute = Integer.valueOf(time.split(":")[1]);

        if ("pm,p.m.,Pm,P.m.,pM,p.M.,PM,P.M.".contains(am_pm)) {
            hour = hour + 12;
        }

        this.deadline = Calendar.getInstance();
        this.deadline.set(y, m, d, hour, minute);
    }
}
