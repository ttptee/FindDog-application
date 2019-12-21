package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class allAdoptPost extends AppCompatActivity {

    private RecyclerView missingList;
    int state = 0;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencee;

    private FirebaseRecyclerOptions<MissingBlog> options;
    private FirebaseRecyclerAdapter<MissingBlog, ViewHolder> adapter;

    private ImageButton searchButton;
    String TAG;
    EditText searchBreed;
    ArrayList<MissingBlog> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_adopt_post);


        missingList = (RecyclerView) findViewById(R.id.findHomeAll);
        missingList.setHasFixedSize(true);

        searchBreed = (EditText)findViewById(R.id.searchbreed);
        arrayList = new ArrayList<>();

        ImageView logogo = findViewById(R.id.logo);
        logogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(allAdoptPost.this, MainActivity.class);
                startActivity(i);
            }
        });
        searchButton = (ImageButton) findViewById(R.id.searchBtn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*adapter.stopListening();*/
                /*String searchText = searchBreed.getText().toString();
                firebaseSearch(searchText);*/
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("postadopt");
        databaseReferencee = FirebaseDatabase.getInstance().getReference("postadopt");

        searchBreed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    firebaseSearch(s.toString());
                }
                else {
                    firebaseSearch("");
                }
            }
        });


            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
            missingList.setLayoutManager(gridLayoutManager);


            options = new FirebaseRecyclerOptions.Builder<MissingBlog>()
                    .setQuery(databaseReference, MissingBlog.class).build();

            adapter = new FirebaseRecyclerAdapter<MissingBlog, ViewHolder>(options) {
                @Override
                protected void onBindViewHolder(ViewHolder viewHolder, int i, final MissingBlog missingBlog) {
                    Picasso.get().load(missingBlog.getImage()).into(viewHolder.postImg);

                    viewHolder.postName.setText(missingBlog.getName());
                    viewHolder.postBreed.setText(missingBlog.getBreed());


                    final String post_key = getRef(i).getKey();
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent singleAdoptPage = new Intent(allAdoptPost.this, SingleAdopt.class);

                            singleAdoptPage.putExtra("blog_id", post_key);
                            Log.d(TAG, "NORMAL : " + "\n" + "key : " + post_key + "\n" + "postName : " + missingBlog.getName()
                                    + "\n" + "postBreed : " + missingBlog.getBreed());
                            startActivity(singleAdoptPage);
                        }
                    });


                }


                @NonNull
                @Override
                public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row, parent, false);

                    return new ViewHolder(view);
                }
            };

       /* GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        missingList.setLayoutManager(gridLayoutManager);*/
            adapter.startListening();
            missingList.setAdapter(adapter);










        ImageView add = findViewById(R.id.addPost);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (allAdoptPost.this,addPostAdopt.class);
                startActivity(i);
            }
        });

    }

    private void firebaseSearch(String s) {

        Log.d(TAG, "firebaseSearch: String : "+s);

        Query firebaseSearchQuery = databaseReference.orderByChild("breed")
                .startAt(s)
                .endAt(s+"\uf8ff");
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

                        Intent singleAdoptPage = new Intent(allAdoptPost.this,SingleAdopt.class);
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

                return new ViewHolder(view);
            }
        };
        adapter.startListening();
        missingList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

//    private void search(String s) {
//        Query query = databaseReference.orderByChild("breed")
//                .startAt(s)
//                .endAt(s+"\uf8ff");
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChildren())
//                {
//                    arrayList.clear();
//                    for (DataSnapshot dss : dataSnapshot.getChildren())
//                    {
//                        final MissingBlog missingBlog = dss.getValue(MissingBlog.class);
//                        arrayList.add(missingBlog);
//                        final String pos_key = dss.getKey();
//                        MyAdapter.MyAdapterViewHolder.class.getClass();
//
//
////                        if (dss.getKey()!=null){
////                        //final String pos_key = dss.getKey();
////                        Intent singleAdoptPage = new Intent(allAdoptPost.this,SingleAdopt.class);
////                        singleAdoptPage.putExtra("blog_id",pos_key);
////                        startActivity(singleAdoptPage);}
//                    }
//
//                    MyAdapter myAdapter = new MyAdapter(getApplicationContext(),arrayList);
//                    missingList.setAdapter(myAdapter);
//                    myAdapter.notifyDataSetChanged();
//
//                    Log.d(TAG, "State: "+state);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }




    @Override
    protected void onStart() {
        super.onStart();
        /*adapter.startListening();*/

    }
}
