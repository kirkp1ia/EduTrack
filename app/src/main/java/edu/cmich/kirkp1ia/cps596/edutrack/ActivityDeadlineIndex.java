package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

public class ActivityDeadlineIndex extends AppCompatActivity {

    public final static String TAG = "Deadline Index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline_index);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            try {
                if (new Initialization(this).initialize()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstTime", true);
                    editor.commit();
                    Log.d(TAG, "Finished initialization.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.loadDeadlines(this.getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadDeadlines(Context context) throws FileNotFoundException {
        LinearLayout display = (LinearLayout) this.findViewById(R.id.deadline_display_view);

        JSONArray allFuture = this.getAllFutureDeadlines(context);

        for (int i = 0; i < allFuture.length(); i ++) {
            try {
                JSONArray deadlineArr = allFuture.getJSONArray(i);
                long deadline = deadlineArr.getLong(0);
                int deadlineId = deadlineArr.getInt(1);

                Calendar now = Calendar.getInstance();
                long daysBetween = deadline - now.getTimeInMillis();

                if (daysBetween <= Long.valueOf(this.getString(R.string.millis_in_seven_days))) {
                    Deadline d = new Deadline(context, deadlineId);
                    LinearLayout deadlineDisplay = this.getDeadlineDisplay(d);

                    display.addView(deadlineDisplay);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONArray getAllFutureDeadlines(Context context) throws FileNotFoundException {
        File upcomingDeadlines = new File(context.getFilesDir(), this.getString(R.string.path__deadlines_upcoming));
        Scanner scnr = new Scanner(upcomingDeadlines).useDelimiter("\\Z");
        try {
            JSONObject json = new JSONObject(scnr.next());
            JSONArray deadlinesInFuture = json.getJSONArray("deadlines");
            scnr.close();
            return deadlinesInFuture;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private LinearLayout getDeadlineDisplay(final Deadline deadline) {

        Resources r = getResources();
        int threeeightteen = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 318, r.getDisplayMetrics());
        int twenty = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        int fifteen = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());

        TableRow.LayoutParams lableLayout = new TableRow.LayoutParams(threeeightteen, TableRow.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams deadlineLayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        TableRow.LayoutParams wide = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        /*
         * LinearLayout Horizontal
         */
        LinearLayout elementLayout = new LinearLayout(this.getApplicationContext());
        elementLayout.setLayoutParams(wide);
        elementLayout.setOrientation(LinearLayout.HORIZONTAL);

        /*
         * Display TextFields
         */
        TextView lableDisplay = new TextView(this.getApplicationContext());
        lableDisplay.setLayoutParams(lableLayout);
        lableDisplay.setText(deadline.getDescriptionString());
        lableDisplay.setPadding(twenty, fifteen, twenty, fifteen);

        TextView deadlineDisplay = new TextView(this.getApplicationContext());
        deadlineDisplay.setLayoutParams(deadlineLayout);
        deadlineDisplay.setText(deadline.getDeadlineString());
        deadlineDisplay.setPadding(twenty, fifteen, twenty, fifteen);
        deadlineDisplay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        elementLayout.addView(lableDisplay);
        elementLayout.addView(deadlineDisplay);

        elementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = deadline.getId();
                Intent viewIntent = new Intent(v.getContext(), ActivityViewDeadline.class);
                viewIntent.putExtra("deadline_id", id);
                startActivity(viewIntent);
            }
        });

        return elementLayout;
    }

    public void addDeadlinePressed(View v) {
        Intent addDeadlineIntent = new Intent(this, ActivityAddDeadline.class);
        this.startActivity(addDeadlineIntent);
    }
}
