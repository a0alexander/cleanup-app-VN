package com.example.cleanup_vn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Creater_Dashboard extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1 ;
    Button signout,gotomap, viewpublic;
    FirebaseAuth mAuth ;
    FirebaseFirestore db;
    FirebaseUser mUser;
    CardView mviewpublicCard, mViewmyCard;
    ImageView userimage_admin;
    StorageReference mStorageRef;
    TextView user_admin_name;

    ImageButton  addsitimagebtn;


    @Override
    protected void onStart() {
        super.onStart();
//        setupRecyclerMySites();
//        setupPublicRecycler();



    }

    @Override
    protected  void onResume(){
        super.onResume();
        setupRecyclerMySites();
        setupPublicRecycler();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creater__dashboard);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        signout = findViewById(R.id.signout1);
        gotomap = findViewById(R.id.createSite);
        viewpublic = findViewById(R.id.viewPublicSites);
        mviewpublicCard = findViewById(R.id.cardtohide2_creator);
        mViewmyCard = findViewById(R.id.cardtohide1_creator);
        userimage_admin = findViewById(R.id.userImage_admin);
        user_admin_name = findViewById(R.id.users_name_info_creator);




        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getUserinfo();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
//                Toast.makeText(Creater_Dashboard.this, "signed out!", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent intent = new Intent(Creater_Dashboard.this, Login.class);
                startActivity(intent);
                finish();


            }
        });



        gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creater_Dashboard.this,Map1.class);
                startActivity(intent);

            }
        });




        viewpublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creater_Dashboard.this,Map2.class);
                startActivity(intent);
            }
        });



        userimage_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });




        FirebaseMessaging.getInstance().subscribeToTopic("siteUpdates")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            Toast.makeText(Creater_Dashboard.this, "Subscribed!", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

















    }

































    public void getUserinfo(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                            if(documentSnapshot.exists()){
                            users u  = documentSnapshot.toObject(users.class);
                            user_admin_name.setText(u.firstName +" "+ u.lastName);

                            if(u.profilePic!=null) {
                                Picasso.get().load(u.profilePic).resize(80, 80).into(userimage_admin);


                            }





                        }
                    }
                });
    }





    public void setupRecyclerMySites(){

        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {





                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        ArrayList<Events> eventsSites = new ArrayList<>();
                        RecyclerView mRecycler = findViewById(R.id.eventRecycler);
                        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(Creater_Dashboard.this,LinearLayoutManager.HORIZONTAL,true);
                        RecyclerView.Adapter madapter;

                        for(QueryDocumentSnapshot d: queryDocumentSnapshots){

                                    eventsSites.add(d.toObject(Events.class));



                                }

                         madapter = new adapter_events_recycle(eventsSites,R.layout.addsite);


                        mRecycler.setLayoutManager(mlayoutmanager);
                        mRecycler.setAdapter(madapter);
                        mlayoutmanager.smoothScrollToPosition(mRecycler,new RecyclerView.State(),madapter.getItemCount());
                        mRecycler.setHasFixedSize(true);



                        if(madapter.getItemCount()==1){

//                            Toast.makeText(Creater_Dashboard.this, "zero", Toast.LENGTH_SHORT).show();

                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(Creater_Dashboard.this,RecyclerView.HORIZONTAL,false);
                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);
                            mViewmyCard.setVisibility(View.VISIBLE);


                        }

                        if(madapter.getItemCount()==2){
                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(Creater_Dashboard.this,RecyclerView.HORIZONTAL,true);
                            LinearLayout linearLayout1 = findViewById(R.id.mysiterecycler_linear_layout);


                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);
                            mViewmyCard.setVisibility(View.GONE);

                        }




                    }
                });



    }

    public void setupPublicRecycler(){
        db.collection("Sites")
                .addSnapshotListener(this,new EventListener<QuerySnapshot>() {




                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        ArrayList<Events> eventsSites = new ArrayList<>();
                        RecyclerView mRecycler = findViewById(R.id.viewsitesRecycler_creator);
                        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(Creater_Dashboard.this,LinearLayoutManager.HORIZONTAL,true);
                        RecyclerView.Adapter madapter;

                        for(QueryDocumentSnapshot d: queryDocumentSnapshots){

                            eventsSites.add(d.toObject(Events.class));



                        }

                        madapter = new adapter_events_recycle(eventsSites,R.layout.existing_sites);


                        mRecycler.setLayoutManager(mlayoutmanager);
                        mRecycler.setAdapter(madapter);
                        mlayoutmanager.smoothScrollToPosition(mRecycler,new RecyclerView.State(),madapter.getItemCount());
                        mRecycler.setHasFixedSize(true);



                        if(madapter.getItemCount()==1){

//                            Toast.makeText(Creater_Dashboard.this, "zero", Toast.LENGTH_SHORT).show();

                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(Creater_Dashboard.this,RecyclerView.HORIZONTAL,false);
                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);

                            mviewpublicCard.setVisibility(View.VISIBLE);
                        }

                        if(madapter.getItemCount()==2){
                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(Creater_Dashboard.this,RecyclerView.HORIZONTAL,true);
                            mRecycler.setLayoutManager(mlayoutmanager1);

                            mviewpublicCard.setVisibility(View.GONE);
                        }






                    }
                });


    }




public void chooseFile(){


    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(intent,PICK_IMAGE_REQUEST);




}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null & data.getData()!=null){

            Uri mimageuri = data.getData().normalizeScheme();

            Picasso.get().load(mimageuri).into(userimage_admin);
            sendtoFirebase(mimageuri);
            // Toast.makeText(this, mimageuri.toString(), Toast.LENGTH_SHORT).show();



        }


    }

    public void sendtoFirebase(Uri uri){

        Uri file = uri;

        StorageReference profilePicRef = mStorageRef.child(mAuth.getCurrentUser().getUid()+".jpg");


        profilePicRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .update("profilePic",url.toString());

                    }
                });







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Creater_Dashboard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }




}
