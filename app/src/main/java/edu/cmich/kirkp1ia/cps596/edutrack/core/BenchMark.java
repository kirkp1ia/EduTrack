package edu.cmich.kirkp1ia.cps596.edutrack.core;

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

    public BenchMark(String description, int[] deadline) {
        this.description = description;
        this.setDeadline(deadline);
    }

    public BenchMark(String description, long deadline) {
        this.description = description;
        this.deadline = Calendar.getInstance();
        this.deadline.setTimeInMillis(deadline);
    }

    private void setDeadline(int[] deadline) {
        this.deadline = Calendar.getInstance();
        this.deadline.set(deadline[0], deadline[1], deadline[2], deadline[3], deadline[4]);
    }

    public String toString() {
        return this.deadline.getTime().toString() + " - " + this.description;
    }
}
