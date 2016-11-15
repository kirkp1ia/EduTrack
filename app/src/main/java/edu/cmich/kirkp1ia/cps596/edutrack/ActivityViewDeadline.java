package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;

import edu.cmich.kirkp1ia.cps596.edutrack.core.Deadline;

/**
 * Created by kirkp1ia on 11/15/16.
 */

public class ActivityViewDeadline extends ActivityAddDeadline {

    protected Deadline editingDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            load(extras.getInt("deadline_id"));
        }
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
        super.refreshUI();

        Button submitButton = (Button) this.findViewById(R.id.btn_submit_deadline);
        submitButton.setText("Submit Changes");
    }

    @Override
    public void finishPressed(View v) {
//        Calendar deadline = this.getInputDeadline();
//        String desc = ((EditText) this.findViewById(R.id.input_deadline_desc)).getText().toString();
//        String notes = ((EditText) this.findViewById(R.id.input_deadline_notes)).getText().toString();
//
//        try {
//            Deadline deadlineObj = new Deadline(this.getApplicationContext(), deadline, notes, desc);
//            deadlineObj.addBenchMarks(this.benchmarksToAdd);
//            deadlineObj.save(this.getApplicationContext());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
        Intent indexIntent = new Intent(this, ActivityDeadlineIndex.class);
        this.startActivity(indexIntent);
    }
}
