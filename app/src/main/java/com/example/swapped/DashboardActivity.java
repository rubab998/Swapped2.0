package com.example.swapped;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    //fire base authentication checking if user is loggeed in
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
//views
   // TextView prof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //init

        firebaseAuth= FirebaseAuth.getInstance();
        //prof=findViewById(R.id.profilet);
        //bottom navigation view
        BottomNavigationView navigationView=findViewById(R.id.bott_n);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        //defaul home fragment on start
        actionBar.setTitle("Home");
        HomeFragment fragment =new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,fragment,"");
        fragmentTransaction.commit();

    }

private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //handle item clicks
        switch(item.getItemId()){
            case R.id.nav_home:
                actionBar.setTitle("Home");
                HomeFragment fragment =new HomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.content,fragment,"");
               fragmentTransaction.commit();

                return true;
            case R.id.nav_profilr:
                actionBar.setTitle("Profile");
                ProfileFragment profileFragment  = new ProfileFragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.content,profileFragment,"");
                fragmentTransaction1.commit();
                //profile fragment
                return true;

            case R.id.nav_users:
                actionBar.setTitle("Users");
                UserFragment userFragment = new UserFragment();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.content,userFragment,"");
                fragmentTransaction2.commit();
                return true;
                //user fragment
            case R.id.nav_chat:
                actionBar.setTitle("Chats");
                ChatListFragment chatListFragment= new ChatListFragment();
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.content,chatListFragment,"");
                fragmentTransaction3.commit();
                return true;
            //user fragment
        }

        return false;
    }
};

    private void checkuserstatus()
    {
//get current user
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user != null)
        {
            //user is signed in so stay here
                //set email of logged in usrr
            //prof.setText(user.getEmail());
        }
        else
        {
            //user not signeg in go to mainactivity
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
    protected void  onStart()
    {
        //check on start of app
        checkuserstatus();;
        super.onStart();
    }
    //inflate options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //inflating menu
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);

    }

    //handling menu item clicks
    public boolean onOptionsItemSelected(MenuItem item){
        //get item id
        int id=item.getItemId();
        if(id==R.id.actlogout){
            firebaseAuth.signOut();
            checkuserstatus();

        }
        return super.onOptionsItemSelected(item);
    }
}
