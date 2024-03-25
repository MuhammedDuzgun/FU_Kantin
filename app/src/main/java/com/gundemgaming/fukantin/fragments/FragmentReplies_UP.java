package com.gundemgaming.fukantin.fragments;

import android.content.Intent;
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
import com.gundemgaming.fukantin.adapters.ReplyAdapter;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.models.Reply;

import java.util.List;


public class FragmentReplies_UP extends Fragment {

    //Fragment Elements
    private ConstraintLayout CL_USER_PROFILE;
    private RecyclerView recyclerViewReply_UP;
    private TextView textViewZeroReplyInfo_UP;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //Intent Data
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_replies_user_profile, container, false);
        findViewById(rootView);
        getFragmentLayout();
        recyclerViewSettings(rootView);

        //Get IntentData
        getIntentData();

        //Get Replies
        databaseRepository = new DatabaseRepository(rootView.getContext(), CL_USER_PROFILE);
        databaseRepository.getUsersReplies(rootView,textViewZeroReplyInfo_UP,recyclerViewReply_UP,userId);

        return rootView;
    }

    public void getIntentData() {
        Intent intent = getActivity().getIntent();
        userId = intent.getIntExtra("userId" ,0);
    }

    public void recyclerViewSettings(View rootView) {
        recyclerViewReply_UP.setHasFixedSize(true);
        recyclerViewReply_UP.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
    }

    public void getFragmentLayout() {
        CL_USER_PROFILE = getActivity().findViewById(R.id.CL_USER_PROFILE);
    }

    public void findViewById(View rootView) {
        recyclerViewReply_UP = rootView.findViewById(R.id.recyclerviewReply_UP);
        textViewZeroReplyInfo_UP = rootView.findViewById(R.id.textViewZeroReplyInfo_UP);
    }

}
