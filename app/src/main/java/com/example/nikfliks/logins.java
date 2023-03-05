package com.example.nikfliks;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class logins extends AppCompatActivity {

    TextView textViewRegisterNow;
    TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    Button buttonSubmit;
    String name, email, password, apiKey;
    TextView textViewError;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logins);

        textViewRegisterNow = findViewById(R.id.registerNow);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonSubmit = findViewById(R.id.submit);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                email = String.valueOf(textInputEditTextEmail.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://192.168.175.133/nicflixregistration/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(logins.this, response, Toast.LENGTH_SHORT).show();
                                textViewError.setText(response);
                                textViewError.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                try {
                                    JSONObject jsonobject = new JSONObject(response);
                                    String status = jsonobject.getString("status");
                                    String message = jsonobject.getString("message");
                                    if (status.equals("success")){
                                        name = jsonobject.getString("name");
                                        email = jsonobject.getString("email");

                                        apiKey = jsonobject.getString("apiKey");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged", "true");
                                        editor.putString("name", name);
                                        editor.putString("email", email);
                                        editor.putString("apiKey", apiKey);
                                        editor.putString("password", password);
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        textViewError.setText(message);
                                        textViewError.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(logins.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    textViewError.setText(e.getLocalizedMessage());
                                    textViewError.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        textViewError.setText(error.getLocalizedMessage());
                        textViewError.setVisibility(View.VISIBLE);
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        textViewRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), registration.class);
                startActivity(intent);
                finish();
            }
        });
    }
}


