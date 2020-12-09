package com.example.cleanup_vn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import com.google.maps.android.PolyUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class Map2 extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {



    private GoogleMap mMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ClusterManager<Clustering> mCluster;
    CardView mCardView2, viewpeoplecard;
    ConstraintLayout cl;
    Button join;
    Button viewPeople;
    ImageButton closebutton1, closebutton2;
    TextView sitenamelabel,datetext2,timetext2, owner, numberofusrs;
    SearchView searchView;
    Spinner siteSearchspin;
    String searchType;
    FusedLocationProviderClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        searchView = findViewById(R.id.search1);

        client = LocationServices.getFusedLocationProviderClient(this);



      loadSites();

        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            users u = task.getResult().toObject(users.class);

                            if(u.typeOfUser.equals("admin")){

                                FloatingActionButton fab1 = findViewById(R.id.filterFloatingbutton);
                                fab1.setVisibility(View.VISIBLE);
                                fab1.setOnClickListener(filterclicklisten);



                            }

                        }

                    }
                });




    }

    private void loadSites() {


            try{
                String locations = getIntent().getStringExtra("firestoreRef");

                FirebaseFirestore.getInstance().collection(locations).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            setUpCluster();
                            populatePoints(task);

                        }
                    }
                });
            }
            catch (Exception e){

                db.collection("Sites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            setUpCluster();
                            populatePoints(task);

                        }
                    }
                });


            }






    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;




