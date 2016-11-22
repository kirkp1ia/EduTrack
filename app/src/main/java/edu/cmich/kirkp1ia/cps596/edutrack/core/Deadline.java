package edu.cmich.kirkp1ia.cps596.edutrack.core;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

import edu.cmich.kirkp1ia.cps596.edutrack.R;

/**
 * Created by kirkp1ia on 11/1/16.
 */
public class Deadline {

    private static final String FILE_EXTENSION = "ddln";
    private static final String TAG = "Deadline";

    private int id;                                                                                 public int getId() { return this.id; }
    private String description;                                                                     public String getDescription() { return this.description; }
    private String notes;                                                                           public String getNotes() { return this.notes; }
    private Calendar deadline;                                                                      public Calendar getDeadline() { return this.deadline; }
    public boolean alerted12 = false;
    public boolean alerted2 = false;
    public boolean alertedDue = false;
    private ArrayList<BenchMark> benchmarks = new ArrayList<BenchMark>();                                   public ArrayList<BenchMark> getBenchmarks() { return this.benchmarks; }

    private String location;

    /**
     * Create new deadline instance with all new attributes.
     * @param context
     * @param date
     * @throws FileNotFoundException
     */
    public Deadline(Context context, Calendar date, String notes, String description) throws FileNotFoundException {

        this.id = this.getNextId(context);
        this.location = context.getResources().getString(R.string.path__deadline_storage) + this.id + "." + FILE_EXTENSION;

        this.description = description;
        this.notes = notes;

        this.deadline = date;
    }

