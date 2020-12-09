package com.example.cleanup_vn;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;

public class Clustering implements ClusterItem {


    public LatLng mPos;
    public String mTitle;
    public String mSnippet;
    public String mId;
    String owner;




    public Clustering(){

    }


    public Clustering(double lat, double lng, String title,String snippet, String id){

        this.mPos = new LatLng(lat, lng);
        this.mTitle = title;
        this.mSnippet = snippet;
        this.mId = id;

    }






    @Override
    public LatLng getPosition() {
        return mPos;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public LatLng getmPos() {
        return mPos;
    }

    public void setmPos(LatLng mPos) {
        this.mPos = mPos;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSnippet() {
        return mSnippet;
    }

    public void setmSnippet(String mSnippet) {
        this.mSnippet = mSnippet;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


}