//        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//
//            @Override
//            public void onSuccess(Location location) {
//
//                LatLng viet = new LatLng(location.getLatitude(), location.getLongitude());
//
//                //LatLng sydney = new LatLng(10.7295612, 106.6937702);
//               // mMap.addMarker(new MarkerOptions().position(sydney).title("You are Here"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viet, 12f));
//                mMap.setMyLocationEnabled(true);
//
//
//
//            }
//
//
//        });



        LatLng viet = new LatLng(10.7295612, 106.6937702);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viet, 12f));




        //mMap.setMyLocationEnabled(true);

        mCardView2 = findViewById(R.id.cardView2);
        cl = findViewById(R.id.cl1);
        datetext2 = findViewById(R.id.dateText2);
        timetext2 = findViewById(R.id.timeText2);
        sitenamelabel = findViewById(R.id.siteNameLabel);
        join = findViewById(R.id.joinButton);
        closebutton1  = findViewById(R.id.closeButton);
        owner = findViewById(R.id.ownerName);
        viewpeoplecard = findViewById(R.id.viewPeoplecardView);
        viewPeople = findViewById(R.id.viewPeople);
        closebutton2 = findViewById(R.id.closePeopleCard);
        numberofusrs = findViewById(R.id.numberofusers);





        setupSpinner();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if(searchType==null || searchType.equals("Site Name")){
                    getSearchResults(query, "siteName");


                }
                else if(searchType.equals("Site Owner")){

                    getSearchResults(query, "owner");

                }
                else if(query==""){
//                            mCluster.clearItems();
//                        loadSites();

                }




                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });










    }


    public void getSearchResults(final String query, final String fieldName){

        db.collection("Sites").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            mMap.clear();
                           // setUpCluster();

                            populatePointsforSearch(task,query,fieldName);



                        }
                        else{
//                            Toast.makeText(Map2.this, "No Results!", Toast.LENGTH_SHORT).show();

                            String a;

                        }

                    }
                });



    }







    ArrayList<Clustering> objects = new ArrayList<Clustering>();
    ArrayList<Events> eventObjects = new ArrayList<Events>();
    HashMap<Clustering,Events> clusteringEventsHashMaphashMap= new HashMap<Clustering, Events>();

    public void populatePoints(Task<QuerySnapshot> task){
        objects.clear();
        clusteringEventsHashMaphashMap.clear();
        objects = new ArrayList<Clustering>();
        eventObjects = new ArrayList<Events>();
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


    public void populatePointsforSearch(Task<QuerySnapshot> task,String query, String fieldName){

       objects.clear();
       clusteringEventsHashMaphashMap.clear();
       mCluster.clearItems();

         objects = new ArrayList<Clustering>();
         eventObjects = new ArrayList<Events>();
        for(QueryDocumentSnapshot document : task.getResult()){

            Events eventName = document.toObject(Events.class);

            Clustering cluterItem = new Clustering(eventName.loc.getLatitude(), eventName.loc.getLongitude(),
                    eventName.siteName,eventName.owner,document.getId());

            if(fieldName.equals("owner")) {

                if (eventName.owner.toLowerCase().startsWith(query.toLowerCase())) {
                    eventName.setMainrootSiteID(cluterItem.mId);
                    objects.add(cluterItem);
                    clusteringEventsHashMaphashMap.put(cluterItem, eventName);

                    if(objects.size()>0) {

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(objects.get(0).mPos, 12f));

                    }


                }

            }

            else if(fieldName.equals("siteName")){

                if (eventName.siteName.toLowerCase().startsWith(query.toLowerCase())) {
                    eventName.setMainrootSiteID(cluterItem.mId);
                    objects.add(cluterItem);
                    clusteringEventsHashMaphashMap.put(cluterItem, eventName);

                    if(objects.size()>0) {

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(objects.get(0).mPos, 12f));

                    }


                }

            }
            else{

                eventName.setMainrootSiteID(cluterItem.mId);
                objects.add(cluterItem);
                clusteringEventsHashMaphashMap.put(cluterItem, eventName);


            }




        }

        mCluster.addItems(objects.subList(0,objects.size()));
        mCluster.cluster();
        if(objects.size()==0){
            Toast.makeText(this, "No Results Found!", Toast.LENGTH_SHORT).show();
        }






    }











    Events event1 = new Events();
    public void setUpCluster(){

        mCluster = new ClusterManager<Clustering>(this,mMap);
        mCluster.setRenderer(new markerClusterRenderer(this,mMap,mCluster));
        mMap.setOnCameraIdleListener(mCluster);
        mMap.setOnMarkerClickListener(mCluster);
        mMap.setOnInfoWindowClickListener(mCluster);



        final ObjectAnimator ob1 = ObjectAnimator.ofFloat(mCardView2,"translationX",-1000f,0f).setDuration(300);
        final ObjectAnimator ob2 = ObjectAnimator.ofFloat(mCardView2,"translationX",0f,1000f).setDuration(300);
        ob1.setInterpolator(new AccelerateDecelerateInterpolator());
        ob2.setInterpolator(new AccelerateDecelerateInterpolator());


        join.setOnClickListener(joinClickListen);



        mCluster.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Clustering>() {
            @Override
            public boolean onClusterItemClick(Clustering clustering) {
                join.setVisibility(View.VISIBLE);

                event1 = clusteringEventsHashMaphashMap.get(clustering);
                updateEventCardView(event1);

                try {
//                    Toast.makeText(Map2.this, event1.getPlaceId(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){

//                    Toast.makeText(Map2.this, "not available", Toast.LENGTH_SHORT).show();
                }



                return false;


            }
        });



        mCluster.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<Clustering>() {

            @Override
            public void onClusterItemInfoWindowClick(Clustering clustering) {


                join.setVisibility(View.VISIBLE);
               // getRoutetoMarker(clustering.mPos);


                if(mCardView2.getX()>20 && mCardView2.getX()<100 && mCardView2.isShown()){
                    ob2.start();
                    ob2.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ob1.start();
                        }
                    });

                }
                else{
                    mCardView2.setVisibility(View.VISIBLE);
                    ob1.start();
                }



               event1 = clusteringEventsHashMaphashMap.get(clustering);


                updateEventCardView(event1);

               getRoutetoMarker(new LatLng(event1.loc.getLatitude(),event1.loc.getLongitude()));

               Toast.makeText(Map2.this, event1.eventDate.toString(), Toast.LENGTH_SHORT).show();




            }
        });


        closebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ob2.start();
                ob2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mCardView2.setVisibility(View.INVISIBLE);
                        ob2.removeAllListeners();
                    }
                });



            }
        });

        closebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpeoplecard.setVisibility(View.INVISIBLE);

            }
        });




        findViewById(R.id.viewPeople).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Map2.this, "Viewing Site Participants", Toast.LENGTH_SHORT).show();

                db.collection("Sites").document(event1.mainrootSiteID).collection("People").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        RecyclerView mRecycler;




                        mRecycler = findViewById(R.id.mPeopleRecycler);
                        RecyclerView.LayoutManager mlayoutmanager= new LinearLayoutManager(Map2.this);
                        mRecycler.setHasFixedSize(true);

                        ArrayList<users> usersListonSite = new ArrayList<>();

                        if(task.isSuccessful()){
//                            Toast.makeText(Map2.this, "Success!", Toast.LENGTH_SHORT).show();
                            for(QueryDocumentSnapshot d : task.getResult()){

                                usersListonSite.add(d.toObject(users.class));




                            }

                            RecyclerView.Adapter madapter = new recyclerviewAdapter(usersListonSite);
                            mRecycler.setLayoutManager(mlayoutmanager);
                            mRecycler.setAdapter(madapter);

                        }



                    }
                });

               viewpeoplecard.setVisibility(View.VISIBLE);

            }
        });


    }

    public void setAdapter(RecyclerView.Adapter madapter, Events events1, View mRecycler, RecyclerView.LayoutManager mlayoutmanager){


    }


    View.OnClickListener joinClickListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                getUserData();
        }
    };