    /**
     * Instantiate deadline instance using a pre-existing
     * id. Assume the deadline exists in storage so this
     * constructor will load that deadline from it's file
     * which is named with id as the file name.
     * @param context
     * @param id
     */
    public Deadline(Context context, int id) throws FileNotFoundException {

        this.id = id;

        this.location = context.getResources().getString(R.string.path__deadline_storage) + this.id + "." + FILE_EXTENSION;

        File locationFile = new File(context.getFilesDir(), this.location);
        Scanner sc = new Scanner(locationFile).useDelimiter("\\Z");

        JSONTokener parser = new JSONTokener(sc.next());
        try {
            JSONObject json = new JSONObject(parser);
            String desc = json.getString("description");
            String notes = json.getString("notes");
            Long deadline = json.getLong("deadline");
            boolean alerted12 = false;
            boolean alerted2 = false;
            boolean alertedDue = false;
            try {
                alerted12 = json.getBoolean("alerted12");
            } catch (JSONException e) {

            }
            try {
                alerted2 = json.getBoolean("alerted2");
            } catch (JSONException e) {

            }
            try {
                alertedDue = json.getBoolean("alertedDue");
            } catch (JSONException e) {

            }

            this.description = desc;
            this.notes = notes;
            this.deadline = Calendar.getInstance();
            this.deadline.setTimeInMillis(deadline);
            this.readBenchmarks(json);
            this.alerted12 = alerted12;
            this.alerted2 = alerted2;
            this.alertedDue = alertedDue;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addBenchMark(String description, int[] deadline) {
        this.benchmarks.add(new BenchMark(description, deadline));
    }

    public void addBenchMark(BenchMark benchMark) {
        this.benchmarks.add(benchMark);
    }

    public void addBenchMarks(ArrayList<BenchMark> benchmarks) {
        this.benchmarks.addAll(benchmarks);
    }

    private void readBenchmarks(JSONObject deadlineObj) {
        try {
            JSONArray benchmarks = deadlineObj.getJSONArray("benchmarks");
            for (int i = 0; i < benchmarks.length(); i ++) {
                JSONArray benchmark = benchmarks.getJSONArray(i);
                this.addBenchMark(benchmark);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBenchMark(JSONArray benchmark) {
        try {
            this.benchmarks.add(new BenchMark(benchmark.getString(0), benchmark.getLong(1)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Deadline removeBenchmark(Context context, int i) {
        try {
            this.benchmarks.remove(i);
            Deadline newDeadline = new Deadline(context, this.deadline, this.notes, this.description);
            newDeadline.benchmarks = this.benchmarks;

            this.delete(context);
            return newDeadline;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getNextId(Context context) throws FileNotFoundException {
        File idFile = new File(context.getFilesDir(), context.getResources().getString(R.string.path__deadline_ids));
        Scanner s = new Scanner(idFile);

        int id = s.nextInt();
        s.close();

        PrintWriter pw = new PrintWriter(idFile);
        pw.println(id + 1);
        pw.close();

        return id;
    }

    public void saveChanges(Context context) throws IOException {
        this.delete(context);
        this.save(context);
    }

    public void save(Context context) throws IOException {
        File location = new File(context.getFilesDir(), this.location);
        if (location.exists()) {
            /*
             * This deadline is stored so just overwrite it.
             */
            PrintWriter pw = new PrintWriter(location);
            pw.print(this.jsonString());
            pw.close();
        } else {
            if (location.createNewFile()) {
                PrintWriter pw = new PrintWriter(location);
                pw.print(this.jsonString());
                pw.close();
                this.saveToUpcoming(context);
                Log.d(TAG, "Created file: " + this.location);
            } else {
                Log.d(TAG, "Could not create file: " + this.location);
            }
        }
    }

    private void saveToUpcoming(Context context) throws IOException {
        File upcomingDeadlinesFile = new File(context.getFilesDir(), context.getString(R.string.path__deadlines_upcoming));
        Scanner scnr = new Scanner(upcomingDeadlinesFile).useDelimiter("\\Z");

        try {
            JSONObject json = new JSONObject(scnr.next());
            scnr.close();

            JSONArray arr = new JSONArray();
            arr.put(this.getDeadline().getTimeInMillis());
            Log.d(TAG, Arrays.toString(this.location.split("/")));
            Log.d(TAG, this.location.split("/")[this.location.split("/").length-1]);
            Log.d(TAG, Arrays.toString(this.location.split("/")[this.location.split("/").length-1].split("\\.")));
            Log.d(TAG, this.location.split("/")[this.location.split("/").length-1].split("\\.")[0]);

            arr.put(Integer.valueOf(this.location.split("/")[this.location.split("/").length-1].split("\\.")[0]));
            try {
                json.getJSONArray("deadlines").put(arr);

                PrintWriter pw = new PrintWriter(upcomingDeadlinesFile);
                pw.print(json.toString(4));
                pw.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void delete(Context context) {
        File location = new File(context.getFilesDir(), this.location);

        if (location.exists()) {
            location.delete();
            try {
                this.removeFromUpcoming(context);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeFromUpcoming(Context context) throws FileNotFoundException {
        File upcomingDeadlinesFile = new File(context.getFilesDir(), context.getString(R.string.path__deadlines_upcoming));
        Scanner scnr = new Scanner(upcomingDeadlinesFile).useDelimiter("\\Z");

        try {
            JSONObject json = new JSONObject(scnr.next());
            scnr.close();

            JSONArray deadlines = json.getJSONArray("deadlines");
            for (int i = 0; i < deadlines.length(); i ++) {
                JSONArray deadlineArr = deadlines.getJSONArray(i);
                int id = deadlineArr.getInt(1);

                if (id == Integer.valueOf(this.location.split("/")[this.location.split("/").length-1].split("\\.")[0])) {
                    deadlines.remove(i);
                }
            }

            PrintWriter pw = new PrintWriter(upcomingDeadlinesFile);
            pw.print(json.toString(4));
            pw.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Context context, int id) {
        try {
            Deadline toDelete = new Deadline(context, id);
            toDelete.delete(context);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "deadline: " + id + " doesn't exist.");
        }
    }

    public String getDeadlineString() {
        return (this.deadline.get(Calendar.MONTH) + 1) + "/" + this.deadline.get(Calendar.DAY_OF_MONTH);
    }

    public String getDescriptionString() {
        return this.description;
    }

    public String toString() {
        return this.deadline.toString() + " -- " + this.description + " with " + this.benchmarks.size() + " benchamrks";
    }

    public String jsonString() {
        JSONObject json = this.jsonify();

        try {
            return json.toString(4);
        } catch (JSONException e) {
            return json.toString();
        }
    }

    public JSONObject jsonify() {
        JSONObject json = new JSONObject();

        try {
            json.put("id", this.id);
        } catch (JSONException e) {

        }
        try {
            json.put("description", this.description);
        } catch (JSONException e) {

        }
        try {
            json.put("notes", this.notes);
        } catch (JSONException e) {

        }
        try {
            json.put("deadline", this.deadline.getTimeInMillis());
        } catch (JSONException e) {

        }
        try {
            json.put("alerted12", this.alerted12);
        } catch (JSONException e) {

        }
        try {
            json.put("alerted2", this.alerted2);
        } catch (JSONException e) {

        }
        try {
            json.put("alertedDue", this.alertedDue);
        } catch (JSONException e) {

        }
        try {
            ArrayList<ArrayList<Object>> benchmarks = new ArrayList<ArrayList<Object>>();
            for (BenchMark b: this.benchmarks) {
                ArrayList<Object> benchmark = new ArrayList<Object>();
                benchmark.add(b.getDescription());
                benchmark.add(b.getDeadline().getTimeInMillis());
                benchmarks.add(benchmark);
            }
            JSONArray ja = new JSONArray(benchmarks);
            json.put("benchmarks", ja);
        } catch (JSONException e) {

        }

        return json;
    }
}