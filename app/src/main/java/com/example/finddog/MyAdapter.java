package com.example.finddog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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


        }

        }
    }
