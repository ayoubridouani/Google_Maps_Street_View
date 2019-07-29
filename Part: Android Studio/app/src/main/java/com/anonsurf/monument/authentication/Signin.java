package com.anonsurf.monument.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.anonsurf.monument.R;
import com.anonsurf.monument.monument.MonumentsHome;

import org.json.JSONException;
import org.json.JSONObject;

public class Signin extends AppCompatActivity implements View.OnClickListener{
    private EditText user;
    private EditText pass;
    private Button signin;
    private TextView signup;
    private TextView asguest;
    private Context context;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //to store session variable
        shared = getSharedPreferences("session", MODE_PRIVATE);
        editor = shared.edit();

        if(!shared.getString("user", "").isEmpty()){
            startActivity(new Intent(this, MonumentsHome.class));
            finish();
        }

        user = this.findViewById(R.id.user);
        pass = this.findViewById(R.id.pass);
        signin = this.findViewById(R.id.signin);
        signup = this.findViewById(R.id.signup);
        asguest = this.findViewById(R.id.asguest);

        context = this;

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
        asguest.setOnClickListener(this);
    }

    public EditText getUser() {
        return user;
    }

    public EditText getPass() {
        return pass;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signin){
            if(this.getUser().getText().toString().isEmpty()){
                this.getUser().setError("enter username");
            }
            else if(this.getPass().getText().toString().isEmpty()){
                this.getPass().setError("enter password");
            }
            else{
                String url = "http://unscanned-solvents.000webhostapp.com/checkUser.php?user=" + getUser().getText().toString() + "&pass=" + getPass().getText().toString();

                RequestQueue queue = Volley.newRequestQueue(this);
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.isEmpty()){
                            Toast.makeText(context, "invalid information ...", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Intent intent = new Intent(context, MonumentsHome.class);

                            try {
                                JSONObject data = new JSONObject(response);
                                editor.putString("fname", data.getString("fname"));
                                editor.putString("lname", data.getString("lname"));
                                editor.putString("user", data.getString("user"));
                                editor.putString("email", data.getString("email"));
                                editor.putString("langue", data.getString("langue"));
                            } catch (JSONException e) {
                                Log.e("this App", "Could not parse malformed JSON: " + response + "");
                            }

                            editor.putBoolean("asguest", false);
                            editor.apply();

                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getUser().setText("");
                        getPass().setText("");
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(request);
            }
        }

        else if(v.getId() == R.id.signup){
            Intent intent = new Intent(Signin.this,Signup.class);
            this.startActivityForResult(intent,20);
        }
        else if(v.getId() == R.id.asguest){
            Intent intent = new Intent(context, MonumentsHome.class);
            editor = shared.edit();
            editor.putBoolean("asguest", true);
            editor.apply();
            startActivity(intent);
            finish();
        }
        else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 20 && resultCode == 21 && data != null){
            getUser().setText(data.getStringExtra("username"));
        }
    }
}