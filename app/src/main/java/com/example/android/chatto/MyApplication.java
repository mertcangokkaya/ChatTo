package com.example.android.chatto;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.onesignal.OneSignal;


public class MyApplication extends android.app.Application {

    
    @Override
    public void onCreate() {
        super.onCreate();

        //Previous versions of Firebase
        Firebase.setAndroidContext(this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);

        }

    
    }
