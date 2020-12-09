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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class normal_Activity extends AppCompatActivity {

    Button signout, viewpublic;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser;

    ImageView userProfilepic;
    ImageButton addsitimagebtn;
    FirebaseFirestore db;
    CardView mCard1, mCard2;
    StorageReference mStorageRef;
    TextView users_name;
    public static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);


        setContentView(R.layout.activity_normal_);
        db = FirebaseFirestore.getInstance();
        signout = findViewById(R.id.signout1);
       viewpublic = findViewById(R.id.viewPublicSites);
        mCard1 = findViewById(R.id.cardtohide1);
        mCard2 = findViewById(R.id.cardtohide2);
        userProfilepic = findViewById(R.id.userImage);
        users_name = findViewById(R.id.users_name_info);


        mStorageRef = FirebaseStorage.getInstance().getReference();






        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
//                Toast.makeText(normal_Activity.this, "signed out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(normal_Activity.this, Login.class);
                startActivity(intent);
                finish();


            }
        });




        viewpublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(normal_Activity.this,Map2.class);
                Bundle bundle = new Bundle();

                CollectionReference d = db.collection("Sites");

                bundle.putString("firestoreRef",d.getPath());

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });






        userProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });







    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpPublicRecycler();
        setUpMysiteRecycler();
        getUserinfo();

    }

    public void setUpPublicRecycler(){



        db.collection("Sites").orderBy("eventDate", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {

                    ArrayList<Events> eventsSites = new ArrayList<>();
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        RecyclerView mRecycler = findViewById(R.id.eventRecyler_normal);
                        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(normal_Activity.this,LinearLayoutManager.HORIZONTAL,true);
                        RecyclerView.Adapter madapter;

                        mRecycler.setHasFixedSize(true);

                        if(e!=null){
                            return;
                        }

                        for(QueryDocumentSnapshot d : queryDocumentSnapshots){


                            eventsSites.add(d.toObject(Events.class));



                        }
                        madapter = new adapter_events_recycle(eventsSites,R.layout.existing_sites);
                        mRecycler.setLayoutManager(mlayoutmanager);
                        mRecycler.setAdapter(madapter);
                        mlayoutmanager.smoothScrollToPosition(mRecycler,new RecyclerView.State(),madapter.getItemCount());

                        
                        if(madapter.getItemCount()==1){
                            mCard2.setVisibility(View.VISIBLE);
//                            Toast.makeText(normal_Activity.this, "zero", Toast.LENGTH_SHORT).show();

                        RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(normal_Activity.this,RecyclerView.HORIZONTAL,false);
                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);


                        }
                        if(madapter.getItemCount()==2){
                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(normal_Activity.this,RecyclerView.HORIZONTAL,true);
                            mRecycler.setLayoutManager(mlayoutmanager1);


                        }

                        







                    }
                });
    }

    public void setUpMysiteRecycler(){



        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("myPlaces")
                .orderBy("eventDate", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {


                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        RecyclerView mRecycler = findViewById(R.id.myEventRecycler);
                        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(normal_Activity.this,LinearLayoutManager.HORIZONTAL,true);
                        RecyclerView.Adapter madapter;

                        ArrayList<Events> eventsSites = new ArrayList<>();
                        mRecycler.setHasFixedSize(true);

                        if(e!=null){
                            return;
                        }


                        for(QueryDocumentSnapshot d : queryDocumentSnapshots){


                            eventsSites.add(d.toObject(Events.class));

                        }

                        madapter = new adapter_events_recycle(eventsSites,R.layout.my_existing_sites);
                        mRecycler.setLayoutManager(mlayoutmanager);
                        mRecycler.setAdapter(madapter);
                        mlayoutmanager.smoothScrollToPosition(mRecycler,new RecyclerView.State(),madapter.getItemCount());



                        if(madapter.getItemCount()==1){
                            mCard1.setVisibility(View.VISIBLE);
                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(normal_Activity.this,RecyclerView.HORIZONTAL,false);

                            mRecycler.setLayoutManager(mlayoutmanager1);
                            mRecycler.setAdapter(madapter);

                        }

                        if(madapter.getItemCount()==2){
                            RecyclerView.LayoutManager mlayoutmanager1 = new LinearLayoutManager(normal_Activity.this,RecyclerView.HORIZONTAL,true);
                            mRecycler.setLayoutManager(mlayoutmanager1);


                        }











                    }
                });


    }


public void getUserinfo(){
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                                try{
                                    users u  = task.getResult().toObject(users.class);
                                    users_name.setText(u.firstName +" "+ u.lastName);

                                    Picasso.get().load(u.profilePic).resize(80,80).into(userProfilepic);
                                }
                                catch (Exception e){
//                                    Toast.makeText(normal_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }



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

            Picasso.get().load(mimageuri).into(userProfilepic);
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
//                Toast.makeText(normal_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }




}
