package com.socio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socio.Model.ImageModel;
import com.socio.Model.InstaFeedModal;
import com.socio.imagetoserver.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstaAdapter extends  RecyclerView.Adapter<InstaAdapter.InstaViewHolder>  {

    private ArrayList<ImageModel> instaFeedModalArrayList;
    private Context mcontext;

    public InstaAdapter(ArrayList<ImageModel> instaFeedModalArrayList, Context mcontext) {
        this.instaFeedModalArrayList = instaFeedModalArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public InstaAdapter.InstaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout for item of recycler view item.
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
       View view= layoutInflater.inflate(R.layout.recycler_item,parent,false);
        return new InstaViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull InstaViewHolder holder, int position) {

        //inside on bind view holder method we are setting ou data to each UI component.
        ImageModel instaFeedModal = instaFeedModalArrayList.get(position);
        Picasso.get().load(R.drawable.avtar).into(holder.authorIV);
        holder.authorTV.setText(instaFeedModal.getUsername());
        Picasso.get().load(R.drawable.games).into(holder.postIV);
        holder.likeTV.setText("6likes");
        holder.desctv.setText("this is a revenue generating feed showing plaltform wher kevnbjbv k bkjvbeskbkj");

    }

    @Override
    public int getItemCount() {
        return instaFeedModalArrayList.size();
    }

    public class InstaViewHolder extends RecyclerView.ViewHolder {

        CircleImageView authorIV;
        private TextView authorTV;
        private ImageView postIV,likeIV,commentIV,shareIV;
        private TextView likeTV,desctv;


        public InstaViewHolder(@NonNull View itemView) {
            super(itemView);
            authorIV = itemView.findViewById(R.id.idCVAuthor);
            authorTV = itemView.findViewById(R.id.idTVAuthorName);
            postIV = itemView.findViewById(R.id.idIVPost);
            likeIV=itemView.findViewById(R.id.idCVlike);
            commentIV=itemView.findViewById(R.id.idComment);
            shareIV=itemView.findViewById(R.id.idShare);
            likeTV = itemView.findViewById(R.id.idTVLikes);
            desctv = itemView.findViewById(R.id.idTVPostDesc);


        }
    }
}