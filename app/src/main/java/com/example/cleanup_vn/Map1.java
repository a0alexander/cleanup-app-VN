package com.example.cleanup_vn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Map1 extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private GoogleMap mMap;
    FloatingActionButton fabA,fabViewcurrentsites;
    LatLng userclickLatLng;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CardView mCardView;
    ImageButton exitCard,dateButton, timeButton;
    EditText dateText, timeText,siteText;
    Button addsiteBtn;

    ObjectAnimator cardViewanimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map1);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;


        LatLng sydney = new LatLng(10.7295612,106.6937702);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("RMIT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12f));
        fabA = findViewById(R.id.fab1);
        mCardView = findViewById(R.id.cardView1);
        exitCard = findViewById(R.id.closeCard);
        timeButton = findViewById(R.id.timeButton);
        dateButton = findViewById(R.id.dateButton);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        addsiteBtn = findViewById(R.id.addbutton);
        siteText = findViewById(R.id.eventName);
        fabViewcurrentsites = findViewById(R.id.fab);

        cardViewanimator = ObjectAnimator.ofFloat(mCardView,"translationY",-2000f,0f);
        cardViewanimator.setInterpolator(new DecelerateInterpolator());

//        final ArrayList<Marker> markerList= new ArrayList<Marker>();










        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            Marker m1;
            @Override
            public void onMapClick(LatLng latLng) {


                if(m1!=null){
                    m1.remove();
                }

               m1  = mMap.addMarker(new MarkerOptions().position(latLng));
//                markerList.add(m1);
//                if(markerList.size()>0){
//                    mMap.clear();
//                    markerList.clear();
//                    mMap.addMarker(new MarkerOptions().position(latLng));
//                }
                userclickLatLng = latLng;
               // Toast.makeText(Map1.this, latLng.latitude+" "+latLng.longitude, Toast.LENGTH_SHORT).show();
                fabA.animate().rotationBy(360f).setInterpolator(new AccelerateInterpolator()).setDuration(500);

            }
        });


        fabA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Map1.this, "Add New Site", Toast.LENGTH_SHORT).show();
                Button delbutton = findViewById(R.id.deletebutton);
                delbutton.setVisibility(View.INVISIBLE);



                TableRow garbage_weight_row = findViewById(R.id.garbage_weight_row);
                garbage_weight_row.setVisibility(View.GONE);

                clearFields();
                addsiteBtn.setText("Create Site!");
                addsiteBtn.setOnClickListener(null);
                addsiteBtn.setOnClickListener(addsiteButtonListener);


                db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                               String a =  task.getResult().getString("firstName");
                                Toast.makeText(Map1.this, a, Toast.LENGTH_SHORT).show();

                            }
                        });


                if(userclickLatLng!=null){


                cardViewanimator.start();
                mCardView.setVisibility(View.VISIBLE);


                }


            }
        }
        );

        fabViewcurrentsites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Map1.this, "Showing Your Clean Up Sites", Toast.LENGTH_SHORT).show();
                setupCluster();

                db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            populatePoints(task);
                        }

                    }
                });



            }
        });









        exitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              cardViewanimator.reverse();


            }
        });


        View.OnClickListener dateButtonListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        };


        View.OnClickListener timeButtonListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        };


        dateButton.setOnClickListener(dateButtonListen);
        dateText.setOnClickListener(dateButtonListen);

        timeButton.setOnClickListener(timeButtonListen);
        timeText.setOnClickListener(timeButtonListen);







}

    View.OnClickListener addsiteButtonListener = new View.OnClickListener() {

        SimpleDateFormat timeStamp = new SimpleDateFormat("dd/MMM/yyyy hh : mm a");
        StringBuilder sb= new StringBuilder();
        Events event = new Events();


        users user;
        String uid = mAuth.getCurrentUser().getUid();
        @Override
        public void onClick(View v) {



            cardViewanimator.reverse();

            if(!timeText.getText().toString().isEmpty() && !dateText.getText().toString().isEmpty()
                    && !siteText.getText().toString().isEmpty()){

                sb.append(dateText.getText().toString()+" ");
                sb.append(timeText.getText().toString());

                try {
                    Date d = timeStamp.parse(sb.toString());
                    Toast.makeText(Map1.this, d.toString(), Toast.LENGTH_SHORT).show();

                    event.setSiteName(siteText.getText().toString().trim());
                    event.setEventDate(d);
                    event.setLoc(new GeoPoint(userclickLatLng.latitude,userclickLatLng.longitude));
                    /*get firstName and lastname and set as owner*/
                    db.collection("Users").document(uid).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    user = task.getResult().toObject(users.class);
                                    event.setOwner(user.firstName+" "+user.lastName);
                                    event.setOwnerId(uid);




                                    db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                                            .add(event).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            event.setPlaceId(task.getResult().getId());
                                            copySite(event, task.getResult().getId());
                                            Toast.makeText(Map1.this, "Successfully Added!", Toast.LENGTH_SHORT).show();

                                            clearFields();

                                        }
                                    });
                                }
                            });



                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }


        }
    };



    ClusterManager mCluster;
    Events event1 = new Events();

    public void setupCluster(){

        mMap.clear();

        mCluster = new ClusterManager<Clustering>(this,mMap);
        mCluster.setRenderer(new markerClusterRenderer(this,mMap,mCluster));
        mMap.setOnCameraIdleListener(mCluster);
        mMap.setOnMarkerClickListener(mCluster);
        mMap.setOnInfoWindowClickListener(mCluster);





        mCluster.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener() {
            @Override
            public boolean onClusterItemClick(final ClusterItem clusterItem) {
                userclickLatLng = null;
                TableRow garbage_weight_row = findViewById(R.id.garbage_weight_row);
                garbage_weight_row.setVisibility(View.VISIBLE);

                EditText garbageweight = findViewById(R.id.garbage_weight);
                final Button deleteButton = findViewById(R.id.deletebutton);

                cardViewanimator.start();
                mCardView.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);

                addsiteBtn.setOnClickListener(null);



               final Events eventfrompopulate =  clusteringEventsHashMaphashMap.get(clusterItem);

                dateText.setText(eventfrompopulate.getDateNormal(eventfrompopulate.eventDate));
                timeText.setText(eventfrompopulate.gettimeNormal(eventfrompopulate.eventDate));
                siteText.setText(eventfrompopulate.getSiteName());
                try{
                    garbageweight.setText(String.valueOf(eventfrompopulate.getGarbageWeight()));
                }
                catch (Exception e){

                }




                addsiteBtn.setText("UPDATE");



                addsiteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateAndReviseSite(eventfrompopulate);



                    }
                });



                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteSite(eventfrompopulate);
                        mCardView.setVisibility(View.INVISIBLE);
                        deleteButton.setVisibility(View.INVISIBLE);


                        db.collection("Sites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                mMap.clear();
//                                objects.clear();
//                                clusteringEventsHashMaphashMap.clear();
                                mCluster.removeItem(clusterItem);
                                mCluster.cluster();
//                                mCluster.clearItems();
//                                populatePoints(task);



                            }
                        });


                    }
                });






                return false;
            }
        });












    }



    public void updateAndReviseSite(final Events e){

        SimpleDateFormat timeStamp = new SimpleDateFormat("dd/MMM/yyyy hh : mm a");
        StringBuilder sb= new StringBuilder();
        final Events event = new Events();
        EditText garbageweight = findViewById(R.id.garbage_weight);

        final String uid = mAuth.getCurrentUser().getUid();
        cardViewanimator.reverse();

        if(  !timeText.getText().toString().isEmpty() && !dateText.getText().toString().isEmpty()
                && !siteText.getText().toString().isEmpty()){

            sb.append(dateText.getText().toString()+" ");
            sb.append(timeText.getText().toString());

            try {
                Date d = timeStamp.parse(sb.toString());
//                Toast.makeText(Map1.this, d.toString(), Toast.LENGTH_SHORT).show();

                event.setSiteName(siteText.getText().toString().trim());
                event.setEventDate(d);
                event.setLoc(new GeoPoint(e.loc.getLatitude(),e.loc.getLongitude()));
                try{
                    event.setGarbageWeight(0);
                    event.setGarbageWeight(Integer.parseInt(garbageweight.getText().toString()));
                }
                catch(Exception e2){
                    event.setGarbageWeight(0);
                }

                /*get firstName and lastname and set as owner*/
                db.collection("Users").document(uid).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            users user;
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                user = task.getResult().toObject(users.class);
                                event.setOwner(user.firstName+" "+user.lastName);
                                event.setOwnerId(uid);




                                db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                                        .document(e.mainrootSiteID)
                                        .set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        event.setPlaceId(e.mainrootSiteID);
                                        copySite(event, e.mainrootSiteID);
//                                        Toast.makeText(Map1.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Map1.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                        clearFields();


                                    }
                                });
                            }
                        });



            } catch (ParseException e1) {
                e1.printStackTrace();
            }


        }
        else{
//            Toast.makeText(this, "fields detected empty", Toast.LENGTH_SHORT).show();
        }










    }


    public void deleteSite(final Events delsite){




        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                .document(delsite.mainrootSiteID).delete();


        db.collection("Sites").document(delsite.mainrootSiteID).collection("People").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot d : task.getResult()){

                                db.collection("Users").document(d.getId()).collection("myPlaces")
                                        .document(delsite.mainrootSiteID).delete();


                            }


                            db.collection("Sites").document(delsite.mainrootSiteID).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Map1.this, "Site Deleted!", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }



                    }
                });









    }









    ArrayList<Clustering> objects = new ArrayList<Clustering>();

    HashMap<Clustering,Events> clusteringEventsHashMaphashMap= new HashMap<Clustering, Events>();

    public void populatePoints(Task<QuerySnapshot> task){
        objects.clear();
        clusteringEventsHashMaphashMap.clear();
        objects = new ArrayList<Clustering>();

        for(QueryDocumentSnapshot document : task.getResult()){

            Events eventName = document.toObject(Events.class);

            Clustering cluterItem = new Clustering(eventName.loc.getLatitude(), eventName.loc.getLongitude(),
                    eventName.siteName,eventName.owner,document.getId());

            eventName.setMainrootSiteID(cluterItem.mId);


            objects.add(cluterItem);
            clusteringEventsHashMaphashMap.put(cluterItem,eventName);


        }

        mCluster.addItems(objects.subList(0,objects.size()));

        mCluster.cluster();




    }













    public void copySite(Events event,String taskid) {



        db.collection("Sites").document(taskid).set(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



                if(task.isSuccessful()){
//                    Toast.makeText(Map1.this, "Copy Complete", Toast.LENGTH_SHORT).show();

                }

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

    public void showTimePicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                3, 00,false);

        timePickerDialog.show();

    }



    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        EditText dateinput = findViewById(R.id.dateText);


        Date date = new Date();
        Calendar c = Calendar.getInstance();
        
        
        c.set(year, month, dayOfMonth);
        
        
        if(c.getTimeInMillis()>Calendar.getInstance().getTimeInMillis()){

            dateinput.setText(dateFormat.format(c.getTime()));
            getDateandTime getDate = new getDateandTime();
            getDate.year  = year;
            getDate.month = month;
            getDate.day = dayOfMonth;
            
            
        }
        else{
//            Toast.makeText(this, "Invalid Date!", Toast.LENGTH_SHORT).show();

        }

       

    }




    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh : mm a");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.set(0,0,0,hourOfDay,minute);


        timeText.setText(dateFormat.format(c.getTime()));
        getDateandTime getDate = new getDateandTime();
        getDate.hour  = hourOfDay;
        getDate.mins = minute;
        TimeStampMaker();



    }

    public long TimeStampMaker(){

        Calendar c = Calendar.getInstance();
        getDateandTime gt = new getDateandTime();
        c.set(gt.year,gt.month,gt.day,gt.hour,gt.mins,0);
//        timeText.setText(String.valueOf(c.getTimeInMillis()));

        return c.getTimeInMillis();


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

    public void clearFields(){

        EditText editText  = findViewById(R.id.garbage_weight);
        editText.getText().clear();
        siteText.getText().clear();
        dateText.getText().clear();
        timeText.getText().clear();

    }



















}

