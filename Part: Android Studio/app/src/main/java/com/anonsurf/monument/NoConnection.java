package com.anonsurf.monument;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class NoConnection extends AppCompatActivity implements View.OnClickListener{
    private static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button tryagain = findViewById(R.id.tryagain);
        activity = this;

        tryagain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CheckInternet main = new CheckInternet(this);
        main.execute();
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }

    // close this activity and return to preview activity (if there is any)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}