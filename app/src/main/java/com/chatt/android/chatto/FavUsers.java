package com.chatt.android.chatto;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;


public class FavUsers extends AppCompatActivity {

    ListView favUsersList;
    TextView noUsersText;
    ArrayList<String> fav_users = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    String key;
    private static final String TAG = "Login";

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        favUsersList = findViewById(R.id.usersList);
        noUsersText = findViewById(R.id.noUsersText);



        Firebase ref = new Firebase("https://chatto-f30aa.firebaseio.com/users");

        ref.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                fav_users.clear();
                String statue;
                for(com.firebase.client.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                    statue = String.valueOf(childDataSnapshot.child("statue").getValue());
                    if(statue.equals("1") && !(childDataSnapshot.getKey().equals(UserDetails.username))){
                        String online_users=childDataSnapshot.getKey();
                        if (fav_users.contains(childDataSnapshot.getKey())) {

                        }
                        else{
                            fav_users.add(online_users);

                        }
                    }
                }
                for(int i=0; i<fav_users.size();i++){
                    Log.v(TAG,"Online Kullanıcı..: "+ fav_users.get(i));
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(FavUsers.this,android.R.layout.simple_list_item_1, fav_users);
                favUsersList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
*/
}
