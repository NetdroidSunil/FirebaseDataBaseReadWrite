package com.sp.demofirebasedatabase;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sp.demofirebasedatabase.Utils.Config;
import com.sp.demofirebasedatabase.Utils.Constant;
import com.sp.sunilnetd.demofirebasedatabase.R;

import java.util.HashMap;

//http://www.devexchanges.info/2016/08/android-getting-started-with-firebase.html
public class RegistrationScreen extends AppCompatActivity {

    private View btnLogin;
    private View btnSignUp;
    private ProgressDialog progressDialog;
    private TextInputLayout name_field;
    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout phone_field;
    FirebaseDatabase fire_database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        //Get Firebase auth instance
        fire_database = FirebaseDatabase.getInstance();


        btnLogin = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.sign_up);
        name_field = (TextInputLayout) findViewById(R.id.name_field);
        email = (TextInputLayout) findViewById(R.id.email_field);
        password = (TextInputLayout) findViewById(R.id.password_field);
        phone_field = (TextInputLayout) findViewById(R.id.phone_field);


        //go to Login Activity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //sign up a new account
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Config.hasText(email)) {
                    Config.showToast(RegistrationScreen.this, "Please input your email");
                } else if (!Config.hasText(password)) {
                    Config.showToast(RegistrationScreen.this, "Please input your password");
                } else {
                    //requesting Firebase server
                    showProcessDialog();
                    DatabaseReference dbRef = fire_database.getReference(Constant.Firebase_data_user_Folder);

                    String key = dbRef.push().getKey();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Name", Config.getText(name_field).toString());
                    hashMap.put("Email", Config.getText(email).toString());
                    hashMap.put("Password", Config.getText(password).toString());
                    hashMap.put("Contact", Config.getText(phone_field).toString());

                    dbRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            finish();
                            Toast.makeText(RegistrationScreen.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Insert Error :", " : " + e.toString());
                            Toast.makeText(RegistrationScreen.this, "something wants  wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Register a new account...");
        progressDialog.show();
    }
}
