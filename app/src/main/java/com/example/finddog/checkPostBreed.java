package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.type.LatLng;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class checkPostBreed extends Activity implements LocationListener {
    RecyclerView missingList;
    List<MissingBlog> listSum;
//    MyAdapter adapter;
    ArrayList LatList;
    String TAG;
    private FirebaseRecyclerOptions<MissingBlog> options;
    private FirebaseRecyclerAdapter<MissingBlog, ViewHolder> adapter;

    public TextView textView;
    @Override
    public void onLocationChanged(Location location) {
        final double longitude=location.getLongitude();
        final double latitude = location.getLatitude();

        //textView.setText("Long"+longitude+"\n"+"Lat"+latitude);
        missingList=findViewById(R.id.ShowSumBreed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        missingList.setLayoutManager(layoutManager);
        listSum = new ArrayList<>();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postmiss");

        Query firebaseSearchQuery = ref.orderByChild("breedML").equalTo(TextBreed);

        Log.d(TAG, "firebaseSearchQuery: "+firebaseSearchQuery);

        options = new FirebaseRecyclerOptions.Builder<MissingBlog>()
                .setQuery(firebaseSearchQuery, MissingBlog.class).build();

        adapter = new FirebaseRecyclerAdapter<MissingBlog, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ViewHolder viewHolder, int i, final MissingBlog missingBlog) {
                Picasso.get().load(missingBlog.getImage()).into(viewHolder.postImg);
                viewHolder.postName.setText(missingBlog.getName());
                viewHolder.postBreed.setText(missingBlog.getBreed());
                Log.d(TAG, "Search : "+"\n"+"postName : "+missingBlog.getName()
                        +"\n"+"postBreed : "+missingBlog.getBreed());

                final String post_key = getRef(i).getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleAdoptPage = new Intent(checkPostBreed.this,SingleMissing.class);
                        singleAdoptPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        singleAdoptPage.putExtra("blog_id",post_key);
                        Log.d(TAG, "Search : "+"\n"+"key : "+post_key+"\n"+"postName : "+missingBlog.getName()
                                +"\n"+"postBreed : "+missingBlog.getBreed());

                        startActivity(singleAdoptPage);
                    }
                });



            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row,parent,false);
                Log.d(TAG, "ViewHolder!! ");
                return new ViewHolder(view);
            }
        };
        adapter.startListening();
        missingList.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        Log.d(TAG, "adapter!! ");
      /*  ref.orderByChild("breedML").equalTo(TextBreed).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                listSum.clear();
                for ( final DataSnapshot dss:dataSnapshot.getChildren()){
                    ref.orderByChild("Long").startAt(longitude-0.007).endAt(longitude+0.007).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ref.orderByChild("Lat").startAt(latitude-0.007).endAt(latitude+0.007).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "ref : "+ref.orderByChild("breedML").equalTo(TextBreed));
                                    MissingBlog blog = dss.getValue(MissingBlog.class);
                                    listSum.add(blog);
                                    Log.d(TAG, "BLOG : "+blog);
                                    Log.d(TAG, "BLOG State : "+listSum);
                                    Log.d(TAG, "LONG : "+longitude);
                                    Log.d(TAG, "LAT : "+latitude);
                                    Log.d(TAG, "key : "+ref.getKey());


                                    adapter=new MyAdapter(getApplicationContext(), (ArrayList<MissingBlog>) listSum);
                                    Log.d(TAG, "adapter : "+adapter);

                                    ShowSum.setAdapter(adapter);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



    }
    private LocationManager locationManager;
    public String TextBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_post_breed);

        SharedPreferences sp = getSharedPreferences("BreedText", Context.MODE_PRIVATE);
        TextBreed = sp.getString("BreedMLText", "Error");

        TextView testBreed = findViewById(R.id.BreedText);
        testBreed.setText(TextBreed);

        //textView = (TextView) findViewById(R.id.Lat);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED &&
         ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                 != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        onLocationChanged(location);

    }

    private void FoundPost() {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
