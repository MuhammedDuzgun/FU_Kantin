package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.tools.AlertManager;
import com.gundemgaming.fukantin.tools.BannedWordManager;

public class WriteReplyActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_WRITE_REPLY;
    private EditText editTextReply;
    private Button buttonShareReply;
    private AdView adviewBanner_WRITE_REPLY;

    //Activity Datas
    private int postId;
    private int userId;
    private String reply;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //Intent
    private Intent intent;

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    //BannedWords
    private BannedWordManager bannedWordManager;
    private boolean isTextBanned;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_reply);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //Get Intent Data
        getIntentDatas();

        //Get SharedPreferences Data
        getSharedPreferencesDatas();

        //Database Repository
        getRepository();

        //Button - ShareReply
        buttonShareReplySetOnClickListener();

    }

    public void getSharedPreferencesDatas() {
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        userId = sharedPreferencesLogInInfo.getInt("userId", 0);
    }

    public void getIntentDatas() {
        intent = getIntent();
        postId = intent.getIntExtra("postId", 0);
    }

    public void buttonShareReplySetOnClickListener() {
        buttonShareReply.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_WRITE_REPLY, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            boolean isValuesReady = replyValueChecker();
            if(isValuesReady) {
                if(postId == 0 || userId == 0) {
                    AlertManager.showErrorAlert(CL_WRITE_REPLY, "Bir Şeyler Ters Gitti.");
                } else {
                    reply = editTextReply.getText().toString().trim();

                    if(isTextContainsBannedWord(reply)) {
                        AlertManager.showErrorAlert(CL_WRITE_REPLY, "Cevapta Sakıncalı Kelimeler Tespit Edildi.");
                    } else {
                        //INSERT REPLY
                        databaseRepository.insertReply(postId, userId, reply);
                    }
                }
            } else {
                AlertManager.showErrorAlert(CL_WRITE_REPLY, "Cevap Bölümü Boş Bırakılamaz.");
            }
        });
    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(WriteReplyActivity.this, CL_WRITE_REPLY);
    }

    public boolean isTextContainsBannedWord(String text) {
        bannedWordManager = new BannedWordManager(text);
        isTextBanned = bannedWordManager.isUsernameBanned();

        if(isTextBanned) {
            return true;
        } else {
            return false;
        }
    }

    public boolean replyValueChecker() {
        reply = editTextReply.getText().toString();

        if(reply.equals("")) {
            return false;
        }
            return true;
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_WRITE_REPLY.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(WriteReplyActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_WRITE_REPLY = findViewById(R.id.CL_WRITE_REPLY);
        editTextReply = findViewById(R.id.editTextReply);
        buttonShareReply = findViewById(R.id.buttonShareReply);
        adviewBanner_WRITE_REPLY = findViewById(R.id.adviewBanner_WRITE_REPLY);
    }


}