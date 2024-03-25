package com.gundemgaming.fukantin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.gundemgaming.fukantin.adapters.MyPostsAdapter;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.models.Post;

import java.util.List;

public class FragmentPosts_MP extends Fragment {

    //Fragment Elements
    private ConstraintLayout CL_MY_PROFILE;
    private RecyclerView recyclerViewPost_MP;
    private TextView textViewZeroPostInfo_MP;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //SharedPrefrences Data
    private int userId;

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_my_profile, container, false);
        findViewById(rootView);
        getFragmentLayout();
        recyclerViewSettings(rootView);

        //SharedPreferences Data
        getSharedPreferencesDatas();

        //Get MyPosts
        databaseRepository = new DatabaseRepository(rootView.getContext(), CL_MY_PROFILE);
        databaseRepository.getMyPosts(rootView, textViewZeroPostInfo_MP, recyclerViewPost_MP, userId);

        return rootView;
    }

    public void getSharedPreferencesDatas() {
        sharedPreferencesLogInInfo = this.getActivity().getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        userId = sharedPreferencesLogInInfo.getInt("userId", 0);
    }

    public void recyclerViewSettings(View rootView) {
        recyclerViewPost_MP.setHasFixedSize(true);
        recyclerViewPost_MP.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }

    public void getFragmentLayout() {
        CL_MY_PROFILE = getActivity().findViewById(R.id.CL_MY_PROFILE);
    }

    public void findViewById(View rootView) {
        recyclerViewPost_MP = rootView.findViewById(R.id.recyclerViewPost_MP);
        textViewZeroPostInfo_MP = rootView.findViewById(R.id.textViewZeroPostInfo_MP);
    }
}
