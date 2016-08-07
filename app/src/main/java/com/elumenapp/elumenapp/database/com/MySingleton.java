package com.elumenapp.elumenapp.database.com;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MySingleton {
    private static MySingleton mySingletonInstance;
    private RequestQueue requestQueue;
    private static Context context;


    private MySingleton(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }


    public static synchronized MySingleton getInstance(Context context) {
        if (mySingletonInstance == null) {
            mySingletonInstance = new MySingleton(context);
        }
        return mySingletonInstance;
    }


    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

}
