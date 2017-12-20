package com.example.android.chatto;

import android.support.annotation.Keep;


public class UserDetails {
    static String username = "";
    static String password = "";
    static String chatWith = "";
    static String isOnline = "";
    private static String Os_userid = "";

    public static String getOs_userid() {
        return Os_userid;
    }

    public static void setOs_userid(String os_userid) {
        Os_userid = os_userid;
    }
}
