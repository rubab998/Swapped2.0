package com.example.swapped;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
//firebase auth
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //views from xml


    ImageView profilepic;
    TextView name;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,container, false);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //init views
        profilepic = view.findViewById(R.id.pro_pic);
        name = view.findViewById(R.id.pro_name);

        /* we have to get info of currently signed in user. we can get it using users email or uid
        i will be retrieving data using email
         */
        /* by using order by child query we will show detail from a node whose key named email has value equal to
        currently signed in email.
        it will search all nodes, where the the key matches it will get  its detail
         */

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 //check until required data get
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String nam = ""+ds.child("name").getValue();
                    String imag = ""+ds.child("profilepic").getValue();//atrributes given in the firebase database

                    // set data
                    name.setText(nam);
                    try{
                        //if image received then set
                        Picasso.get().load(imag).into(profilepic);
                    }
                    catch (Exception e)
                    {
                        //if any exception occurs while getting image then set default
                        Picasso.get().load(R.drawable.ic_prof_pic).into(profilepic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
