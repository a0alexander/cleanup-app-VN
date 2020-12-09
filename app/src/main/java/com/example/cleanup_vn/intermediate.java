package com.example.cleanup_vn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class intermediate extends AppCompatActivity {

    Button create, join;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        create = findViewById(R.id.creatorButton);
        join = findViewById(R.id.joinerButton);




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           sendUsertype("admin");





            }
        });


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendUsertype("normal");


            }
        });











    }

    public void sendUsertype(String userType){

        Intent intent =new Intent(intermediate.this, MainActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("typeOfUser",userType);
        intent.putExtras(bundle1);
        startActivity(intent);

    }
}
