package com.example.android.chatto;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.firebase.client.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class User extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al_user = new ArrayList<>();
    ArrayList<String> al_statue = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    int sayac = 0;
    String key;
    Context context=this;
    private static final String TAG = "Login";
    String user_name;
    DatabaseReference rootRef,demoRef;

    //Geri tuşuna basıldığında programı kapatır
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.users_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        OneSignal.sendTag("user_name", UserDetails.username);

        rootRef = FirebaseDatabase.getInstance().getReference();

        demoRef = rootRef.child("users");


        usersList = findViewById(R.id.usersList);
        noUsersText = findViewById(R.id.noUsersText);

        pd = new ProgressDialog(User.this);
        pd.setMessage("Yükleniyor...");
        pd.show();

        String url = "https://chatto-f30aa.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                 listUsers(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(User.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al_user.get(position);
                startActivity(new Intent(User.this, Chat.class));
            }
        });



    }



    public void listUsers(String s){
        try {




            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();

            while(i.hasNext()){
                key = i.next().toString();

                        if(!key.equals(UserDetails.username)) {
                            al_user.add(key);

                        }

                        totalUsers++;

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al_user));
        }

        pd.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }



    private void signOut() {
        final ProgressDialog pd = new ProgressDialog(User.this);
        pd.setMessage("Yükleniyor...");
        pd.show();
        SaveSharedPreference.clearUserName(context);
        String url = "https://chatto-f30aa.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://chatto-f30aa.firebaseio.com/users");

                reference.child(UserDetails.username).child("statue").setValue("0");
                startActivity(new Intent(User.this, Login.class));

          }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                pd.dismiss();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(User.this);
        rQueue.add(request);






    }
}
