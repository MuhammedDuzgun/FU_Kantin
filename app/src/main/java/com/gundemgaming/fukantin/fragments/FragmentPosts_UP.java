package com.gundemgaming.fukantin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gundemgaming.fukantin.R;
import com.gundemgaming.fukantin.adapters.PostAdapter;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.models.Post;

import java.util.List;

public class FragmentPosts_UP extends Fragment {

    //Fragment Elements
    private ConstraintLayout CL_USER_PROFILE;
    private RecyclerView recyclerViewPost_UP;
    private TextView textViewZeroPostInfo_UP;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //Intent Data
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_user_profile, container, false);
        findViewById(rootView);
        getFragmentLayout();
        recyclerViewSettings(rootView);

        //Intent Data
        getIntentData();

        //Get Posts
        databaseRepository = new DatabaseRepository(rootView.getContext(), CL_USER_PROFILE);
        databaseRepository.getUsersPosts(rootView, textViewZeroPostInfo_UP, recyclerViewPost_UP, userId);

        return rootView;
    }


    public void getIntentData() {
        Intent intent = getActivity().getIntent();
        userId = intent.getIntExtra("userId" ,0);
        Log.i("userId", "gelen ıd : " + String.valueOf(userId));
    }

    public void recyclerViewSettings(View rootView) {
        recyclerViewPost_UP.setHasFixedSize(true);
        recyclerViewPost_UP.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }

    public void getFragmentLayout() {
        CL_USER_PROFILE = getActivity().findViewById(R.id.CL_USER_PROFILE);
    }

    public void findViewById(View rootView) {
        recyclerViewPost_UP = rootView.findViewById(R.id.recyclerviewPost_UP);
        textViewZeroPostInfo_UP = rootView.findViewById(R.id.textViewZeroPostInfo_UP);
    }

}
