package com.example.finddog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView postName;
    public TextView postBreed;
    public ImageView postImg;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        postName = (TextView) itemView.findViewById(R.id.postName);
        postBreed = (TextView) itemView.findViewById(R.id.postBreed);
        postImg = (ImageView)itemView.findViewById(R.id.postImage);


    }
}
