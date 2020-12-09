package com.example.cleanup_vn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Super extends AppCompatActivity {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button analytics, signout;
    TextView totusers, totsites, totweightgarbage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super);

        analytics = findViewById(R.id.runanalytics);
        totsites = findViewById(R.id.totalcleanups);
        totusers = findViewById(R.id.totalusers);
        totweightgarbage = findViewById(R.id.totalgarbage);
        signout = findViewById(R.id.signoutadmin);


        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTotalSites();
                getTotalUsers();
                getTotalGarbage();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(Super.this, Login.class);
                startActivity(intent);
                finish();



            }
        });







    }




    public void getTotalUsers(){



        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<users> userList = new ArrayList<>();
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot d : task.getResult()){

                        userList.add(d.toObject(users.class));

                    }

                    if(userList.size()>0){
                        totusers.setText(String.valueOf(userList.size()-1));
                    }


                }



            }
        });

    }

    public void getTotalSites(){


        db.collection("Sites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            ArrayList<Events> siteList = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot d : task.getResult()){

                        siteList.add(d.toObject(Events.class));

                    }

                    try{
                        totsites.setText(String.valueOf(siteList.size()));
                    }
                    catch (Exception e){

                        Toast.makeText(Super.this, "Error Retrieving Data", Toast.LENGTH_SHORT).show();
                    }


                }



            }
        });

    }


    public void getTotalGarbage(){



        db.collection("Sites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int garbageTotal = 0;
                ArrayList<Events> siteList1 = new ArrayList<>();
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot d : task.getResult()){

                        siteList1.add(d.toObject(Events.class));

                    }

                    if (siteList1.size() > 0) {

                        for(Events e : siteList1){

                            garbageTotal += e.garbageWeight;

                        }

                        totweightgarbage.setText(String.valueOf(garbageTotal) + "kg");

                    }




                }



            }
        });




    }





}
