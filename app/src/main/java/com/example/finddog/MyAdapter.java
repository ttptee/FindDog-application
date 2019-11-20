package com.example.finddog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.core.content.ContextCompat.startActivity;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder> {

    public Context c;
    public ArrayList<MissingBlog> arrayList;
    public MyAdapter (Context c, ArrayList<MissingBlog> arrayList)
    {
        this.c=c;
        this.arrayList=arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_row,parent,false);

        return new MyAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {
        MissingBlog missingBlog = arrayList.get(position);

        holder.postName.setText(missingBlog.getName());
        holder.postBreed.setText(missingBlog.getBreed());
        Picasso.get().load(missingBlog.getImage()).into(holder.postImg);

    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder{

        public TextView postName;
        public TextView postBreed;
        public ImageView postImg;

        public MyAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            postName = (TextView) itemView.findViewById(R.id.postName);
            postBreed = (TextView) itemView.findViewById(R.id.postBreed);
            postImg = (ImageView)itemView.findViewById(R.id.postImage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    arrayList.get(getAdapterPosition());
                    int mPost_key = getAdapterPosition();
                    Intent singleAdoptPage = new Intent(c,SingleAdopt.class);
                    singleAdoptPage.putExtra("blog_id", mPost_key);
                    c.startActivity(singleAdoptPage);
                    Toast.makeText(c, mPost_key,
                            Toast.LENGTH_LONG).show();



                }
            });


        }

        }
    }
