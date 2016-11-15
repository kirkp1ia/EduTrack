package edu.cmich.kirkp1ia.cps596.edutrack;

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

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

public class ActivityDeadlineIndex extends AppCompatActivity {

    public final static String TAG = "Deadline Index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline_index);

//        try {
//            Deadline d1 = new Deadline(this.getApplicationContext(), Calendar.getInstance(), "haha", "yoyoyoyoyo");
//            d1.save(this.getApplicationContext());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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

        this.loadDeadlines();
    }

    private void loadDeadlines() {
        LinearLayout display = (LinearLayout) this.findViewById(R.id.deadline_display_view);

        try {
            Deadline d = new Deadline(this.getApplicationContext(), 1);
            LinearLayout row = this.getDeadlineDisplay(d);
            display.addView(row);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Can't find deadline: " + 1);
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
