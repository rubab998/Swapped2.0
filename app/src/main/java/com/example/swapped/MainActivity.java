package com.example.swapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
//views
    Button sb,lb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init views
        sb=findViewById(R.id.sup);
        lb=findViewById(R.id.logb);

        // handle register button click
        sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start register activity
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));

            }

        });
        //handle login button click
        lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start Login activity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

            }
        });

    }
}
/*In this part we will do the followings
01-Add internet permission (required for firebase)
02-Add Register and Login Buttons in MainActivity
03-Create RegisterActivity
04-Create Firebase Project and connect app with that project
05-Check google-services.json file to make sure app is connected with firebase
06-User Registration using Email & Password
07-Create DashboardActivity
08-Go to Profile Activity After Register/Login


part2
09 make profile activity launcher
10 add logout feature



Testing
run
enter invalid email e.g without @, .com,etc
enter empty password or less than 6 characters
enter valid email /password
enter existing (registered )email( it should not be accepted
check registered user in firebase


 Part(02):
01-Make ProfileAcivity Launcher//putting the intent filter inside profile activity in manifest
02-On app start Check if user signed in stay in ProfileAcivity otherwise go to MainActivity
03-Create Login Activity
04-Login User with Email/Password
05-After LoggingIn go to ProfileAcivity
06-Add options menu for adding Logout Option
07-After LoggingOut go to MainActivity

 Part(03):
01- Add Recover Password option in LoginActivity
02- Clicking Forgot password will show a dialog containing
       TextView(as label)
       EditText(input email)
       Button(will send recover password instructions) to your email
03- Little Improvements

Part(04):
Add Google Sign-In feature
 Requirements:
 Enable Google sign-in from Firebase Authentication
 Add Project support email
 Add SHA-1 Certificate
 Add Google Sign-In library

part5 01- Save registered user's info(name, email, uid, phone, image)
      in firebase realtime database.
      Requirements:
 Add Firebase Realtime database
Change firebase realtime database rules
  02- Add Bottom Navigation in Profile Activity having three menus
  Home
Profile (User info like name, email, uid, phone, image)
All Users

 //add menu in xml to show in bottom navigation

 part 6 design user profile

 Design user profile
 Get user info from firebase
 Show user info

 Part(10):
Design Chat Activity [Create new Empty Activity]
Toolbar will contain receiver icon, name and status like online/offline.
Add new Fragment For Chats List
Add this fragment to BottomNavigation

 */





















