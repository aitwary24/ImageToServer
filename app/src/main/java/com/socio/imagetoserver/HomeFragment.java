package com.socio.imagetoserver;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socio.Adapter.InstaAdapter;
import com.socio.Adapter.InstagramFeedRVAdapter;
import com.socio.Model.ImageModel;
import com.socio.Model.InstaFeedModal;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    InstaAdapter adapter;
    ArrayList<ImageModel> imageModelArrayList;
    View view;
    public  HomeFragment () {


    }

    @Override
    public View onCreateView(LayoutInflater inflater ,ViewGroup container ,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_home, container, false);
       recyclerView = view.findViewById(R.id.idRVInstaFeeds);
          adapter=new InstaAdapter(imageModelArrayList,getContext());
          recyclerView.setAdapter(adapter);
          recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageModelArrayList=new ArrayList<>();
        String str="2022-12-17 09:01:15";
        Timestamp timestamp= Timestamp.valueOf(str);
        imageModelArrayList.add(new ImageModel("1","Image",
                Picasso.get().load(R.drawable.avtar).toString(),
                Picasso.get().load(R.drawable.highsports).toString(),
                "Steve",getString(R.string.app_name),
                timestamp.toString(),"THis is a demo url",4));

        imageModelArrayList.add(new ImageModel("2","Image",
                Picasso.get().load(R.drawable.avtar).toString(),
                Picasso.get().load(R.drawable.games).toString(),
                "Steve",getString(R.string.app_name),
                timestamp.toString(),"THis is a demo url",4));
        imageModelArrayList.add(new ImageModel("3","Image",
                Picasso.get().load(R.drawable.avtar).toString(),
                Picasso.get().load(R.drawable.highsports).toString(),
                "Steve",getString(R.string.app_name),
                timestamp.toString(),"THis is a demo url",4));
        imageModelArrayList.add(new ImageModel("4","Image",
                Picasso.get().load(R.drawable.avtar).toString(),
                Picasso.get().load(R.drawable.games).toString(),
                "Steve",getString(R.string.app_name),
                timestamp.toString(),"THis is a demo url",4));
        imageModelArrayList.add(new ImageModel("5","Image",
                Picasso.get().load(R.drawable.avtar).toString(),
                Picasso.get().load(R.drawable.highsports).toString(),
                "Steve",getString(R.string.app_name),
                timestamp.toString(),"THis is a demo url",4));
        imageModelArrayList.add(new ImageModel("6","Image",
                Picasso.get().load(R.drawable.avtar).toString(),
                Picasso.get().load(R.drawable.games).toString(),
                "Steve",getString(R.string.app_name),
                timestamp.toString(),"THis is a demo url",4));

    }
}