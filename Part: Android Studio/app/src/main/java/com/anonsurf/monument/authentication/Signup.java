package com.anonsurf.monument.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity implements View.OnClickListener{
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText username;
    private EditText password;
    private Button register;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstname = this.findViewById(R.id.firstname);
        lastname = this.findViewById(R.id.lastname);
        email = this.findViewById(R.id.email);
        username = this.findViewById(R.id.username);
        password = this.findViewById(R.id.password);
        register = this.findViewById(R.id.register);

        context = this;

        register.setOnClickListener(this);
    }

    public EditText getFirstname() {
        return firstname;
    }

    public EditText getLastname() {
        return lastname;
    }

    public EditText getEmail() {
        return email;
    }

    public EditText getUsername() {
        return username;
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
        if (this.getEmail().getText().toString().isEmpty()) {
            this.getEmail().setError("enter your email");
        }
        if (this.getUsername().getText().toString().isEmpty()) {
            this.getUsername().setError("enter your username");
        }
        if (this.getPassword().getText().toString().isEmpty()) {
            this.getPassword().setError("enter your password");
        }
        if (!this.getFirstname().getText().toString().isEmpty() && !this.getLastname().getText().toString().isEmpty() && !this.getEmail().getText().toString().isEmpty()
                && !this.getUsername().getText().toString().isEmpty() && !this.getPassword().getText().toString().isEmpty()) {

            String url = "http://unscanned-solvents.000webhostapp.com/registerUser.php";

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.contains("Duplicate entry 'user'")){
                        getUsername().setError("this username is already in use");
                    }
                    if(response.contains("Duplicate entry 'email'")){
                        getEmail().setError("this email is already in use");
                    }

                    if(response.equals("Insertion reussie")){
                        Toast.makeText(context, "your account has been created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, Signin.class);
                        intent.putExtra("username", getUsername().getText().toString());
                        setResult(21, intent);

                        new CountDownTimer(1500, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                finish();
                            }
                        }.start();
                    }
                    else {
                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("fname", getFirstname().getText().toString());
                    data.put("lname", getLastname().getText().toString());
                    data.put("email", getEmail().getText().toString());
                    data.put("user", getUsername().getText().toString());
                    data.put("pass", getPassword().getText().toString());
                    return data;
                }
            };
            queue.add(request);
        }
    }
}