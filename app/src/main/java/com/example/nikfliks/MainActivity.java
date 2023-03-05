package com.example.nikfliks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView textViewName, textViewEmail, textViewFetchResult;
    TextInputEditText textInputEditTextPassword;
    Button buttonLogout, buttonFetchUser, buttonUpdate, buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TEXT VIEW DISPLAY
        textViewName = findViewById(R.id.name);
        textViewEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        textViewFetchResult = findViewById(R.id.fetchResult);
        //BUTTONS
        buttonLogout = findViewById(R.id.logout);
        buttonUpdate = findViewById(R.id.update);
        buttonDelete = findViewById(R.id.delete);
        buttonFetchUser = findViewById(R.id.fetchProfile);
        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), logins.class);
            startActivity(intent);
            finish();
        }
        textViewName.setText(sharedPreferences.getString("name", ""));
        textViewEmail.setText(sharedPreferences.getString("email", ""));
        textInputEditTextPassword.setText(sharedPreferences.getString("password", ""));


        //Log Out Button
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.100.25/nicflixregistration/logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equals("Success")){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("name", "");
                                    editor.putString("email", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), logins.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                }

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));

                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        //Fetch User Button
        buttonFetchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.175.133/nicflixregistration/profile.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String name = jsonObject.getString("name");
                                    String subscriptiontype = jsonObject.getString("subscriptiontype");
                                    String paymentmethod = jsonObject.getString("paymentmethod");
                                    String email = jsonObject.getString("email");


                                    String display = "Name: " + name +"\n";
                                    display+= "Subscription Type: " + subscriptiontype + "\n";
                                    display+= "Payment Method: " + paymentmethod + "\n";
                                    display+= "Email: " + email;


                                    textViewFetchResult.setText(display);
                                    textViewFetchResult.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));

                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.175.133/nicflixregistration/delete.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                textViewFetchResult.setText(response);
                                textViewFetchResult.setVisibility(View.VISIBLE);

                                if(response.equals("success")){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("name", "");
                                    editor.putString("email", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), logins.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                }

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));

                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.43.252/nicflixregistration/update.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("password", textInputEditTextPassword.getText().toString());
                                editor.apply();


                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                        paramV.put("password", textInputEditTextPassword.getText().toString());
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });





    }
}


