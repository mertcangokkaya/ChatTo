package com.chatt.android.chatto;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class AllUsers extends AppCompatActivity {
    ListView allUsersList;
    TextView noUsersText;
    ArrayList<String> al_user = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        allUsersList = findViewById(R.id.allUsersList);
        noUsersText = findViewById(R.id.noUsersText);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pd = new ProgressDialog(AllUsers.this);
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

        RequestQueue rQueue = Volley.newRequestQueue(AllUsers.this);
        rQueue.add(request);

        allUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al_user.get(position);
                startActivity(new Intent(AllUsers.this, com.chatt.android.chatto.Chat.class));
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
            allUsersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            allUsersList.setVisibility(View.VISIBLE);
            allUsersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al_user));
        }

        pd.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
