package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AddDeadline extends AppCompatActivity {

    private final String TAG = "Deadline Add";

    // Each array list in this list is a pairing of name, deadlinePicker EditText inputs.//
    private ArrayList<ArrayList<EditText>> benchmarksToAdd = new ArrayList<ArrayList<EditText>>();
    private boolean addingBenchmark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadline);
    }

    private LinearLayout newBenchmark() {
        LinearLayout newBenchmarkLayout = new LinearLayout(this.getApplicationContext());
        newBenchmarkLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText name = new EditText(this.getApplicationContext());
        EditText deadlinePicker = new EditText((this.getApplicationContext()));

        deadlinePicker.setHint("Deadline");
        name.setHint("Description");
        name.setHintTextColor(Color.GRAY);
        name.setTextColor(Color.GRAY);
        deadlinePicker.setHintTextColor(Color.GRAY);
        deadlinePicker.setTextColor(Color.GRAY);

        newBenchmarkLayout.addView(deadlinePicker);
        newBenchmarkLayout.addView(name);

        ArrayList<EditText> input = new ArrayList<EditText>();
        input.add(name);
        input.add(deadlinePicker);
        this.benchmarksToAdd.add(input);

        return newBenchmarkLayout;
    }

    public void addBenchmarkPressed(View v) {
        Log.d(this.TAG, "New Benchmark");

        if (this.addingBenchmark) {
            LinearLayout.LayoutParams benchmarkAddLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout newBenchmarkLayout = this.newBenchmark();
            ((LinearLayout) this.findViewById(R.id.view_benchmarks)).addView(newBenchmarkLayout);
            this.addingBenchmark = true;
        }
    }

    public void finishPressed(View v) {
        Intent indexIntent = new Intent(this, DeadlineIndexActivity.class);
        this.startActivity(indexIntent);
    }
}