/* add person to root directory site collection */
    public void joinEvent(final String placeId, users user){




            //add user to root "Sites" directory
           db.collection("Sites").document(placeId).collection("People")
                   .document(mAuth.getCurrentUser().getUid())
                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                  if(task.isSuccessful()){
                      Toast.makeText(Map2.this, "Successfully Joined!", Toast.LENGTH_SHORT).show();
//                      Toast.makeText(Map2.this, "Successfully Added to Main", Toast.LENGTH_SHORT).show();
                      addtoMyPlaces();
                      

                  }
                  else{
                      
//                      Toast.makeText(Map2.this, "Error adding to main", Toast.LENGTH_SHORT).show();
                      
                  }
               }
           });



    }

    public void updateEventCardView(Events event1){

        sitenamelabel.setText(capitalize(event1.siteName));
        datetext2.setText(event1.getDateNormal(event1.eventDate));
        timetext2.setText(event1.gettimeNormal(event1.eventDate));
        owner.setText(printwithSpaces(capitalize(event1.owner).split(" ")).trim());
        numberofusrs.setText(String.valueOf(event1.sitePop));
    }

    public String capitalize(String str){


        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }

        return builder.toString();
    }

    public String printwithSpaces(String[] str){

        String name = new String();
        for(String s : str) {
            name += s +"\n";

        }
        return name;


    }




    users user = new users();
    public void getUserData(){


        if(event1.placeId!=null) {
            //get already logged in user profile
    db.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    user = documentSnapshot.toObject(users.class);

                    checkIfDocExiststoCountandJoin(event1.mainrootSiteID,user);


                    copytoSupervisorProfile();

                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
//            Toast.makeText(Map2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });


                }
    }

    public void copytoSupervisorProfile(){

        db.collection("Users").document(event1.ownerId).collection("places")
                .document(event1.placeId).collection("People").document(mAuth.getCurrentUser().getUid())
                .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                
                if(task.isSuccessful()){
//                    Toast.makeText(Map2.this, "Complete!", Toast.LENGTH_SHORT).show();
                }
                else{
//                    Toast.makeText(Map2.this, "Error copying!", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

    }

    /*SPINNER SETUP*/
    public void setupSpinner(){
        siteSearchspin = findViewById(R.id.spinner_site_search);

        final String[] arraySpin = new String[]{
                "Search By...","Site Name", "Site Owner"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                arraySpin );

        siteSearchspin.setAdapter(adapter);

        siteSearchspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){

//                    Toast.makeText(Map2.this, arraySpin[position]+"", Toast.LENGTH_SHORT).show();
                    searchType = arraySpin[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchType = arraySpin[1];
            }
        });

    }

    public void checkIfDocExiststoCountandJoin(final String id, final users user){


        db.collection("Sites").document(id).collection("People")
                .document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot d = task.getResult();
                        if(!d.exists()){

//                            Toast.makeText(Map2.this, "Exists000000000000", Toast.LENGTH_LONG).show();
                            db.collection("Sites").document(id)
                                    .update("sitePop", FieldValue.increment(1));

                        }
                        else{


                        }
                        joinEvent(id,user);

                    }
                });





    }



    public void addtoMyPlaces(){

        db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("myPlaces")
                .document(event1.mainrootSiteID).set(event1);


    }


    public void getRoutetoMarker(LatLng destination){

        LatLng sydney = new LatLng(10.7295612, 106.6937702);

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(sydney, destination).key("AIzaSyB5TwxTm0D-hMPrkV8A23sr4RD-RAW7iyA")
                .build();


        routing.execute();

    }


    @Override
    public void onRoutingFailure(RouteException e) {
//        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

         List<Polyline> polylines = new ArrayList<>();

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.

        final PolylineOptions polyOptions1 = new PolylineOptions();

        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i <route.size(); i++) {


            PolylineOptions polyOptions = new PolylineOptions();

            polyOptions1.addAll(route.get(i).getPoints());
            polyOptions.width(10 );
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);


//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }

        List<Polyline> finalPolylines = polylines;
        findViewById(R.id.filterFloatingbutton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(PolyUtil.isLocationOnEdge(new LatLng(event1.loc.getLatitude(),event1.loc.getLongitude()),
                        polyOptions1.getPoints(),false,500)){

                Toast.makeText(Map2.this, "present ", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(Map2.this, "not present", Toast.LENGTH_SHORT).show();
            }


            }
        });

    }




    @Override
    public void onRoutingCancelled() {

    }

    View.OnClickListener filterclicklisten = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            db.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("places")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        populatePoints(task);

                        mMap.clear();
                        mCluster.clearItems();


                        if(objects.size()>0) {

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(objects.get(0).mPos, 12f));

                        }


                    }



                }
            });


        }
    };






}


