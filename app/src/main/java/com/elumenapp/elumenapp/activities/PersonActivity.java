package com.elumenapp.elumenapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.models.Person;
import com.elumenapp.elumenapp.models.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;

public class PersonActivity extends AppCompatActivity {

    private TextView fullName;
    private ImageView imageOfPerson;

    private static Person globalStaticPerson = new Person(null, null, null, null, null, null, null, null);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static void setGlobalStaticPerson(String username, Drawable drawable, BigDecimal bigDecimal, String password, String description, String name, String lastname, String email) {
        globalStaticPerson = new Person(username, drawable, bigDecimal, password, description, name, lastname, email);
    }

    public static Person getGlobalStaticPerson() {
        return globalStaticPerson;
    }


    @Override
    public void onBackPressed() {
//        Intent profileIntent = new Intent(PersonActivity.this, MainActivity.class);
//        profileIntent.putExtra("fullName", user.getFullName());
//        profileIntent.putExtra("facebookId", user.getFacebookId());
//        profileIntent.putExtra("firstName", user.getFirstName());
//        profileIntent.putExtra("lastName", user.getLastName());
//        startActivity(profileIntent);
        finish();
    }

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle inBundle = getIntent().getExtras();
        String facebookName = inBundle.getString("fullName");
        String facebookId = inBundle.getString("facebookId");
        user = new User(facebookId, inBundle.getString("firstName"), inBundle.getString("lastName"), facebookName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        fullName = (TextView) findViewById(R.id.personFullName);
        fullName.setText(facebookName);
        imageOfPerson = (ImageView) findViewById(R.id.personImage);
//        Bitmap bitmap = null;
//        try {
//            bitmap = getFacebookProfilePicture(facebookId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //   Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        //       imageOfPerson.setImageBitmap(bitmap);

//        try {
//            //  String fbId="970463683015249";
//            URL fb_url = new URL("https://graph.facebook.com/" + facebookId + "/picture?type=large");//small | noraml | large
//            HttpsURLConnection conn1 = (HttpsURLConnection) fb_url.openConnection();
//            HttpsURLConnection.setFollowRedirects(true);
//            conn1.setInstanceFollowRedirects(true);
//            Bitmap fb_img = BitmapFactory.decodeStream(conn1.getInputStream());
//            imageOfPerson.setImageBitmap(fb_img);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


//        Picasso.with(PersonActivity.this)
//                .load("https://graph.facebook.com/" + facebookId + "/picture?type=large")
//                .into(imageOfPerson);


//        try {
//            URL image_value = new URL("http://graph.facebook.com/"+ facebookId+ "/picture?type=normal");
//            Bitmap bmp = null;
//            try {
//                bmp = BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            imageOfPerson.setImageBitmap(bmp);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }


//        String imageUrl = "https://graph.facebook.com/" + facebookId + "/picture?type=small";
//
//        try {
//            Bitmap bitmap = getFacebookProfilePicture(facebookId);
//            imageOfPerson.setImageBitmap(bitmap);
//            System.out.print("let me see the problem");
//            System.out.print(bitmap);
//            System.out.print("let me see the problem");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String imgUrl = "https://graph.facebook.com/" + facebookId + "/picture?type=large";
        Picasso.with(this).load(imgUrl).into(imageOfPerson);

//        Picasso.with(this)
//                .load(imageUrl)
//                .into(imageOfPerson);

    }


    public static Bitmap getFacebookProfilePicture(String userID) throws SocketException, SocketTimeoutException, MalformedURLException, IOException, Exception {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=small");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }


}