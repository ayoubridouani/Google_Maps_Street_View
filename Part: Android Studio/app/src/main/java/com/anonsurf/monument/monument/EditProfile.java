package com.anonsurf.monument.monument;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anonsurf.monument.R;
import com.anonsurf.monument.authentication.Signin;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText username;
    private EditText password;
    private Button edit;
    private Context context;
    private SharedPreferences shared;
    private String session_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        firstname = this.findViewById(R.id.firstname);
        lastname = this.findViewById(R.id.lastname);
        email = this.findViewById(R.id.email);
        username = this.findViewById(R.id.username);
        password = this.findViewById(R.id.password);
        edit = this.findViewById(R.id.register);

        session_user = "";
        shared = getSharedPreferences("session", MODE_PRIVATE);

        if(!shared.getBoolean("asguest", true)) {
            firstname.setText(shared.getString("fname", "No name defined"));
            lastname.setText(shared.getString("lname", "No name defined"));
            username.setText(shared.getString("user", "No name defined"));
            email.setText(shared.getString("email", "No name defined"));

            session_user = shared.getString("user", "No name defined");
            username.setEnabled(false);
            email.setEnabled(false);

            edit.setOnClickListener(this);
        }else{
            startActivity(new Intent(this, Signin.class));
            finish();
        }
        context = this;
        edit.setText("Save Change");
    }

    public EditText getFirstname() {
        return firstname;
    }

    public EditText getLastname() {
        return lastname;
    }

    public EditText getPassword() {
        return password;
    }

    @Override
    public void onClick(View v) {
        if (this.getFirstname().getText().toString().isEmpty()) {
            this.getFirstname().setError("enter your firstname");
        }
        if (this.getLastname().getText().toString().isEmpty()) {
            this.getLastname().setError("enter your lastname");
        }
        if (this.getPassword().getText().toString().isEmpty()) {
            this.getPassword().setError("enter your password");
        }
        if (!this.getFirstname().getText().toString().isEmpty() && !this.getLastname().getText().toString().isEmpty() && !this.getPassword().getText().toString().isEmpty()) {

            String url = "http://unscanned-solvents.000webhostapp.com/editUser.php?user=" + session_user;

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Modification reussie")) {
                        Toast.makeText(context, "your profile has been successfully updated", Toast.LENGTH_LONG).show();
                        password.setText("");
                    } else {
                        Log.d("Error:",response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error:",error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("fname", getFirstname().getText().toString());
                    data.put("lname", getLastname().getText().toString());
                    data.put("pass", getPassword().getText().toString());
                    return data;
                }
            };
            queue.add(request);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        menu.getItem(Menu.FIRST-1).setVisible(false);
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
}