package com.anonsurf.monument.monument;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anonsurf.monument.R;
import com.anonsurf.monument.authentication.Signin;
import com.anonsurf.monument.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class AllMonuments extends AppCompatActivity {
    private static AppCompatActivity activity;
    private ListView verticalListView;
    private ArrayAdapter<MonumentModel> customeAdapter;
    private Context context;
    private ActivityMainBinding activityMainBinding;
    private ArrayList<MonumentModel> list;
    private ProgressDialog progressBar;
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent()!=null) {
            Intent intent = getIntent();

            activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_monuments);

            verticalListView = findViewById(R.id.verticalListView);
            list = new ArrayList<>();
            shared = getSharedPreferences("session", MODE_PRIVATE);

            activity = this;
            context = this;

            getSupportActionBar().setTitle(intent.getStringExtra("typeURL") +"s");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            progressBar = new ProgressDialog(this);
            progressBar.setCancelable(false);
            progressBar.setMessage("Please wait until finish....");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();

            activityMainBinding.search.setVisibility(View.GONE);
            activityMainBinding.search.setActivated(true);
            activityMainBinding.search.setQueryHint("Type your keyword here");
            activityMainBinding.search.onActionViewExpanded();
            activityMainBinding.search.setIconified(false);
            activityMainBinding.search.clearFocus();

            String url = "http://unscanned-solvents.000webhostapp.com/get"+ intent.getStringExtra("typeURL") +"s.php";

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray obj = new JSONArray(response);
                        int i = 0;
                        while (i < obj.length()) {
                            JSONObject c = obj.getJSONObject(i);
                            final MonumentModel product = new MonumentModel();
                            product.setId(c.getInt("id"));
                            product.setTitle(c.getString("title"));

                            final String imgg = c.getString("image1");

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        URL url = new URL("http://unscanned-solvents.000webhostapp.com/images/" + imgg);
                                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        product.setBmp(bmp);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            thread.start();

                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                Log.d("Error:", e.getMessage());
                            }

                            i++;
                            list.add(product);
                        }
                    } catch (Throwable tx) {
                        Log.d("My App:", "Could not parse malformed JSON: " + response + "");
                    }
                    customeAdapter = new VerticalAdapter(context, R.layout.row_item_for_vertical_list, list);
                    verticalListView.setAdapter(customeAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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

            final String typeURL = intent.getStringExtra("typeURL");
            verticalListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView idP = view.findViewById(R.id.idP);
                            if (!shared.getBoolean("asguest", true)) {
                                Intent intent = new Intent(context, DescribeMonument.class);
                                intent.putExtra("id", idP.getText().toString());
                                intent.putExtra("typeURL", typeURL);
                                intent.putExtra("fromQR", false);
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

            activityMainBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    customeAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
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
            case R.id.searchMenu:
                activityMainBinding.search.setVisibility(View.VISIBLE);
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

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static AppCompatActivity getActivity() {
        return activity;
    }
}
