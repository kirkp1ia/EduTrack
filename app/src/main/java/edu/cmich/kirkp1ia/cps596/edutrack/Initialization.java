package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by kirkp1ia on 11/5/16.
 */

public class Initialization {

    private String TAG = "Initialization";

    private Context context;

    public Initialization(Context context) {
        this.context = context;
    }

    /**
     * Create storage folder and initialize other processes to
     * initialize the app environment.
     */
    public boolean initialize() throws IOException {
        try {
            this.TAG = this.context.getClass().getField("TAG").get(null) + ":" + "Initialization";
        } catch (NoSuchFieldException | IllegalAccessException e) {
            this.TAG = "Initialization";
        }

        Toast.makeText(this.context.getApplicationContext(), "Please wait while initial processes are being initialize.", Toast.LENGTH_LONG).show();

        /*
         * Create folder to contain all the deadlines
         */
        File deadlineFolder = new File(this.context.getFilesDir(), this.context.getResources().getString(R.string.path__deadline_storage));
        if (deadlineFolder.mkdir() || deadlineFolder.exists()) {
            Log.d(TAG, "Created deadline folder: " + deadlineFolder.getAbsolutePath());
            return this.initializeIdFile();
        } else {
            Log.d(TAG, "Cannot create deadline folder: " + deadlineFolder.getAbsolutePath());
            return false;
        }
    }

    /**
     * Create file to contain id's for different deadlines
     *
     * The id in this file will be used in initializing the next deadline created.
     */
    private boolean initializeIdFile() throws IOException {
        File idFile = new File(this.context.getFilesDir(), this.context.getResources().getString(R.string.path__deadline_ids));
        Log.d(TAG, "Trying to create id file: " + idFile.getPath());
        if (idFile.createNewFile() || idFile.exists()) {
            PrintWriter pw = new PrintWriter(idFile);
            pw.println("1");
            pw.close();
            Log.d(TAG, "Created id file: " + idFile.getAbsolutePath());

            this.initializeDeadlineFile();

            return true;
        } else {
            Log.d(TAG, "Cannot create id file: " + idFile.getAbsolutePath());
            return false;
        }
    }

    /**
     * Create file that lists all upcoming deadlines
     * @return
     */
    private boolean initializeDeadlineFile() throws IOException {
        File upcoming = new File(this.context.getFilesDir(), this.context.getString(R.string.path__deadlines_upcoming));
        Log.d(TAG, "Trying to create upcoming deadlines file");
        if (upcoming.createNewFile()) {
            this.initializeDeadlineFileContent(upcoming);
            Log.d(TAG, "Created upcoming deadlines file: " + upcoming.getAbsolutePath());
            return true;
        } else if(upcoming.exists()) {
            Log.d(TAG, "Upcoming deadlines file already exists.");
            return false;
        } else {
            Log.d(TAG, "Cannot create upcoming deadlines file: " + upcoming.getAbsolutePath());
            return false;
        }
    }

    /**
     * Print the starting data to the upcoming deadlines file.
     *
     * Format is as follows:
     *
     * {
     *     "deadlines": [
     *          [-deadline_long-, file_location],
     *          [-deadline_long-, file_location],
     *          (e.g.) [104353498, "14.ddln"],
     *          (e.g.) [104353498, "15.ddln"],
     *     ]
     * }
     * @return
     */
    private boolean initializeDeadlineFileContent(File deadlineFile) {
        try {
            JSONObject json = new JSONObject("{\"deadlines\": []}");
            PrintWriter pw = new PrintWriter(deadlineFile);
            pw.print(json.toString(4));
            pw.close();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }
}
