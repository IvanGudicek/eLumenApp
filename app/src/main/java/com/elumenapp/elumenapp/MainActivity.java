package com.elumenapp.elumenapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.database.com.LogInActivity;
import com.elumenapp.elumenapp.database.com.MySingleton;
import com.elumenapp.elumenapp.person.com.PersonActivity;
import com.elumenapp.elumenapp.person.com.RecyclerActivity;
import com.elumenapp.elumenapp.quiz.com.StartQuizActivity;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog.Builder builder;
    private Toolbar toolbar;
    private View globalView;
    public static boolean logging = false;
    public static boolean connecting = false;
    public static String response_persons = null;
    private static final String persons_url = "http://ivangudicek.comli.com/persons.php";
    public static boolean server_error = false;
    public static boolean sharedCondition = false;


    public void getResponseFromPersons() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, persons_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response_persons = response;
                server_error = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                server_error = true;
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }


    public class CheckingConnection implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo == null || !networkInfo.isConnected()) {
                        connecting = false;
                    } else {
                        getResponseFromPersons();
                        connecting = true;
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
        byte[] decodedString = Base64.decode(sp.getString("image", ""), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
        RecyclerActivity.setCurrentPerson(sp.getString("username", ""), drawable, new BigDecimal(sp.getFloat("total_score", 0)), sp.getString("password", ""), sp.getString("description", ""), sp.getString("name", ""), sp.getString("lastname", ""), sp.getString("email", ""));
        if (!sp.getString("username", "").equals("")) {
            sharedCondition = true;
        } else {
            sharedCondition = false;
        }
    }

    public void cleanSharedPreferences() {
        logging = false;
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
        RecyclerActivity.setCurrentPerson(null, null, null, null, null, null, null, null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (logging) {
            setContentView(R.layout.activity_main_login);
        } else {
            setContentView(R.layout.activity_main_logout);
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CheckingConnection checkingConnection = new CheckingConnection();
        executorService.execute(checkingConnection);
        executorService.shutdown();

        setSharedPreferences();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentMainRelativeLayout);
        View childRelativeLayout, childLinearLayout;
        if (sharedCondition && logging) {
            childRelativeLayout = inflater.inflate(R.layout.content_main_login, (ViewGroup) findViewById(R.id.contentLoginRelativeLayout));
            childLinearLayout = inflater.inflate(R.layout.nav_login, (ViewGroup) findViewById(R.id.navLoginLinearLayout));
        } else {
            childLinearLayout = inflater.inflate(R.layout.nav_logout, (ViewGroup) findViewById(R.id.navLogoutLinearLayout));
            childRelativeLayout = inflater.inflate(R.layout.content_main_logout, (ViewGroup) findViewById(R.id.contentLogoutRelativeLayout));
        }
        relativeLayout.removeAllViews();
        relativeLayout.addView(childRelativeLayout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        navigationView.addView(childLinearLayout);
        personFullName = (TextView) findViewById(R.id.personFullName);
        personUsername = (TextView) findViewById(R.id.personUsername);
        personImageView = (ImageView) findViewById(R.id.personImageView);
        if (logging) {
            personFullName.setText(RecyclerActivity.getCurrentPerson().getName() + " " + RecyclerActivity.getCurrentPerson().getLastname());
            personUsername.setText(RecyclerActivity.getCurrentPerson().getUsername());
            personImageView.setImageDrawable(RecyclerActivity.getCurrentPerson().getDrawable());
        } else {
            personFullName.setText("Unknown user!!");
            personUsername.setText("Please logIn :)");
            personImageView.setImageResource(R.drawable.unknown_person);
        }

    }

    private TextView personUsername, personFullName;
    private ImageView personImageView;


    public void startQuizButtonListener(View view) {
        if (logging || server_error || sharedCondition) {
            startActivity(new Intent(MainActivity.this, StartQuizActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "You are not log in!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void profileButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, PersonActivity.class));
    }

    public void signUpButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, LogInActivity.class));
    }

    public void listOfPersonsButtonListener(View view) {
        if (server_error) {
            Toast.makeText(MainActivity.this, "Something went wrong on the server...", Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this, "through few seconds will be enabled question for all users :)", Toast.LENGTH_LONG).show();
        } else if (connecting) {
            startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
        } else {
            Toast.makeText(this, "No internet connection!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void settingsButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    public void aboutButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

    public void exitButtonListener(View view) {
        globalView = view;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("are you sure");
        builder.setMessage("Do you wanna exit of app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (logging) {
            getMenuInflater().inflate(R.menu.menu_logout, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_login, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.action_login:
                finish();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                break;
            case R.id.action_logout:
                cleanSharedPreferences();
                finish();
                startActivity(getIntent());
                break;
            case R.id.action_exit:
                exitButtonListener(globalView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.nav_login:
                finish();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                break;
            case R.id.nav_logout:
                cleanSharedPreferences();
                finish();
                startActivity(getIntent());
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, PersonActivity.class));
                break;
            case R.id.nav_tools:
                startActivity(new Intent(MainActivity.this, Settings_Activity.class));
                break;
            case R.id.nav_exit:
                exitButtonListener(globalView);
                break;
            case R.id.nav_share:
                Toast.makeText(MainActivity.this, "Will be soon", Toast.LENGTH_LONG).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
