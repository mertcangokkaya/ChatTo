package com.example.android.chatto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;


public class  Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;
    String OS_userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(this);

        registerButton = findViewById(R.id.registerButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        final String isOnline = "0";

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                OS_userId = userId ;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("boş bırakılamaz");
                }
                else if(pass.equals("")){
                    password.setError("boş bırakılamaz");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("sadece alfabe ya da numara");
                }
                else if(user.length()<5){
                    username.setError("en az 5 karakter uzunluğunda olmalı");
                }
                else if(pass.length()<5){
                    password.setError("en az 5 karakter uzunluğunda olmalı");
                }
                else{final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Yükleniyor...");
                    pd.show();

                    String url = "https://chatto-f30aa.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://chatto-f30aa.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(Register.this, "Kayıt başarılı", Toast.LENGTH_LONG).show();
                                reference.child(user).child("statue").setValue(isOnline);
                            }
                            else {
                                try {
                                   JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(Register.this, "Kayıt başarılı", Toast.LENGTH_LONG).show();
                                        reference.child(user).child("statue").setValue("0");
                                        reference.child(user).child("os_userid").setValue(OS_userId);
                                    } else {
                                        Toast.makeText(Register.this, "Lütfen başka bir kullanıcı adı giriniz", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }

            }
        });




    }
}