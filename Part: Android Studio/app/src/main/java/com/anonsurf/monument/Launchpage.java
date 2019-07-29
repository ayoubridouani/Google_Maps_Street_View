package com.anonsurf.monument;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class Launchpage extends AppCompatActivity {
    private static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_page);

        activity = this;

        //to change every time the background image after launching this app
        RelativeLayout layout = findViewById(R.id.layout_home);
        int drawable;
        int random = (int)(Math.random() * 5 + 1);
        switch(random){
            case 1:
                drawable = R.drawable.homepage1;
                break;
            case 2:
                drawable = R.drawable.homepage2;
                break;
            case 3:
                drawable = R.drawable.homepage3;
                break;
            case 4:
                drawable = R.drawable.homepage4;
                break;
            case 5:
                drawable = R.drawable.homepage5;
                break;
            default:
                drawable = 0;
                break;
        }

        //to support the background image setting in every platforms android
        final int sdk = android.os.Build.VERSION.SDK_INT; //my current SDK = 15
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), drawable));
        } else {
            layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), drawable));
        }

        //check internet connection after launching app
        CheckInternet main = new CheckInternet(this);
        main.execute();
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }
}