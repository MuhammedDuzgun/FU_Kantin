package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.gundemgaming.fukantin.adapters.ReplyAdapter;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.tools.AlertManager;

public class ReadRepliesActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_READ_REPLIES;
    private SwipeRefreshLayout swipeRefreshReplies;
    private RecyclerView recyclerviewReply_RR;
    private TextView textViewZeroReplyInfo;
    private TextView textViewPostTitle_RR;
    private TextView textViewPost_RR;
    private TextView textViewUsername_RR;
    private TextView textViewDate_RR;
    private TextView textViewReplyNumber_RR;
    private ImageButton imageButtonReplyPost_RR;
    private AdView adviewBanner_READ_REPLIES;

    //Activity Data
    private int postId;
    private String postTitle;
    private String post;
    private String username;
    private String date;
    private String numberOfReplies;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //Intent Data
    private Intent intent;

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_replies);
        findViewById();
        recyclerViewSettings();

        //Adversitements
        adManager();
        showBannerAd();

        //Intent Data
        getIntentDatas();

        //Database Repsitory
        getRepository();

        //Data - Replies
        databaseRepository.getReplies(postId, recyclerviewReply_RR, textViewZeroReplyInfo);

        //Button - ReplyPost
        imageButtonReplyPost_RRSetOnClickListener();

        //Refresh Activity
        refreshActivity();

    }

    public void getIntentDatas() {
        intent = getIntent();

        postId = intent.getIntExtra("postId", 0);
        postTitle = intent.getStringExtra("postTitle");
        post = intent.getStringExtra("post");
        username = intent.getStringExtra("username");
        date = intent.getStringExtra("date");
        numberOfReplies = intent.getStringExtra("numberOfReplies");

        textViewPostTitle_RR.setText(postTitle);
        textViewPost_RR.setText(post);
        textViewUsername_RR.setText(username);
        textViewDate_RR.setText(date);
        textViewReplyNumber_RR.setText(numberOfReplies);

    }

    public void imageButtonReplyPost_RRSetOnClickListener() {
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        boolean isUserEntered = sharedPreferencesLogInInfo.getBoolean("isUserEntered" ,false);

        imageButtonReplyPost_RR.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_READ_REPLIES, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if(isUserEntered) {
                Intent intent = new Intent(ReadRepliesActivity.this, WriteReplyActivity.class);
                intent.putExtra("postId", postId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ReadRepliesActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(ReadRepliesActivity.this, CL_READ_REPLIES);
    }

    public void refreshActivity() {
        swipeRefreshReplies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //GET REPLIES
                databaseRepository.getReplies(postId, recyclerviewReply_RR, textViewZeroReplyInfo);
                swipeRefreshReplies.setRefreshing(false);
            }
        });
    }

    public void recyclerViewSettings() {
        recyclerviewReply_RR.setHasFixedSize(true);
        recyclerviewReply_RR.setLayoutManager(new LinearLayoutManager(this));
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_READ_REPLIES.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(ReadRepliesActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_READ_REPLIES = findViewById(R.id.CL_READ_REPLIES);
        swipeRefreshReplies = findViewById(R.id.swiperefreshReplies);
        recyclerviewReply_RR = findViewById(R.id.recyclerviewReply_RR);
        textViewZeroReplyInfo = findViewById(R.id.textViewZeroReplyInfo);
        textViewPostTitle_RR = findViewById(R.id.textViewPostTitle_RR);
        textViewPost_RR = findViewById(R.id.textViewPost_RR);
        textViewUsername_RR = findViewById(R.id.textViewUsername_RR);
        textViewDate_RR = findViewById(R.id.textViewPostDate_RR);
        textViewReplyNumber_RR = findViewById(R.id.textViewReplyNumber_RR);
        imageButtonReplyPost_RR = findViewById(R.id.imageButtonReplyPost_RR);
        adviewBanner_READ_REPLIES = findViewById(R.id.adviewBanner_READ_REPLIES);
    }

}