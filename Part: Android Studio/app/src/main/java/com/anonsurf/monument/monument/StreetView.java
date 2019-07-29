package com.anonsurf.monument.monument;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.anonsurf.monument.R;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class StreetView extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {

    public static final String TAG = StreetView.class.getSimpleName();
    private static final Integer PANORAMA_CAMERA_DURATION = 9000;

    StreetViewPanorama streetViewPanorama;
    StreetViewPanoramaFragment streetViewPanoramaFragment;
    private static final String STREET_VIEW_BUNDLE = "StreetViewBundle";

    private double latitude;
    private double longtitude;
    private LatLng latLng;

    private StreetViewPanorama.OnStreetViewPanoramaChangeListener streetViewPanoramaChangeListener
            = streetViewPanoramaLocation -> Log.e(TAG, "Street View Panorama Change Listener");
    private StreetViewPanorama.OnStreetViewPanoramaClickListener streetViewPanoramaClickListener
            = (orientation -> {
        Point point = streetViewPanorama.orientationToPoint(orientation);
        if (point != null) {
            streetViewPanorama.animateTo(
                    new StreetViewPanoramaCamera.Builder()
                            .orientation(orientation)
                            .zoom(streetViewPanorama.getPanoramaCamera().zoom)
                            .build(), PANORAMA_CAMERA_DURATION);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streetview);

        Intent intent = getIntent();
        if (intent.getType().equals("text/plain")) {
            String strLatLng = intent.getStringExtra(Intent.EXTRA_TEXT);
            if(Pattern.matches("-?[0-9]+\\.[0-9]*:-?[0-9]+\\.[0-9]*", strLatLng)) {
                StringTokenizer tokenizer = new StringTokenizer(strLatLng, ":");
                latitude = Double.parseDouble(tokenizer.nextToken());
                longtitude = Double.parseDouble(tokenizer.nextToken());
                latLng = new LatLng(latitude, longtitude);
            }else{
                Intent intent2 = new Intent(this, NotFoundMonument.class);
                intent2.putExtra("panorama", intent.getBooleanExtra("panorama", true));
                startActivity(intent2);
                finish();
                return;
            }
        }

        streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetView);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        Bundle streetViewBundle = null;
        if (savedInstanceState != null)
            streetViewBundle = savedInstanceState.getBundle(STREET_VIEW_BUNDLE);
        streetViewPanoramaFragment.onCreate(streetViewBundle);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        this.streetViewPanorama = streetViewPanorama;
        Log.d("Lat Lng values:", "--------------" + latLng.toString());
        this.streetViewPanorama.setPosition(latLng);
        this.streetViewPanorama.setOnStreetViewPanoramaChangeListener(streetViewPanoramaChangeListener);
        this.streetViewPanorama.setOnStreetViewPanoramaClickListener(streetViewPanoramaClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        streetViewPanoramaFragment.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle mStreetViewBundle = outState.getBundle(STREET_VIEW_BUNDLE);
        if (mStreetViewBundle == null) {
            mStreetViewBundle = new Bundle();
            outState.putBundle(STREET_VIEW_BUNDLE, mStreetViewBundle);
        }
        streetViewPanoramaFragment.onSaveInstanceState(mStreetViewBundle);
    }

    @Override
    protected void onStop() {
        super.onStop();
        streetViewPanoramaFragment.onStop();
    }
}
