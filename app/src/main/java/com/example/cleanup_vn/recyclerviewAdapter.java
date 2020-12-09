package com.example.cleanup_vn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recyclerviewAdapter extends RecyclerView.Adapter<recyclerviewAdapter.ViewHolder1> {
    public ArrayList<UserItem_recycler> arraylist;
    public ArrayList<Events> arraylist1;
    public ArrayList<users> siteParticipants;



    public static class ViewHolder1 extends  RecyclerView.ViewHolder {


        public TextView mtextview1;
        public ImageView participant_image;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

           mtextview1 = itemView.findViewById(R.id.nameOfUserinsites);
           participant_image = itemView.findViewById(R.id.participant_pro_picture);


        }
    }

//    public recyclerviewAdapter(ArrayList<UserItem_recycler> list){
//            arraylist = list;
//
//    }

    public recyclerviewAdapter(ArrayList<users> list1){
        this.siteParticipants = list1;

    }




    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_user_row, parent,false);
   ViewHolder1 fe = new ViewHolder1(v);
   return  fe;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder1 holder, int position) {

        users u =   siteParticipants.get(position);

        holder.mtextview1.setText(u.firstName +" "+ u.lastName);
        Picasso.get().load(u.profilePic).into(holder.participant_image);


    }

    @Override
    public int getItemCount() {
        return siteParticipants.size();
    }
}


