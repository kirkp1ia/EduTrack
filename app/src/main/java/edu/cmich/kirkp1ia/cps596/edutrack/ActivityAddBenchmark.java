package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by kirkp1ia on 11/13/16.
 */

public class ActivityAddBenchmark extends AppCompatActivity {

    private static String TAG = "Add Benchmark";

    public static final int REQUEST_NEW_BENCHMARK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_benchmark);
        this.refreshUI();
    }

    private void refreshUI() {
        DatePicker benchmarkDatePicker = (DatePicker) this.findViewById(R.id.benchmark_date_picker);
        benchmarkDatePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
    }

    public void finalizeAddBenchmark(View button) {
        DatePicker deadlinePicker = (DatePicker) this.findViewById(R.id.benchmark_date_picker);
        TimePicker deadlineTimePicker = (TimePicker) this.findViewById(R.id.benchmark_time_picker);
        EditText descriptionInput = (EditText) this.findViewById(R.id.input_benchmark_desc);

        int day = deadlinePicker.getDayOfMonth();
        int month = deadlinePicker.getMonth();
        int year = deadlinePicker.getYear();

        int hour = deadlineTimePicker.getHour();
        int minute = deadlineTimePicker.getMinute();

        Intent finishIntent = new Intent();
        finishIntent.putExtra("date/time", new int[]{month, day, year, hour, minute});
        finishIntent.putExtra("description", descriptionInput.getText().toString());
        this.setResult(RESULT_OK, finishIntent);
        this.finish();
    }
}
