package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.tools.AlertManager;
import com.gundemgaming.fukantin.tools.BannedWordManager;

public class SaveUserActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_SAVE_USER;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonInsertUser;
    private CheckBox checkBoxUserAgreement;
    private AdView adviewBanner_SAVE_USER;


    //Activity Datas
    private String usernameFromLoginPage;
    private String passwordFromLoginPage;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    //Shared Preferences Editor
    public SharedPreferences.Editor editorLogInInfo;

    //BannedWordManager;
    private BannedWordManager bannedWordManager;
    private boolean isUsernameBanned;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //Getting Datas from login page
        getIntentDatas();

        //SharedPrefrences
        getSharedPrefrences();

        //Database Repository
        getRepository();

        //Checkbox UserAgreement
        setCheckBoxLink();

        //Button - Insert User
        buttonInsertUserSetOnClickUser();

    }

    public void getIntentDatas() {
        Intent intent = getIntent();

        usernameFromLoginPage = intent.getStringExtra("username_from_loginpage");
        passwordFromLoginPage = intent.getStringExtra("password_from_loginpage");

        editTextUsername.setText(usernameFromLoginPage);
        editTextPassword.setText(passwordFromLoginPage);

    }

    public void getSharedPrefrences() {
        //SharedPreferences
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        editorLogInInfo = sharedPreferencesLogInInfo.edit();
    }

    public void setCheckBoxLink() {
        checkBoxUserAgreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void buttonInsertUserSetOnClickUser() {
        buttonInsertUser.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_SAVE_USER, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            //CheckBox UserAgreement
            if(checkBoxUserAgreement.isChecked()) {
                isUsernameBanned();
            } else {
                AlertManager.showErrorAlert(CL_SAVE_USER, "Kullanıcı sözleşmesini kabul etmediniz");
            }

        });
    }

    public void isUsernameBanned() {
        ProgressDialog progressDialog = new ProgressDialog(SaveUserActivity.this);
        progressDialog.setMessage("Kayıt Ediliyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String newUsername = editTextUsername.getText().toString().trim();
        bannedWordManager = new BannedWordManager(newUsername);

        isUsernameBanned = bannedWordManager.isUsernameBanned();

        if(isUsernameBanned) {
            progressDialog.dismiss();
            AlertManager.showErrorAlert(CL_SAVE_USER, "Kullanıcı Adında Sakıncalı Kelimeler Tespit Edildi");
        } else {
            //New User - Username & Password
            String newUserUsername = editTextUsername.getText().toString().trim();
            String newUserPassword = editTextPassword.getText().toString().trim();

            int usernameLength = newUserUsername.length();
            int passwordLength = newUserPassword.length();

            if(usernameLength < 4 || passwordLength < 4) {
                progressDialog.dismiss();
                AlertManager.showErrorAlert(CL_SAVE_USER, "Kullanıcı Adı ve Şifre en az 4 karakterden oluşur !");
            } else {
                //INSERT USER
                databaseRepository.insertUser(newUserUsername, newUserPassword, progressDialog, editorLogInInfo);
            }
        }
    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(SaveUserActivity.this, CL_SAVE_USER);
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_SAVE_USER.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(SaveUserActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_SAVE_USER = findViewById(R.id.CL_SAVE_USER);
        editTextUsername = findViewById(R.id.editTextUsername_SU);
        editTextPassword = findViewById(R.id.editTextPassword_DeleteAccount);
        buttonInsertUser = findViewById(R.id.buttonInsertUser);
        checkBoxUserAgreement = findViewById(R.id.checkBoxUserAgreement);
        adviewBanner_SAVE_USER = findViewById(R.id.adviewBanner_SAVE_USER);
    }

}