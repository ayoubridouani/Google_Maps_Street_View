package com.anonsurf.monument.monument;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anonsurf.monument.R;
import com.anonsurf.monument.authentication.Signin;
import com.google.zxing.Result;

import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQR extends AppCompatActivity implements ZXingScannerView.ResultHandler, View.OnClickListener {
    private ZXingScannerView scannerView;
    private SharedPreferences shared;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        shared = getSharedPreferences("session", MODE_PRIVATE);
        context = this;

        if(!shared.getBoolean("asguest", true)) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("You must be login to get all infos for this monument, login now ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(context, Signin.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Information");
            alert.show();
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            setContentView(scannerView);
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setContentView(scannerView);
                } else {
                    setContentView(R.layout.turn_on_permission);
                    TextView turnOnForQr = findViewById(R.id.turnOnForQr);
                    turnOnForQr.setOnClickListener(this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void handleResult(Result result) {
        if (getIntent() != null) {
            Intent intent_s = getIntent();
            byte[] data = null;
            boolean goodformat = Pattern.matches("[A-Za-z0-9=]*", result.getText());
            if(goodformat) {
                try {
                    data = Base64.decode(result.getText(), Base64.DEFAULT);
                }catch(IllegalArgumentException e){
                    Intent intent = new Intent(context, NotFoundMonument.class);
                    intent.putExtra("panorama", intent_s.getBooleanExtra("panorama", false));
                    startActivity(intent);
                    finish();
                    return;
                }

                String res = new String(data);

                //when the user comes from menu (SQAN QR)
                if (intent_s.getBooleanExtra("panorama", false) == false) {
                    boolean bool = Pattern.matches("[0-9]*", res);
                    if (!bool) res = "-99999";
                    Intent intent = new Intent(this, DescribeMonument.class);
                    intent.putExtra("id", res);
                    intent.putExtra("fromQR", true);
                    startActivity(intent);
                }
                //when the user comes from panorama (STREET VIEW)
                else {
                    Intent intent = new Intent(ScanQR.this, StreetView.class);
                    intent.putExtra(Intent.EXTRA_TEXT, res);
                    intent.setType("text/plain");
                    startActivity(intent);
                }
            }else{
                Intent intent = new Intent(context, NotFoundMonument.class);
                intent.putExtra("panorama", intent_s.getBooleanExtra("panorama", false));
                startActivity(intent);
            }
        }
        finish();
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.turnOnForQr){
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
    }
}
