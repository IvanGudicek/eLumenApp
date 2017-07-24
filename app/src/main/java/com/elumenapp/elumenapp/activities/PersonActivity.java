package com.elumenapp.elumenapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.models.Person;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class PersonActivity extends AppCompatActivity {

    private TextView name, username, email;
    private ImageView imageOfPerson, cameraPerson, galleryPerson;
    private Switch personSwitch;
    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    private Button discardButton;
    private String image_string = null;

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

    public void changeThePersonButtonListener(View view) {/*
        MainActivity.getConnectionInfo(PersonActivity.this);
        if (MainActivity.connecting && !MainActivity.server_error) {
            MainActivity.sharedPreferences = personSwitch.isChecked();
            String person_server = "http://ivangudicek.comli.com/person.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, person_server, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    imageOfPerson.buildDrawingCache();
                    Bitmap bitmap = imageOfPerson.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] image = stream.toByteArray();
                    image_string = Base64.encodeToString(image, 0);
                    globalStaticPerson.setDrawable(imageOfPerson.getDrawable());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters
                        params.put("username", globalStaticPerson.getUsername());
                        params.put("password", globalStaticPerson.getPassword());
                        params.put("image", image_string);
                    return params;
                }
            };
            MySingleton.getInstance(PersonActivity.this).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(PersonActivity.this, "NO internet connection or server error!!", Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(PersonActivity.this, MainActivity.class));
        finish();
        */
    }

    public void discardChangesButtonListener(View view) {
        startActivity(new Intent(PersonActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PersonActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        name = (TextView) findViewById(R.id.personName);
        username = (TextView) findViewById(R.id.personUsername);
        email = (TextView) findViewById(R.id.personEmail);
        discardButton = (Button) findViewById(R.id.discardPersonButton);
        name.setText("name: " + globalStaticPerson.getName() + ", last name: " + globalStaticPerson.getLastname());
        username.setText(globalStaticPerson.getUsername());
        email.setText(globalStaticPerson.getEmail());
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        imageOfPerson = (ImageView) findViewById(R.id.imageViewPerson);
        cameraPerson = (ImageView) findViewById(R.id.chooseCameraPerson);
        galleryPerson = (ImageView) findViewById(R.id.chooseGalleryPerson);
        imageOfPerson.setImageDrawable(globalStaticPerson.getDrawable());
        personSwitch = (Switch) findViewById(R.id.personSwitch);
        if (MainActivity.sharedPreferences) {
            personSwitch.setChecked(true);
        } else {
            personSwitch.setChecked(false);
        }
        cameraPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        galleryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                Bitmap bitmap;
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imageOfPerson.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();

                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imageOfPerson.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}