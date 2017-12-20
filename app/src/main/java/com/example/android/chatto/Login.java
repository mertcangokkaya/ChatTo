package com.example.android.chatto;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button loginButton;
    String user, pass;
    TextView register;
    private FirebaseAuth mFirebaseAuth;
    String OS_userId;


    //Geri tuşuna basıldığında programı kapatır
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mFirebaseAuth = FirebaseAuth.getInstance();


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        register = findViewById(R.id.register);

        final Context context = this;

        user = SaveSharedPreference.getUserName(context);
        pass = SaveSharedPreference.getPrefPassword(context);

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                OS_userId = userId ;
            }
        });

        //
        if (SaveSharedPreference.getUserName(Login.this).length() != 0) {

            int i= SaveSharedPreference.getUserName(Login.this).length();
            //username.setText(String.valueOf(i));

            if(!user.equals("") && !pass.equals("")) {
                String url = "https://chatto-f30aa.firebaseio.com/users.json";
                final ProgressDialog pd = new ProgressDialog(Login.this);
                pd.setMessage("Loading...");
                pd.show();

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://chatto-f30aa.firebaseio.com/users");

                        if (s.equals("null")) {
                            Toast.makeText(Login.this, "kullanıcı bulunamadı", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.has(user)) {
                                    Toast.makeText(Login.this, "kullanıcı bulunamadı", Toast.LENGTH_LONG).show();
                                } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                    UserDetails.username = user;
                                    UserDetails.password = pass;

                                    reference.child(user).child("statue").setValue("1");
                                    reference.child(user).child("os_userid").setValue(OS_userId);
                                    startActivity(new Intent(Login.this, User.class));

                                } else {
                                    Toast.makeText(Login.this, "yanlış şifre", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                        pd.dismiss();


                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                rQueue.add(request);


            }


        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Login.this, Register.class));
            }
        });

        //Login button
        


}
