package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TimePicker;

import java.io.FileNotFoundException;
import java.util.Calendar;

import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

/**
 * Created by kirkp1ia on 11/15/16.
 */

public class ActivityViewDeadline extends ActivityAddDeadline {

    protected Deadline editingDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = this.getIntent().getExtras();
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
        root.addView(deleteButton);

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
    }

    @Override
    public void finishPressed(View v) {
        this.editingDeadline.delete(this.getApplicationContext());

        super.finishPressed(v);
    }
}
