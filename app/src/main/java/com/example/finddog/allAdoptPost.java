package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    private DatabaseReference databaseReference;

    private FirebaseRecyclerOptions<MissingBlog> options;
    private FirebaseRecyclerAdapter<MissingBlog, ViewHolder> adapter;

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


        databaseReference = FirebaseDatabase.getInstance().getReference().child("postadopt");

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
                    search(s.toString());
                }
                else {
                    search("");
                }
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        missingList.setLayoutManager(gridLayoutManager);










        options = new FirebaseRecyclerOptions.Builder<MissingBlog>()
                .setQuery(databaseReference, MissingBlog.class).build();

        adapter = new FirebaseRecyclerAdapter<MissingBlog, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ViewHolder viewHolder, int i, MissingBlog missingBlog) {
                Picasso.get().load(missingBlog.getImage()).into(viewHolder.postImg);

                viewHolder.postName.setText(missingBlog.getName());
                viewHolder.postBreed.setText(missingBlog.getBreed());


                final String post_key = getRef(i).getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleAdoptPage = new Intent(allAdoptPost.this,SingleAdopt.class);
                        singleAdoptPage.putExtra("blog_id",post_key);
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

        /*GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
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

    private void search(String s) {
        Query query = databaseReference.orderByChild("breed")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren())
                {
                    arrayList.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren())
                    {
                        final MissingBlog missingBlog = dss.getValue(MissingBlog.class);
                        arrayList.add(missingBlog);
                        final String pos_key = dss.getKey();
                        MyAdapter.MyAdapterViewHolder.class.getClass();


                        if (dss.getKey()!=null){
                        //final String pos_key = dss.getKey();
                        Intent singleAdoptPage = new Intent(allAdoptPost.this,SingleAdopt.class);
                        singleAdoptPage.putExtra("blog_id",pos_key);
                        startActivity(singleAdoptPage);}
                    }

                    MyAdapter myAdapter = new MyAdapter(getApplicationContext(),arrayList);
                    missingList.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*adapter.startListening();*/

    }
}
