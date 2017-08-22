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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.data.MySingleton;
import com.elumenapp.elumenapp.models.Score;
import com.elumenapp.elumenapp.models.Subject;
import com.elumenapp.elumenapp.models.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private View globalView;
    private CallbackManager callbackManager = null;
    public static final String SERVER_CONNECTION_URL = "http://192.168.56.1:8080";


    private static List<User> userList = new ArrayList<>();
    private static List<Score> scoreList = new ArrayList<>();

    public static List<User> getUserList() {
        return userList;
    }

    public static void setUserList(List<User> userList) {
        MainActivity.userList = userList;
    }

    public static List<Score> getScoreList() {
        return scoreList;
    }

    public static void setScoreList(List<Score> scoreList) {
        MainActivity.scoreList = scoreList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

//    public class ThreadServerPerson implements Runnable {
//
//        @Override
//        public void run() {
//
//            JsonArrayRequest userJsonRequest = new JsonArrayRequest(SERVER_CONNECTION_URL + "/user/list",
//                    new Response.Listener<JSONArray>() {
//                        @Override
//                        public void onResponse(JSONArray response) {
//                            if (response.length() > 0) {
//                                for (int i = 0; i < response.length(); i++) {
//                                    try {
//                                        JSONObject object = response.getJSONObject(i);
//                                        userList.add(new User(object.getInt("id"), object.getString("facebookId"), object.getString("firstName")
//                                                , object.getString("lastName"), object.getString("fullName")));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            });
//            MySingleton.getInstance(MainActivity.this).addToRequestQueue(userJsonRequest);
//        }
//    }

    private String jsonResponse = null;
    private static User user = new User();

    public static User getUser() {
        return user;
    }

    public static void setUser(User tmpUser) {
        user = tmpUser;
    }

    private void updateFacebookUserToDatabase(User facebookUser) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("facebookId", facebookUser.getFacebookId());
            jsonBody.put("firstName", facebookUser.getFirstName());
            jsonBody.put("lastName", facebookUser.getLastName());
            jsonBody.put("fullName", facebookUser.getFullName());
            jsonResponse = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                    SERVER_CONNECTION_URL + "/user/login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(MainActivity.this, "Korisnik nije uspješno spremljen u bazu!! Ponovno pokrenite server te probajte ponovno!", Toast.LENGTH_LONG).show();
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

    public void getUserFromDatabase(String facebookId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(SERVER_CONNECTION_URL + "/user/get/facebook/" + facebookId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = new User(response.getInt("id"), response.getString("facebookId"), response.getString("firstName"), response.getString("lastName"), response.getString("fullName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Pokrenite server te probajte ponovo!", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.getString("firstName");
        String surname = inBundle.getString("lastName");
        String id = inBundle.getString("facebookId");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        updateFacebookUserToDatabase(new User(id, name, surname, inBundle.getString("fullName")));
        getUserFromDatabase(id);

//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        ThreadServerPerson threadServerPerson = new ThreadServerPerson();
//        executorService.execute(threadServerPerson);
//        executorService.shutdown();

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
        String imgUrl = "https://graph.facebook.com/" + inBundle.getString("facebookId") + "/picture?type=large";
        Picasso.with(this)
                .load(imgUrl)
                .into(personImageView);
    }

    public void startQuizButtonListener(View view) {
        startActivity(new Intent(MainActivity.this, StartQuizActivity.class));
    }

    public void profileButtonListener(View view) {
        Intent profileIntent = new Intent(MainActivity.this, UserActivity.class);
        profileIntent.putExtra("fullName", user.getFullName());
        profileIntent.putExtra("facebookId", user.getFacebookId());
        startActivity(profileIntent);
    }


    public void listOfPersonsButtonListener(View view) {
        JsonArrayRequest userJsonRequest = new JsonArrayRequest(SERVER_CONNECTION_URL + "/user/list",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    userList.add(new User(object.getInt("id"), object.getString("facebookId"), object.getString("firstName")
                                            , object.getString("lastName"), object.getString("fullName")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        getScoresForUsers();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Dohvat liste korisnika nije uspio, pokrenite server te probajte ponovno!", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(userJsonRequest);
    }

    public void getScoresForUsers() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MainActivity.SERVER_CONNECTION_URL + "/score/list",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject object = response.getJSONObject(i);
                                    scoreList.add(new Score(object.getInt("id"), new BigDecimal(object.getString("score")), object.getInt("roundNumber"),
                                            new User(object.getJSONObject("user").getInt("id"), object.getJSONObject("user").getString("facebookId"), object.getJSONObject("user").getString("firstName"), object.getJSONObject("user").getString("lastName"), object.getJSONObject("user").getString("fullName")),
                                            new Subject(object.getJSONObject("subject").getInt("id"), object.getJSONObject("subject").getString("name"))));
                                }
                            }
                            startActivity(new Intent(MainActivity.this, UserListActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Pokrenite server te probajte ponovo!", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);
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
        getMenuInflater().inflate(R.menu.menu_login, menu);

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
            case R.id.action_logout:
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(login);
                finish();
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
                LoginManager.getInstance().logOut();
                Intent login = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(login);
                finish();
                break;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(MainActivity.this, UserActivity.class);
                profileIntent.putExtra("fullName", user.getFullName());
                profileIntent.putExtra("facebookId", user.getFacebookId());
                startActivity(profileIntent);
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
