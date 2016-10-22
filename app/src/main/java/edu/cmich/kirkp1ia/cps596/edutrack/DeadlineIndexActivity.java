package edu.cmich.kirkp1ia.cps596.edutrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DeadlineIndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline_index);
    }

    public void addDeadlinePressed(View v) {
        Intent addDeadlineIntent = new Intent(this, AddDeadline.class);
        this.startActivity(addDeadlineIntent);
    }
}
