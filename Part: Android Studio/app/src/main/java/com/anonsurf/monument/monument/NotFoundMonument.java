package com.anonsurf.monument.monument;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anonsurf.monument.R;

public class NotFoundMonument extends AppCompatActivity implements View.OnClickListener{
    private TextView retry;
    private Intent intent_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_found_monument);
        if(getIntent()!= null) {
            intent_s = getIntent();
            retry = findViewById(R.id.retry);
            retry.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.retry){
            Intent intent = new Intent(this,ScanQR.class);
            intent.putExtra("panorama", intent_s.getBooleanExtra("panorama", false));
            startActivity(intent);
            finish();
        }
    }
}
