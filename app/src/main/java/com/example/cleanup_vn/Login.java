package com.example.cleanup_vn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Login extends AppCompatActivity {

    Button signinwithemail, signinMain;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    EditText username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){

            LoadUI(mAuth.getCurrentUser().getUid());


        }




        signinwithemail = findViewById(R.id.signUpwithEmail);
        signinMain = findViewById(R.id.signInMain);
        username = findViewById(R.id.usernamefield);
        password = findViewById(R.id.passwordfield);




        signinMain.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        String username1 = username.getText().toString();
        String pass1 = password.getText().toString();

        if(!username1.isEmpty() && !pass1.isEmpty()){


            mAuth.signInWithEmailAndPassword(username1,pass1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    LoadUI(authResult.getUser().getUid());


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();

                }
            });

        }




    }
            });








        signinwithemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, intermediate.class);
                startActivity(intent);



            }
        });



    }

    public void LoadUI(String uid1) {

        //        final userType userType1 = new userType();




        db.collection("Users").document(uid1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String userType = task.getResult().getString("typeOfUser");

                if (userType.equals("admin")) {
                    Intent intent1 = new Intent(Login.this, Creater_Dashboard.class);
                    startActivity(intent1);
                    Toast.makeText(Login.this, "admin user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("admin");
                    finish();
                } else if (userType.equals("normal")) {
                    Intent intent = new Intent(Login.this, normal_Activity.class);
                    startActivity(intent);
//                    Toast.makeText(Login.this, "normal user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("normal");
                    finish();

                }

                else if (userType.equals("super")){
                    Intent intent = new Intent(Login.this, Super.class);
                    startActivity(intent);
//                    Toast.makeText(Login.this, "super user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("normal");
                    finish();

                }


            }

        });

    }




//    public void LoadUI_2(String uid1) {
//
//        //        final userType userType1 = new userType();
//
//        db.collection("Users").document(uid1).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//
//                if(documentSnapshot.exists()){
//
//                    String userType = documentSnapshot.getString("typeOfUser");
//
//                    if (userType.equals("admin")) {
//                        Intent intent1 = new Intent(Login.this, Creater_Dashboard.class);
//                        startActivity(intent1);
//                        Toast.makeText(Login.this, "admin user logged in", Toast.LENGTH_SHORT).show();
////                    userType1.setUserType("admin");
//                        finish();
//                    } else if (userType.equals("normal")) {
//                        Intent intent = new Intent(Login.this, normal_Activity.class);
//                        startActivity(intent);
//                        Toast.makeText(Login.this, "normal user logged in", Toast.LENGTH_SHORT).show();
////                    userType1.setUserType("normal");
//                        finish();
//
//                    }
//
//                    else if (userType.equals("super")){
//                        Intent intent = new Intent(Login.this, Super.class);
//                        startActivity(intent);
//                        Toast.makeText(Login.this, "super user logged in", Toast.LENGTH_SHORT).show();
////                    userType1.setUserType("normal");
//                        finish();
//
//                    }
//
//
//
//                }
//
//
//
//
//
//
//
//
//
//            }
//        });
//
//    }
}
