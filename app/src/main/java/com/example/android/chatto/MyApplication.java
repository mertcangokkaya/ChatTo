package com.example.android.chatto;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;


public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Previous versions of Firebase
        Firebase.setAndroidContext(this);

        }
    }
