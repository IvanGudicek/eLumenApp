package com.elumenapp.elumenapp.database.com;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.person.com.Person;
import com.elumenapp.elumenapp.person.com.PersonActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private EditText loginUser, loginPassword;
    private String user, password;
    private AlertDialog.Builder alertBuilder;
    private final String login_url = "http://ivangudicek.comli.com/login.php";
    private static String staticMessage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        loginUser = (EditText)findViewById(R.id.loginUser);
    }


    public void logInButtonListener(View view){
        user = loginUser.getText().toString();
        password = loginPassword.getText().toString();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.isConnected() == false) {
            Toast.makeText(this, "No internet connection!!!", Toast.LENGTH_LONG).show();
        } else if(user.equals("") || password.equals("")){
            displayAlert("null");
        }else{
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray =jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String code = jsonObject.getString("code");
                        staticMessage = jsonObject.getString("message");
                        displayAlert(code);
                        String string = jsonObject.getString("image");
                        byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
                       // imageView.setImageDrawable(drawable);
                        Person.setStaticPerson(jsonObject.getString("username"), drawable, new BigDecimal(jsonObject.getDouble("total_score")),jsonObject.getString("password") ,
                                jsonObject.getString("description"), jsonObject.getString("name"), jsonObject.getString("lastname"),
                                jsonObject.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LogInActivity.this, "SOmethings went wrong...", Toast.LENGTH_LONG).show();
                }
            }){
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


    public void displayAlert(String string){
        alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("warning");
        if(string.equals("null")){
            alertBuilder.setMessage("You must fill the fields");
        }else if(string.equals("login_failed")){
            alertBuilder.setMessage(staticMessage);
        }else{
            startActivity(new Intent(LogInActivity.this, PersonActivity.class));
        }
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void goToRegisterActivityButtonListener(View view){
        startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
    }
}
