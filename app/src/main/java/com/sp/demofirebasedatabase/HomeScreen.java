package com.sp.demofirebasedatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sp.demofirebasedatabase.Utils.Constant;
import com.sp.demofirebasedatabase.Utils.TinyDB;
import com.sp.sunilnetd.demofirebasedatabase.R;

//http://www.devexchanges.info/2016/08/android-getting-started-with-firebase.html
public class HomeScreen extends AppCompatActivity {

    FirebaseDatabase fire_database;
    TextView tv_response;
    TinyDB sp_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        sp_db = new TinyDB(this);
        tv_response = (TextView) findViewById(R.id.tv_response);

        ///Get Firebase auth instance
        fire_database = FirebaseDatabase.getInstance();


        DatabaseReference myRef = fire_database.getReference(Constant.Firebase_data_user_Folder);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String response = dataSnapshot.child(sp_db.getString(Constant.k_u_id)).getValue().toString();

                tv_response.setText(response);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });

    }
}
