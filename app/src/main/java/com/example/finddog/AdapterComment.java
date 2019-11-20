package com.example.finddog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyHolder>  {

    public String img;

    Context context;
    List<ModelComment> commentList;

    public AdapterComment(Context context, List<ModelComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comment,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String usName = commentList.get(position).getUsername();
        String comments = commentList.get(position).getComment();
        img = commentList.get(position).getImage();

        holder.Uname.setText(usName);
        holder.cM.setText(comments);
        Picasso.get().load(img).into(holder.im);


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
       TextView Uname;
       TextView cM;
       ImageView im;

       public MyHolder(@NonNull View itemView) {
           super(itemView);

           im = itemView.findViewById(R.id.commentPhoto);
           Uname = itemView.findViewById(R.id.namee);
           cM = itemView.findViewById(R.id.comment);
           if (img!=null){
               im.setVisibility(View.GONE);
           }



       }
   }


}
