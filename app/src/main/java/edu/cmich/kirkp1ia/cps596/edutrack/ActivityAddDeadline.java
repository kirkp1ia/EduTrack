package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TimePicker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import edu.cmich.kirkp1ia.cps596.edutrack.core.BenchMark;
import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

public class ActivityAddDeadline extends AppCompatActivity {

    private final String TAG = "Deadline Add";

    // Each array list in this list is a pairing of name, deadlinePicker EditText inputs.//
    private ArrayList<BenchMark> benchmarksToAdd = new ArrayList<BenchMark>();
    private ArrayAdapter<BenchMark> benchmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deadline);

        this.refreshUI();
    }

    private void refreshUI() {
        this.benchmarkAdapter = new ArrayAdapter<BenchMark>(this, R.layout.listview_item_benchmark, this.benchmarksToAdd);
        ListView benchmarkView = (ListView) this.findViewById(R.id.view_benchmarks);
        benchmarkView.setAdapter(this.benchmarkAdapter);

        final ScrollView parentScrollView = (ScrollView) findViewById(R.id.rootView);
        final ScrollView childScrollView = (ScrollView) findViewById(R.id.benchmark_scroll_view);

        parentScrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                childScrollView.getParent().requestDisallowInterceptTouchEvent(
                        false);
                // We will have to follow above for all scrollable contents
                return false;
            }
        });
        childScrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                // this will disallow the touch request for parent scroll on
                // touch of child view
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        benchmarkView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                // this will disallow the touch request for parent scroll on
                // touch of child view
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ActivityAddBenchmark.REQUEST_NEW_BENCHMARK) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, data.getExtras().getString("description"));
                this.benchmarksToAdd.add(new BenchMark(data.getExtras().getString("description"),
                        data.getExtras().getIntArray("date/time")));
                Log.d(TAG, data.getExtras().getString("description") + " - adding to list with " + (this.benchmarksToAdd.size() - 1) + " items in it.");
                this.refreshUI();
            }
        }
    }

    public Calendar getInputDeadline() {
        DatePicker deadlinePicker = (DatePicker) this.findViewById(R.id.deadline_date_picker);
        TimePicker deadlineTimePicker = (TimePicker) this.findViewById(R.id.deadline_time_picker);

        int day = deadlinePicker.getDayOfMonth();
        int month = deadlinePicker.getMonth();
        int year = deadlinePicker.getYear();

        int hour = deadlineTimePicker.getHour();
        int minute = deadlineTimePicker.getMinute();

        Calendar deadlineCal = Calendar.getInstance();
        deadlineCal.set(year, month, day, hour, minute);

        return deadlineCal;
    }

    public void addBenchmarkPressed(View v) {
        Log.d(this.TAG, "New Benchmark");

        Intent addBenchmarkIntent = new Intent(this, ActivityAddBenchmark.class);
        this.startActivityForResult(addBenchmarkIntent, ActivityAddBenchmark.REQUEST_NEW_BENCHMARK);
    }

    public void finishPressed(View v) {
        Calendar deadline = this.getInputDeadline();
        String desc = ((EditText) this.findViewById(R.id.input_deadline_desc)).getText().toString();
        String notes = ((EditText) this.findViewById(R.id.input_deadline_notes)).getText().toString();

        try {
            Deadline deadlineObj = new Deadline(this.getApplicationContext(), deadline, notes, desc);
            deadlineObj.addBenchMarks(this.benchmarksToAdd);
            deadlineObj.save(this.getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent indexIntent = new Intent(this, ActivityDeadlineIndex.class);
        this.startActivity(indexIntent);
    }
}
