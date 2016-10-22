package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddDeadline extends AppCompatActivity {

    private final String TAG = "Deadline Add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadline);
    }

    private LinearLayout newBenchmark() {
        LinearLayout newBenchmarkLayout = new LinearLayout(this.getApplicationContext());
        newBenchmarkLayout.setOrientation(LinearLayout.HORIZONTAL);


        final EditText name = new EditText(this.getApplicationContext());
        final EditText deadlinePicker = new EditText((this.getApplicationContext()));

        deadlinePicker.setHint("Deadline");
        name.setHint("Description");
        name.setHintTextColor(Color.GRAY);
        name.setTextColor(Color.GRAY);
        deadlinePicker.setHintTextColor(Color.GRAY);
        deadlinePicker.setTextColor(Color.GRAY);

        newBenchmarkLayout.addView(deadlinePicker);
        newBenchmarkLayout.addView(name);

        return newBenchmarkLayout;
    }

    public void addBenchmarkPressed(View v) {
        Log.d(this.TAG, "New Benchmark");

        final LinearLayout newBenchmarkLayout = this.newBenchmark();

        ((LinearLayout) this.findViewById(R.id.view_benchmarks)).addView(newBenchmarkLayout);
    }

    public void finishPressed(View v) {
        Intent indexIntent = new Intent(this, DeadlineIndexActivity.class);
        this.startActivity(indexIntent);
    }
}
