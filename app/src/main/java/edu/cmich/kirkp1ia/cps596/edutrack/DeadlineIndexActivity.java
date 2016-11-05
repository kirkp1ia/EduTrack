package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

public class DeadlineIndexActivity extends AppCompatActivity {

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
            Deadline d = new Deadline(this, "2/26/1996 3:12 pm", "Test notes", "Test description");
            d.addBenchMark("Test benchark", "2/20/1996 3:00 pm");
            d.save(this);
            Log.d(TAG, "Created");

            Deadline d2 = new Deadline(this, 1);
            Log.d(TAG, d2.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDeadlinePressed(View v) {
        Intent addDeadlineIntent = new Intent(this, AddDeadline.class);
        this.startActivity(addDeadlineIntent);
    }
}
