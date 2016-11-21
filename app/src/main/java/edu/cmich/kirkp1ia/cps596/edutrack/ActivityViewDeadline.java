package edu.cmich.kirkp1ia.cps596.edutrack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TimePicker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

/**
 * Created by kirkp1ia on 11/15/16.
 */

public class ActivityViewDeadline extends ActivityAddDeadline {

    public static final String TAG = "View Deadline";

    protected Deadline editingDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = this.getIntent().getExtras();
        Log.d(TAG, Arrays.toString(extras.keySet().toArray()));
        if (extras != null) {
            load(extras.getInt("deadline_id"));
        }

        super.onCreate(savedInstanceState);
    }

    protected void load(int id) {
        try {
            this.editingDeadline = new Deadline(this.getApplicationContext(), id);
        } catch (FileNotFoundException e) {
            // Revert back to index
            Intent indexIntent = new Intent(this, ActivityDeadlineIndex.class);
            this.startActivity(indexIntent);
        }
    }

    @Override
    protected void refreshUI() {
        this.benchmarksToAdd = this.editingDeadline.getBenchmarks();

        super.refreshUI();

        Button submitButton = (Button) this.findViewById(R.id.btn_submit_deadline);
        submitButton.setText("Submit Changes");

        Button deleteButton = new Button(this.getApplicationContext());
        deleteButton.setId(R.id.btn_view_deadline_delete);
        TableRow.LayoutParams delBtnParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        deleteButton.setLayoutParams(delBtnParams);
        deleteButton.setText("Delete Deadline");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingDeadline.delete(v.getContext());
                Intent indexIntent = new Intent(v.getContext(), ActivityDeadlineIndex.class);
                startActivity(indexIntent);
            }
        });

        LinearLayout root = (LinearLayout) this.findViewById(R.id.add_deadline_view_content);
        if (this.findViewById(R.id.btn_view_deadline_delete) == null) {
            root.addView(deleteButton);
        }

        Button markDoneButton = new Button(this.getApplicationContext());
        markDoneButton.setId(R.id.btn_view_deadline_markdone);
        TableRow.LayoutParams mkDoneBtnParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        markDoneButton.setLayoutParams(delBtnParams);
        markDoneButton.setText("Complete Deadline");

        markDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingDeadline.delete(v.getContext());
                Intent indexIntent = new Intent(v.getContext(), ActivityDeadlineIndex.class);
                startActivity(indexIntent);
            }
        });

        if (this.findViewById(R.id.btn_view_deadline_markdone) == null) {
            root.addView(markDoneButton);
        }

        DatePicker dpicker = (DatePicker) this.findViewById(R.id.deadline_date_picker);
        TimePicker tpicker = (TimePicker) this.findViewById(R.id.deadline_time_picker);
        EditText desc = (EditText) this.findViewById(R.id.input_deadline_desc);
        EditText notes = (EditText) this.findViewById(R.id.input_deadline_notes);

        int year = this.editingDeadline.getDeadline().get(Calendar.YEAR);
        int month = this.editingDeadline.getDeadline().get(Calendar.MONTH);
        int day = this.editingDeadline.getDeadline().get(Calendar.DAY_OF_MONTH);
        int hour = this.editingDeadline.getDeadline().get(Calendar.HOUR_OF_DAY);
        int minute = this.editingDeadline.getDeadline().get(Calendar.MINUTE);

        dpicker.updateDate(year, month, day);
        tpicker.setHour(hour);
        tpicker.setMinute(minute);

        desc.setText(this.editingDeadline.getDescription());
        notes.setText(this.editingDeadline.getNotes());

        ListView benchmarksView = (ListView) this.findViewById(R.id.view_benchmarks);

        final Context context = this;

        benchmarksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.d(TAG, "click!");
                AlertDialog.Builder markCompletePrompt = new AlertDialog.Builder(context);
                markCompletePrompt.setTitle("Mark Benchmark Complete");
                markCompletePrompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Deadline d = editingDeadline.removeBenchmark(getApplicationContext(), position);
                            d.save(context);
                            finish();
                            Intent intent = new Intent(getApplicationContext(), ActivityViewDeadline.class);
                            intent.putExtra("deadline_id", d.getId());
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                });
                markCompletePrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog prompt = markCompletePrompt.create();
                prompt.show();
            }

        });
    }

    @Override
    public void finishPressed(View v) {
        this.editingDeadline.delete(this.getApplicationContext());

        super.finishPressed(v);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
