package com.elumenapp.elumenapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.data.MySingleton;
import com.elumenapp.elumenapp.models.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private View globalView;
    private CallbackManager callbackManager = null;
    private static List<User> listOfUsers = new ArrayList<>();

    private User user = new User();
    private String facebookid, facebookImagePath;


    public static List<User> getListOfUsers() {
        return listOfUsers;
    }

    public static void setListOfUsers(List<User> listOfUsers) {
        MainActivity.listOfUsers = listOfUsers;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class ThreadServerPerson implements Runnable {

        @Override
        public void run() {

            JsonArrayRequest userJsonRequest = new JsonArrayRequest("http://192.168.56.1:8080/user/list",
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject object = response.getJSONObject(i);
                                        listOfUsers.add(new User(object.getInt("id"), object.getString("facebookId"), object.getString("firstName")
                                                , object.getString("lastName"), object.getString("fullName")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            for (User user : listOfUsers) {
                                System.out.println(user.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            MySingleton.getInstance(MainActivity.this).addToRequestQueue(userJsonRequest);
        }
    }

    private String jsonResponse = null;

    private void updateFacebookUserToDatabase(User facebookUser) {
        JSONObject jsonBody = null;

        try {
            jsonBody = new JSONObject();
            jsonBody.put("facebookId", facebookUser.getFacebookId());
            jsonBody.put("firstName", facebookUser.getFirstName());
            jsonBody.put("lastName", facebookUser.getLastName());
            jsonBody.put("fullName", facebookUser.getFullName());
            jsonResponse = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                    "http://192.168.56.1:8080/user/login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return jsonResponse == null ? null : jsonResponse.getBytes("utf-8");
                    } catch (Exception error) {
                        error.printStackTrace();
                        return null;
                    }
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.getString("firstName");
        String surname = inBundle.getString("lastName");
        String id = inBundle.getString("facebookId");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        user = new User(id, name, surname, inBundle.getString("fullName"));
        updateFacebookUserToDatabase(user);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ThreadServerPerson threadServerPerson = new ThreadServerPerson();
        executorService.execute(threadServerPerson);
        executorService.shutdown();

        setContentView(R.layout.activity_main);


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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentMainRelativeLayout);
        View childRelativeLayout, childLinearLayout;
        childRelativeLayout = inflater.inflate(R.layout.content_main, (ViewGroup) findViewById(R.id.contentMainRelativeLayout));
        childLinearLayout = inflater.inflate(R.layout.nav, (ViewGroup) findViewById(R.id.left_sidebar_nav));


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
        personFullName.setText(name + " " + surname);
        personUsername.setText(id);
        System.out.println("////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////");
        System.out.println(id);

        System.out.println("////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////" +
                "////////////////////////////////////////////////////////////////////////////////////");
//        personImageView.setImageDrawable(PersonActivity.getGlobalStaticPerson().getDrawable());
        String imgUrl = "https://graph.facebook.com/" + inBundle.getString("facebookId") + "/picture?type=large";
        Picasso.with(this)
                .load(imgUrl)
                .into(personImageView);

    }

    public void startQuizButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, StartQuizActivity.class));
        finish();
    }

    public void profileButtonListener(View view) {
        Intent profileIntent = new Intent(MainActivity.this, PersonActivity.class);
        profileIntent.putExtra("fullName", user.getFullName());
        profileIntent.putExtra("facebookId", user.getFacebookId());
        profileIntent.putExtra("firstName", user.getFirstName());
        profileIntent.putExtra("lastName", user.getLastName());
        startActivity(profileIntent);
    }


    public void listOfPersonsButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
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
        getMenuInflater().inflate(R.menu.menu_logout, menu);

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
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(login);
                finish();
                break;
            case R.id.action_logout:
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
            case R.id.nav_logout:
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
