package com.example.swapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class LoginActivity extends AppCompatActivity {
//private static final int RC_SIGN_IN=100;
//GoogleSignInClient  mGoogleSignInClient;
    //views
    EditText emaill,paswl;
    TextView nothaveAc,recpass;
    Button lgb;
    //SignInButton googlelog;
    //before mAuth
    // Configure Google Sign In
    //GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          //  .requestIdToken(getString(R.string.default_web_client_id))
           // .requestEmail()
           // .build();

   // public GoogleSignInClient getmGoogleSignInClient() {
     //   mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
     //   return mGoogleSignInClient;
  // }

    //mGoogleSignInClient=GoogleSignIn.getClient(this,gso);


    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;
    //progress dialog
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //action bar title and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

//In the onCreate() method, initialize the FirebaseAuth instance.
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //init

        emaill=findViewById(R.id.emailinp);
        paswl=findViewById(R.id.passinp);
        lgb=findViewById(R.id.lg);
        nothaveAc=findViewById(R.id.nothavacc);
        recpass=findViewById(R.id.recoverpass);
        //googlelog=findViewById(R.id.goob);
        //login button click
        lgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                String email = emaill.getText().toString();
                String pas = paswl.getText().toString().trim();
                 if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                     //invalid email pattern set error

                        emaill.setError("innvalid email");
                        emaill.setFocusable(true);

                 }
                 else
                 {
                     //valid email pattern
                     loginUser(email,pas);
                 }
            }
        });
        //not have an account text view click
        nothaveAc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        //recover pass text view click
        recpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showRecoverPasswordDialog();
            }
        });
        //handle google button click
        //googlelog.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {
                //begin google login process
               // Intent signInIntent = getmGoogleSignInClient().getSignInIntent();
               // startActivityForResult(signInIntent, RC_SIGN_IN);

           // }
       // });

        //init
        pd= new ProgressDialog(this);



    }
    private void showRecoverPasswordDialog()
    {
        //Alert dialog

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover password");
        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set dialog
        final EditText emailet= new EditText(this);
        emailet.setHint("email");
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        //sets the min width of a editview to fit a text of n 'M' letters regardless of the actual text size
        //extention and size
        emailet.setMinEms(16);

        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);
        //buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input emai
                String email=emailet.getText().toString().trim();
                beginReciivery(email);



            }
        });
        //buttons cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             //dismiss dialog
                dialog.dismiss();

            }
        });
        //show dialog
        builder.create().show();

    }
    private void beginReciivery(String mail){
        //show progress dialog
        pd.setMessage("sending email....");
        pd.show();

        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public  void onComplete(@NonNull Task<Void> task){
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Email sent",Toast.LENGTH_SHORT).show();
                }
                else

                {
                    Toast.makeText(LoginActivity.this,"failed..",Toast.LENGTH_SHORT).show();
                }

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              pd.dismiss();
        //get and show proper error message
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void loginUser(String email, String pas)
    {
        //show progress dialog
        pd.setMessage("logging in....");
        pd.show();
        mAuth.signInWithEmailAndPassword(email, pas)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //dismiss progress dialog
                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user=mAuth.getCurrentUser();
                            //if user is signing in first time then get and show user info from google account
                           if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                //get user email and uid from auth
                                String email=user.getEmail();
                                String uid =user.getUid();
                                //when user is registered store user info in firebase realtime databse too
                                //using hash maps
                                HashMap<Object,String> hashMap=new HashMap<>();
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

                            }
                            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                             finish();
                        } else {
                            //dismiss progress dialog
                            pd.dismiss();
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //dismiss progress dialog
                pd.dismiss();
                //error get and sgow error message
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();//go to previous activity
        return super.onSupportNavigateUp();
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriatly
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            //show user email in toast
                            Toast.makeText(LoginActivity.this,""+user.getEmail(),Toast.LENGTH_SHORT).show();
                            //go to profile activity after logged in
                            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Login failed...",Toast.LENGTH_SHORT).show();

                            //updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //get and show error
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}
