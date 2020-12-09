package com.example.cleanup_vn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    Spinner genderSpin;
    users user = new users();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser mUser;
    View.OnClickListener mOnClickListener;
    Button signup,signin;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    EditText firstname, lastname, birthday, email, password, phone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        mUser = mAuth.getCurrentUser();


        if(mUser!=null){


           checkUserType(mUser.getUid());



        }

        setupSpinner();


        firstname = findViewById(R.id.firstName);
        birthday = findViewById(R.id.Birthday);
        lastname = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        signup = findViewById(R.id.signUpButton);
        signin  =findViewById(R.id.signInButton);




        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getUserData()){
                    
                    createUser();



                }



            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email1 = email.getText().toString();
                String pass1 = password.getText().toString();

                if(!email1.isEmpty() && !pass1.isEmpty()){

                    mAuth.signInWithEmailAndPassword(email1,pass1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            checkUserType(authResult.getUser().getUid());
                        }
                    });


                }




            }
        });



    }








    /*SPINNER SETUP*/
    public void setupSpinner(){
        genderSpin = findViewById(R.id.genderSpinner);

        final String[] arraySpin = new String[]{
                "Gender", "Male", "Female", "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                arraySpin );

        genderSpin.setAdapter(adapter);

        genderSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                user.gender = arraySpin[position];
                Toast.makeText(MainActivity.this, arraySpin[position]+"", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /*SHOW DATE PICKER*/
    public void showDatePicker(){


        DatePickerDialog datePickerDialog = new DatePickerDialog(this
                , this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get((Calendar.MONTH)),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();


    }


    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        EditText dateinput = findViewById(R.id.Birthday);


        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);


        dateinput.setText(dateFormat.format(c.getTime()));
        getDateandTime getDate = new getDateandTime();
        getDate.year  = year;
        getDate.month = month;
        getDate.day = dayOfMonth;

    }


    /*return date in String Form*/
    public class getDateandTime{

        int year;
        int month;
        int day;
        int hour;
        int mins;

        public getDateandTime(){}

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }

        public int getHour() {
            return hour;
        }

        public int getMins() {
            return mins;
        }
    }



    /*get and see if userdata is valid for processing*/
    public boolean getUserData(){


        try {
            user.firstName = firstname.getText().toString();
            user.lastName = lastname.getText().toString();
            user.birthday = dateFormat.parse(birthday.getText().toString());
            user.email = email.getText().toString();
            user.phone = phone.getText().toString();
            String extra = getIntent().getStringExtra("typeOfUser");
            user.typeOfUser = extra;
            return  true;

        }
        catch (Exception e){

            Toast.makeText(MainActivity.this, "Check all data!", Toast.LENGTH_SHORT).show();
            return false;
        }






    }



    /*CREATE USER AND GET uID*/
   
    public void createUser(){
        String password1 = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(user.email,password1)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {

            @Override
            public void onSuccess(AuthResult authResult) {
                String uid = authResult.getUser().getUid();
                db.collection("Users").document(uid).set(user);
                Toast.makeText(MainActivity.this, "User Created Successfully!" , Toast.LENGTH_SHORT).show();
                LoadUI(uid);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
       

    }


    public void checkUserType(String uid1){


//        final userType userType1 = new userType();

        db.collection("Users").document(uid1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               String userType = task.getResult().getString("typeOfUser");

                if(userType.equals("admin")){
                    Intent intent1 = new Intent(MainActivity.this,Creater_Dashboard.class);
                    startActivity(intent1);
                    Toast.makeText(MainActivity.this, "admin user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("admin");
                    finish();
                }
                else if(userType.equals("normal")){
                    Intent intent = new Intent(MainActivity.this,normal_Activity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "normal user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("normal");
                    finish();

                }


            }
        });

//        return userType1.getUsertype();

    }

    public void LoadUI(String uid1) {

        //        final userType userType1 = new userType();




        db.collection("Users").document(uid1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String userType = task.getResult().getString("typeOfUser");

                if (userType.equals("admin")) {
                    Intent intent1 = new Intent(MainActivity.this, Creater_Dashboard.class);
                    startActivity(intent1);
                    Toast.makeText(MainActivity.this, "admin user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("admin");
                    finish();
                } else if (userType.equals("normal")) {
                    Intent intent = new Intent(MainActivity.this, normal_Activity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "normal user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("normal");
                    finish();

                }

                else if (userType.equals("super")){
                    Intent intent = new Intent(MainActivity.this, Super.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "super user logged in", Toast.LENGTH_SHORT).show();
//                    userType1.setUserType("normal");
                    finish();

                }


            }

        });

    }


}
