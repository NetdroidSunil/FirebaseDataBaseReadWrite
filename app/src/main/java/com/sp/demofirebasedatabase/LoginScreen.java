package com.sp.demofirebasedatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sp.Pojo.UsersData;
import com.sp.demofirebasedatabase.Utils.Config;
import com.sp.demofirebasedatabase.Utils.Constant;
import com.sp.demofirebasedatabase.Utils.TinyDB;
import com.sp.sunilnetd.demofirebasedatabase.R;

//http://www.devexchanges.info/2016/08/android-getting-started-with-firebase.html
public class LoginScreen extends AppCompatActivity {


    private TextInputLayout emailField;
    private TextInputLayout passwordField;
    private View btnLogin;
    private ProgressDialog progressDialog;
    FirebaseDatabase fire_database;
    TextView tv_click_to_reg;
    TinyDB sp_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp_db=new TinyDB(this);

        emailField = (TextInputLayout) findViewById(R.id.email_field);
        passwordField = (TextInputLayout) findViewById(R.id.password_field);
        tv_click_to_reg = (TextView) findViewById(R.id.tv_click_to_reg);
        btnLogin = findViewById(R.id.login);

        ///Get Firebase auth instance
        fire_database = FirebaseDatabase.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Config.hasText(emailField)) {
                    Config.showToast(LoginScreen.this, "Please enter your email");
                } else if (!Config.hasText(passwordField)) {
                    Config.showToast(LoginScreen.this, "Please enter your password");
                } else {
                    //requesting Firebase server
                    showProcessDialog();
                    authenticateUser(Config.getText(emailField), Config.getText(passwordField));
                }
            }
        });

        tv_click_to_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this, RegistrationScreen.class));

            }
        });
    }

    private void authenticateUser(String email, final String password) {

        DatabaseReference dbRef = fire_database.getReference(Constant.Firebase_data_user_Folder);

        Query query = dbRef.orderByChild("Email").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean pass_match = false;
                    for (DataSnapshot user : dataSnapshot.getChildren()) {

                        for (DataSnapshot ud : user.getChildren()) {

                            if (ud.getKey().equals("Password") && ud.getValue().equals(password)) {

                                sp_db.putString(Constant.k_u_id,user.getKey());   //here user key is user id...
                                Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                                startActivity(intent);
                                finish();
                                pass_match = true;
                            }
                        }
                    }
                    if (!pass_match) {
                        Toast.makeText(LoginScreen.this, "Password is wrong", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(LoginScreen.this, "User not found", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginScreen.this, "" + databaseError.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



        /*auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // When login failed
                        if (!task.isSuccessful()) {
                            Config.showToast(LoginScreen.this, "Login error!");
                            progressDialog.dismiss();
                        } else {
                            Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                });*/
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Logging in Firebase server...");
        progressDialog.show();
    }
}
