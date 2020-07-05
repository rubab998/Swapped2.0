package com.example.swapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //Views
    EditText email,pasw;
    Button spb;
    TextView haveAc;
    ProgressDialog progressDialog;
    //Declare an instance of FirebaseAuth

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //action bar title and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init

        email=findViewById(R.id.emailinp);
        pasw=findViewById(R.id.passinp);
        spb=findViewById(R.id.sp);
        haveAc=findViewById(R.id.havacc);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registering User.....");

        //in the onCreate() method, initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        //signup button listener
        spb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input email, password
                String mail= email.getText().toString().trim();
                String pas= pasw.getText().toString().trim();
                //validate
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    //set error and focus to email edit text
                    email.setError("invalid  email");
                    email.setFocusable(true);

                }
                else if(pas.length()<6)
                {
                    //set error and focus to email edit text
                    pasw.setError("password lenght atleast 6 characters");
                    pasw.setFocusable(true);
                }
                else
                {
                    registerUser(mail,pas);//register the user
                }


            }
        });
        //handle login text view click listener
        haveAc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }



    private void registerUser(String email, String password)
    {
        //email and password pattern is valid , show progress dialog and start registering user
progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
              .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          // Sign in success, dismiss dialog and start signup activity
                          progressDialog.dismiss();

                          FirebaseUser user = mAuth.getCurrentUser();
                          //get user email and uid from auth
                          String email=user.getEmail();
                          String uid =user.getUid();
                          //when user is registered store user info in firebase realtime databse too
                          //using hash maps
                          HashMap<Object,String>hashMap=new HashMap<>();
                          //put info in hashmap
                          hashMap.put("email",email);
                          hashMap.put("uid",uid);
                          hashMap.put("name","");//will add later(e.g edit profilr)
                          hashMap.put("image","");//will add later(e.g edit profilr)
                          //fire base database instance
                          FirebaseDatabase database=FirebaseDatabase.getInstance();
                          //path to store users data named users
                          DatabaseReference reference = database.getReference("Users");
                          //put data within hashmaps in the database
                          reference.child(uid).setValue(hashMap);



                          Toast.makeText(RegisterActivity.this,"Registerd...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                         finish();
                         //update library at gradle.build
                          //default firebase is not initialized in this process
                      }
                      else {
                          // If sign in fails, display a message to the user.
                          progressDialog.dismiss();

                          Toast.makeText(RegisterActivity.this,"Authentication failed",
                          Toast.LENGTH_SHORT).show();

                      }
                      }

              }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error dismiss progress dialog and get and show the error message
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




    }
    @Override
    public boolean onSupportNavigateUp()
    {
       onBackPressed();//go to previous activity
        return super.onSupportNavigateUp();
    }
}
