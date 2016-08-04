package com.elumenapp.elumenapp.database.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.MainActivity;
import com.elumenapp.elumenapp.R;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final static String register_url = "http://ivangudicek.comli.com/register.php";
    private ImageView ivCamera, ivGallery, ivImage;
    private AlertDialog.Builder alertBuilder;
    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    private EditText editUsername, editName, editLastname, editPassword, editCheck_password, editDescription, editEmail;
    private String name, username, lastname, password, check_password, description, email, image_string = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                Bitmap bitmap;
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                    ivImage.setImageBitmap(bitmap);
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
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editUsername = (EditText) findViewById(R.id.username);
        editName = (EditText) findViewById(R.id.name);
        editLastname = (EditText) findViewById(R.id.lastname);
        editEmail = (EditText) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        editCheck_password = (EditText) findViewById(R.id.check_password);
        editDescription = (EditText) findViewById(R.id.description);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivCamera.setOnClickListener(new View.OnClickListener() {
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
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });
    }


    public void registerButtonListener(View view) {
        alertBuilder = new AlertDialog.Builder(this);
        name = editName.getText().toString();
        lastname = editLastname.getText().toString();
        username = editUsername.getText().toString();
        password = editPassword.getText().toString();
        check_password = editCheck_password.getText().toString();
        email = editEmail.getText().toString();
        description = editDescription.getText().toString();

        ivImage.buildDrawingCache();
        Bitmap bitmap = ivImage.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        image_string = Base64.encodeToString(image, 0);

        if (!MainActivity.connecting) {
            Toast.makeText(this, "No internet connection!!!", Toast.LENGTH_LONG).show();
        } else {
            if (name.equals("") || name.equals("") || username.equals("") || lastname.equals("") || email.equals("") || password.equals("") || check_password.equals("")) {
                alertBuilder.setTitle("Something went wrong...");
                alertBuilder.setMessage("Please fill all the fields...");
                displayAlert("input error");
            } else if (!password.equals(check_password)) {
                alertBuilder.setTitle("Something went wrong...");
                alertBuilder.setMessage("Passwors do not match...");
                displayAlert("password");
            } else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            alertBuilder.setTitle("Server response...");
                            alertBuilder.setMessage(message);
                            displayAlert(code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "SOmething went wrong on the server...", Toast.LENGTH_LONG).show();
                        Toast.makeText(RegisterActivity.this, "through few seconds will be enabled question for all users :)", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        MainActivity.server_error = true;
                        finish();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        //Adding parameters
                        params.put("name", name);
                        params.put("lastname", lastname);
                        params.put("username", username);
                        params.put("password", password);
                        params.put("description", description);
                        params.put("email", email);
                        params.put("image", image_string);
                        params.put("name_of_image", email + "_" + username);
                        return params;
                    }
                };
                MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(stringRequest);
            }
        }
    }


    public void displayAlert(final String code) {
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (code) {
                    case "error": {
                        dialog.cancel();
                    }
                    break;
                    case "password": {
                        editPassword.setText("");
                        editCheck_password.setText("");
                    }
                    break;
                    case "reg_success": {
                        finish();
                    }
                    break;
                    case "reg_failed": {
                        editUsername.setText("");
                        editEmail.setText("");
                        editPassword.setText("");
                        editCheck_password.setText("");
                    }
                    break;
                }
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
