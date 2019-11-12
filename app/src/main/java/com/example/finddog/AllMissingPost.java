package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AllMissingPost extends AppCompatActivity {

    private RecyclerView missingList;

    private DatabaseReference databaseReference;

    private FirebaseRecyclerOptions<MissingBlog> options;
    private FirebaseRecyclerAdapter<MissingBlog, ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_missing_post);

        missingList = (RecyclerView) findViewById(R.id.missingListAll);
        missingList.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");

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
                        Intent singleMissingPage = new Intent(AllMissingPost.this,SingleMissing.class);
                        singleMissingPage.putExtra("blog_id",post_key);
                        startActivity(singleMissingPage);
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        missingList.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        missingList.setAdapter(adapter);






        ImageView add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (AllMissingPost.this,detaildogJava.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
            adapter.startListening();

    }

   /* @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.startListening();

    }*/
}
