package com.elumenapp.elumenapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.data.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private EditText loginUser, loginPassword;
    private String user, password;
    private static String staticMessage = null;
    private CheckBox checkBox;
    private Button facebookButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginUser = (EditText) findViewById(R.id.loginUser);
        checkBox = (CheckBox) findViewById(R.id.rememberCheckBox);

        facebookButton = (Button) findViewById(R.id.facebookButtonListener);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, FacebookMainActivity.class));
                finish();
            }
        });


    }


    public void saveUser(String username, String image, Double total_score, String password, String description, String name, String lastname, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("lastname", lastname);
        editor.putString("email", email);
        editor.putString("description", description);
        editor.putFloat("total_score", total_score.floatValue());
        editor.putString("image", image);
        editor.commit();
        MainActivity.sharedPreferences = true;
    }

    public void clearUser() {
        MainActivity.sharedPreferences = false;
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "");
        editor.putString("username", "");
        editor.putString("password", "");
        editor.putString("lastname", "");
        editor.putString("email", "");
        editor.putString("description", "");
        editor.putFloat("total_score", 0);
        editor.putString("image", "");
        editor.commit();
    }


    public void logInButtonListener(View view) {
        MainActivity.getConnectionInfo(LogInActivity.this);
        user = loginUser.getText().toString();
        password = loginPassword.getText().toString();
        if (!MainActivity.connecting) {
            Toast.makeText(this, "No internet connection!!!", Toast.LENGTH_LONG).show();
        } else if (user.equals("") || password.equals("")) {
            displayAlert("null");
        } else {
            String login_url = "http://ivangudicek.comli.com/login.php";
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONArray jsonArray;
                    try {
                        jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code = jsonObject.getString("code");
                        staticMessage = jsonObject.getString("message");
                        if (code.equals("login_success")) {
                            String string = jsonObject.getString("image");
                            byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
                            PersonActivity.setGlobalStaticPerson(jsonObject.getString("username"), drawable, new BigDecimal(jsonObject.getDouble("total_score")), jsonObject.getString("password"),
                                    jsonObject.getString("description"), jsonObject.getString("name"), jsonObject.getString("lastname"),
                                    jsonObject.getString("email"));
                            if (checkBox.isChecked()) {
                                saveUser(jsonObject.getString("username"), string, jsonObject.getDouble("total_score"), jsonObject.getString("password"),
                                        jsonObject.getString("description"), jsonObject.getString("name"), jsonObject.getString("lastname"),
                                        jsonObject.getString("email"));
                            } else {
                                clearUser();
                            }
                            MainActivity.server_error = false;
                            MainActivity.logging = true;
                            MainActivity.logouting = false;
                            startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            finish();
                            Toast.makeText(LogInActivity.this, "You are successfully logging :)", Toast.LENGTH_LONG).show();
                        } else {
                            displayAlert(code);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LogInActivity.this, "SOmething went wrong on the server...", Toast.LENGTH_LONG).show();
                    Toast.makeText(LogInActivity.this, "through few seconds will be enabled question for all users :)", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    MainActivity.server_error = true;
                    finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user", user);
                    params.put("password", password);
                    return params;
                }
            };
            MySingleton.getInstance(LogInActivity.this).addToRequestQueue(stringRequest);
        }
    }


    public void displayAlert(String string) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        switch (string) {
            case "null": {
                alertBuilder.setTitle("warning");
                alertBuilder.setMessage("You must fill the fields");
                MainActivity.logging = false;
                alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
            break;
            case "login_failed": {
                alertBuilder.setTitle("warning");
                alertBuilder.setMessage(staticMessage);
                MainActivity.logging = false;
                alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(getIntent());
                        finish();
                    }
                });
            }
            break;
        }
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void goToRegisterActivityButtonListener(View view) {
        startActivity(new Intent(LogInActivity.this, com.elumenapp.elumenapp.activities.RegisterActivity.class));
        finish();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(LogInActivity.this, MainActivity.class));
        finish();
    }
}
