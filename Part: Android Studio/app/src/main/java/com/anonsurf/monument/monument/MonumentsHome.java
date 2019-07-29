package com.anonsurf.monument.monument;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anonsurf.monument.HorizontalListView;
import com.anonsurf.monument.R;
import com.anonsurf.monument.authentication.Signin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MonumentsHome extends AppCompatActivity implements View.OnClickListener{
    private TextView buttonMore1;
    private TextView buttonMore2;
    private HorizontalListView horizontalListView1;
    private HorizontalListView horizontalListView2;
    private HorizontalAdapter customeAdapter1;
    private HorizontalAdapter customeAdapter2;
    private ProgressDialog progressBar;
    private SharedPreferences shared;
    private static AppCompatActivity activity;
    private final static int MAX_DISPLAY_ITEMS = 5;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monuments_home);

        shared = getSharedPreferences("session", MODE_PRIVATE);
        buttonMore1 = findViewById(R.id.more1);
        buttonMore2 = findViewById(R.id.more2);

        buttonMore1.setOnClickListener(this);
        buttonMore2.setOnClickListener(this);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait until finish....");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        activity = this;
        context = this;

        horizontalListView1 = findViewById(R.id.HorizontalListView1);
        horizontalListView2 = findViewById(R.id.HorizontalListView2);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url1 = "http://unscanned-solvents.000webhostapp.com/getMonuments.php";
        StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<MonumentModel> list = new ArrayList<>();

                try {
                    JSONArray obj = new JSONArray(response);
                    int i=0;
                    while(i<obj.length()) {
                        final MonumentModel monument = new MonumentModel();

                        JSONObject c = obj.getJSONObject(i);
                        monument.setId(c.getInt("id"));
                        monument.setTitle(c.getString("title"));

                        final String img = c.getString("image1");

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://unscanned-solvents.000webhostapp.com/images/" + img);
                                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    monument.setBmp(bmp);
                                } catch (Exception e) {
                                    Log.d("Error:",e.getMessage());
                                }
                            }
                        });

                        thread.start();

                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            Log.d("Error:",e.getMessage());
                        }

                        list.add(monument);
                        if(i == MAX_DISPLAY_ITEMS) break;
                        i++;
                    }
                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: " + response + "");
                }
                customeAdapter1 = new HorizontalAdapter(context,list);
                horizontalListView1.setAdapter(customeAdapter1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        horizontalListView1.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView idP = view.findViewById(R.id.idP);

                    if (!shared.getBoolean("asguest", true)) {
                        Intent intent = new Intent(context, DescribeMonument.class);
                        intent.putExtra("id", idP.getText().toString());
                        intent.putExtra("typeURL", "Monument");
                        startActivity(intent);
                    }
                    else {
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
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Information");
                        alert.show();
                    }
                }
            });

        String url2 = "http://unscanned-solvents.000webhostapp.com/getVilles.php";
        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<MonumentModel> list = new ArrayList<>();

                try {
                    JSONArray obj = new JSONArray(response);
                    int i=0;
                    while(i<obj.length()) {
                        final MonumentModel monument = new MonumentModel();

                        JSONObject c = obj.getJSONObject(i);
                        monument.setId(c.getInt("id"));
                        monument.setTitle(c.getString("title"));

                        final String img = c.getString("image1");

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://unscanned-solvents.000webhostapp.com/images/" + img);
                                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    monument.setBmp(bmp);
                                } catch (Exception e) {
                                    Log.d("Error:",e.getMessage());
                                }
                            }
                        });

                        thread.start();

                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            Log.d("Error:",e.getMessage());
                        }

                        list.add(monument);
                        if(i == MAX_DISPLAY_ITEMS) break;
                        i++;
                    }
                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: " + response + "");
                }
                customeAdapter2 = new HorizontalAdapter(context,list);
                horizontalListView2.setAdapter(customeAdapter2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        horizontalListView2.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView idP = view.findViewById(R.id.idP);
                        if (!shared.getBoolean("asguest", true)) {
                            Intent intent = new Intent(context, DescribeMonument.class);
                            intent.putExtra("id", idP.getText().toString());
                            intent.putExtra("typeURL","Ville");
                            startActivity(intent);
                        } else {
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
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.setTitle("Information");
                            alert.show();
                        }
                    }
                });

        queue.add(request1);
        queue.add(request2);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                progressBar.cancel();
                progressBar.dismiss();
            }
        });
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
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.more1){
            Intent intent = new Intent(context, AllMonuments.class);
            intent.putExtra("typeURL","Monument");
            startActivity(intent);
        }
        if(v.getId() == R.id.more2){
            Intent intent = new Intent(context, AllMonuments.class);
            intent.putExtra("typeURL","Ville");
            startActivity(intent);
        }
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }
}
