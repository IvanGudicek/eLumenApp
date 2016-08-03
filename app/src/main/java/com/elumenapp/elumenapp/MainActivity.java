package com.elumenapp.elumenapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.database.com.LogInActivity;
import com.elumenapp.elumenapp.database.com.MySingleton;
import com.elumenapp.elumenapp.person.com.RecyclerActivity;
import com.elumenapp.elumenapp.quiz.com.StartQuizActivity;

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


    public void getResponseFromPersons() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, persons_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response_persons = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CheckingConnection checkingConnection = new CheckingConnection();
        executorService.execute(checkingConnection);
        executorService.shutdown();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startQuizButtonListener(View view) {
        if (logging || server_error) {
            startActivity(new Intent(MainActivity.this, StartQuizActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "You are not log in!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void signUpButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, LogInActivity.class));
    }

    public void listOfPersonsButtonListener(View view) {
        if (connecting) {
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
}
