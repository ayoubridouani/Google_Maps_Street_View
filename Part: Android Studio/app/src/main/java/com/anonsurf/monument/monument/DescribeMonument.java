package com.anonsurf.monument.monument;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anonsurf.monument.R;
import com.anonsurf.monument.authentication.Signin;

import org.json.JSONObject;

import java.net.URL;

public class DescribeMonument extends AppCompatActivity implements View.OnClickListener{
    private ImageView img1;
    private ImageView img2;
    private TextView about;
    private TextView description;
    private ProgressDialog progressBar;
    private Context context;
    private Button panorama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_monument);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait until finish....");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        about = findViewById(R.id.about);
        description = findViewById(R.id.description);
        panorama = findViewById(R.id.panorama);
        context = this;

        panorama.setOnClickListener(this);

        if(getIntent() != null) {
            Intent intent = getIntent();
            String url;
            if (intent.getStringExtra("id").equals("-99999")) {
                startActivity(new Intent(context, NotFoundMonument.class));
                finish();
            } else {
                if(intent.getBooleanExtra("fromQR",false))
                    url = "http://unscanned-solvents.000webhostapp.com/getMonument.php?id=" + intent.getStringExtra("id");
                else
                    url = "http://unscanned-solvents.000webhostapp.com/get"+ intent.getStringExtra("typeURL") +".php?id=" + intent.getStringExtra("id");

                RequestQueue queue = Volley.newRequestQueue(this);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.isEmpty()) {
                            try {
                                final JSONObject c = new JSONObject(response);
                                getSupportActionBar().setTitle(c.getString("title"));

                                about.setText(c.getString("about"));
                                description.setText(c.getString("description"));

                                Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            URL url = new URL("http://unscanned-solvents.000webhostapp.com/images/" + c.getString("image1"));
                                            Log.d("url1",url.toString());
                                            img1.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                Thread thread2 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            URL url = new URL("http://unscanned-solvents.000webhostapp.com/images/" + c.getString("image2"));
                                            Log.d("url2",url.toString());
                                            img2.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                thread1.start();
                                thread2.start();

                                try {
                                    thread1.join();
                                    thread2.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } catch (Throwable tx) {
                                Log.e("My App:", "Could not parse malformed JSON: " + response + "");
                            }
                        }else{
                            startActivity(new Intent(context, NotFoundMonument.class));
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.getMessage());
                    }
                });
                queue.add(request);
                queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
                    @Override
                    public void onRequestFinished(Request<String> request) {
                        progressBar.cancel();
                        progressBar.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        menu.getItem(Menu.FIRST+1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.profileMenu:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.scanQRMenu:
                Intent intent = new Intent(this,ScanQR.class);
                intent.getBooleanExtra("panorama", false);
                startActivity(intent);
                break;
            case R.id.exitMenu:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("are you want to log out ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences shared = getSharedPreferences("session", MODE_PRIVATE);
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("fname", "");
                                editor.putString("lname", "");
                                editor.putString("user", "");
                                editor.putString("email", "");
                                editor.putBoolean("asguest", true);
                                editor.apply();
                                Intent intent = new Intent(context, Signin.class);
                                if(MonumentsHome.getActivity() != null) MonumentsHome.getActivity().finish();
                                if(AllMonuments.getActivity() != null) AllMonuments.getActivity().finish();
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Exit Application");
                alert.show();
                break;
            default:
                break;
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.panorama){
            Intent intent = new Intent(DescribeMonument.this,ScanQR.class);
            intent.putExtra("panorama",true);
            startActivity(intent);
        }
    }
}
