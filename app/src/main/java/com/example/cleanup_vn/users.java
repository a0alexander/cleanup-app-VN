package com.example.cleanup_vn;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class users {

    String firstName;
    String lastName;
    Date birthday;

    String email;

    String phone;

    String gender;
    String typeOfUser;
    String profilePic;



    public users(String firstName, String lastName, Date birthday, String email, String phone, String gender, String typeOfUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;

        this.phone = phone;
        this.gender = gender;
        this.typeOfUser = typeOfUser;
    }

    public users(){

    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }



    public String getPhone() {
        return phone;
    }

    public String isGender() {
        return gender;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }
}

   class Events{

    String siteName;
    Date eventDate;
    String owner;
    //LatLng loc;
    String ownerId;
    GeoPoint loc;
    String placeId;
       String mainrootSiteID;
       int sitePop;
       int garbageWeight;



    public Events(){

    }

//        public Events(String siteName, Date eventDate, Timestamp time, String Owner, LatLng location,String ownerid) {
//            this.siteName = siteName;
//            this.eventDate = eventDate;
//            this.owner = Owner;
//            this.loc = new GeoPoint(location.latitude, location.longitude);
//            this.ownerId = ownerid;
//        }

       public Events(String siteName, Date eventDate, Timestamp time, String Owner, GeoPoint location,String ownerid) {
           this.siteName = siteName;
           this.eventDate = eventDate;
           this.owner = Owner;
           this.loc = location;
           this.ownerId = ownerid;
       }

       public int getGarbageWeight() {
           return garbageWeight;
       }

       public void setGarbageWeight(int garbageWeight) {
           this.garbageWeight = garbageWeight;
       }

       public int getSitePop() {
           return sitePop;
       }

       public void setSitePop(int sitePop) {
           this.sitePop = sitePop;
       }

       public String getMainrootSiteID() {
           return mainrootSiteID;
       }

       public void setMainrootSiteID(String mainrootSiteID) {
           this.mainrootSiteID = mainrootSiteID;
       }

       public String getPlaceId() {
           return placeId;
       }

       public void setPlaceId(String placeId) {
           this.placeId = placeId;
       }

       public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getSiteName() {
            return siteName;
        }

        public Date getEventDate() {
            return eventDate;
        }

        public String getOwner(){
                return owner;
        }

        public GeoPoint getLoc() {
            return loc;
        }

        public  String getDateNormal(Date date1){
            SimpleDateFormat dt = new SimpleDateFormat("dd/MMM/yyyy");
            return dt.format(date1);


       }

       public  String gettimeNormal(Date date1){
           SimpleDateFormat dt = new SimpleDateFormat("hh : mm a");
           return dt.format(date1);


       }



        public void setLoc(GeoPoint loc) {
            this.loc = loc;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public void setEventDate(Date eventDate) {
            this.eventDate = eventDate;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }



    }
