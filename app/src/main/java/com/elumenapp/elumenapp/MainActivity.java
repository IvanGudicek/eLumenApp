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
import com.elumenapp.elumenapp.person.com.Person;
import com.elumenapp.elumenapp.person.com.PersonActivity;
import com.elumenapp.elumenapp.person.com.RecyclerActivity;
import com.elumenapp.elumenapp.quiz.com.StartQuizActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private View globalView;
    public static boolean logging = false, logouting = false;
    public static boolean connecting = false;
    private static final String persons_url = "http://ivangudicek.comli.com/persons.php";
    public static boolean server_error = false, sharedPreferences = false;
    private static List<Person> listOfPersons = new ArrayList<>();


    public static List<Person> getListOfPersons() {
        return listOfPersons;
    }

    public static void setListOfPersons(List<Person> listOfPersons) {
        MainActivity.listOfPersons = listOfPersons;
    }

    public class ThreadServerPerson implements Runnable {

        @Override
        public void run() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, persons_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    server_error = false;
                    JSONArray jsonArray;
                    listOfPersons = new ArrayList<>();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("persons");
                        int count = 0;
                        while (count < jsonArray.length()) {
                            JSONObject object = jsonArray.getJSONObject(count++);
                            String string = object.getString("image");
                            byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
                            listOfPersons.add(new Person(object.getString("username"), drawable, new BigDecimal(object.getDouble("total_score")), object.getString("password"),
                                    object.getString("description"), object.getString("name"), object.getString("lastname"),
                                    object.getString("email")));
                            MainActivity.server_error = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        MainActivity.server_error = true;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        MainActivity.server_error = true;
                    }
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
    }


    public void setSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
        byte[] decodedString = Base64.decode(sp.getString("image", ""), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
        sharedPreferences = !sp.getString("username", "").equals("");
        if (sharedPreferences) {
            PersonActivity.setGlobalStaticPerson(sp.getString("username", ""), drawable, new BigDecimal(sp.getFloat("total_score", 0)), sp.getString("password", ""), sp.getString("description", ""), sp.getString("name", ""), sp.getString("lastname", ""), sp.getString("email", ""));
        }
    }

    public void cleanSharedPreferences() {
        sharedPreferences = false;
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
        PersonActivity.setGlobalStaticPerson(null, null, null, null, null, null, null, null);
    }


    public static void getConnectionInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        MainActivity.connecting = !(networkInfo == null || !networkInfo.isConnected());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ThreadServerPerson threadServerPerson = new ThreadServerPerson();
        executorService.execute(threadServerPerson);
        executorService.shutdown();
        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(getApplicationContext());
       // AppEventsLogger.activateApp(this);



        getConnectionInfo(MainActivity.this);
        setSharedPreferences();
        if (connecting && !server_error && (logging || sharedPreferences && !logouting)) {
            setContentView(R.layout.activity_main_login);
        } else {
            setContentView(R.layout.activity_main_logout);
        }
        //getResponseFromPersons();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentMainRelativeLayout);
        View childRelativeLayout, childLinearLayout;
        if (connecting && !server_error && (logging || sharedPreferences && !logouting)) {
            childRelativeLayout = inflater.inflate(R.layout.content_main_login, (ViewGroup) findViewById(R.id.contentLoginRelativeLayout));
            childLinearLayout = inflater.inflate(R.layout.nav_login, (ViewGroup) findViewById(R.id.navLoginLinearLayout));
        } else {
            childLinearLayout = inflater.inflate(R.layout.nav_logout, (ViewGroup) findViewById(R.id.navLogoutLinearLayout));
            childRelativeLayout = inflater.inflate(R.layout.content_main_logout, (ViewGroup) findViewById(R.id.contentLogoutRelativeLayout));
        }
        if (relativeLayout != null) {
            relativeLayout.removeAllViews();
        }
        if (relativeLayout != null) {
            relativeLayout.addView(childRelativeLayout);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }


        if (navigationView != null) {
            navigationView.addView(childLinearLayout);
        }
        TextView personFullName = (TextView) findViewById(R.id.personFullName);
        TextView personUsername = (TextView) findViewById(R.id.personUsername);
        ImageView personImageView = (ImageView) findViewById(R.id.personImageView);
        if (connecting && !server_error && (logging || sharedPreferences && !logouting)) {
            personFullName.setText(PersonActivity.getGlobalStaticPerson().getName() + " " + PersonActivity.getGlobalStaticPerson().getLastname());

            personUsername.setText(PersonActivity.getGlobalStaticPerson().getUsername());

            personImageView.setImageDrawable(PersonActivity.getGlobalStaticPerson().getDrawable());

        } else {
            personFullName.setText("Unknown user!!");

            personUsername.setText("Please logIn :)");

            personImageView.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);

        }
    }


    public void startQuizButtonListener(View view) {
        if (connecting && !server_error && (logging || sharedPreferences && !logouting)) {
            startActivity(new Intent(MainActivity.this, StartQuizActivity.class));
            finish();
        } else {
            Toast.makeText(MainActivity.this, "You are not log in!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void profileButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, PersonActivity.class));
    }

    public void signUpButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, LogInActivity.class));
        finish();
    }

    public void listOfPersonsButtonListener(View view) {
        getConnectionInfo(MainActivity.this);
        if (server_error) {
            Toast.makeText(MainActivity.this, "Something went wrong on the server...", Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this, "through few seconds will be enabled question for all users :)", Toast.LENGTH_LONG).show();
        } else {
            if (connecting) {
                startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
            } else {
                Toast.makeText(this, "No internet connection!!!", Toast.LENGTH_LONG).show();
            }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                //super.onBackPressed();
                exitButtonListener(drawer);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (connecting && !server_error && (logging || sharedPreferences && !logouting)) {
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
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();
                break;
            case R.id.action_logout:
                logging = false;
                logouting = true;
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
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                logging = false;
                logouting = true;
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
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


}
