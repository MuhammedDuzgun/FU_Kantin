package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
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
import com.gundemgaming.fukantin.models.User;
import com.gundemgaming.fukantin.tools.AlertManager;

public class LoginActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_LOGIN;
    private Button buttonLogin;
    private Button buttonSaveUser;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private AdView adviewBanner_LOGIN;

    //Activity Datas
    private String enteredUsername;
    private String enteredPassword;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    //Shared Preferences Editor
    public SharedPreferences.Editor editorLogInInfo;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //SharedPrefrences
        getSharedPrefrences();

        //Database Repository
        getRepository();

        //Button - SaveUser
        buttonSaveUserSetOnClickListener();

        //Button - Login
        buttonLoginSetOnClickListener();

    }

    //SharedPrefrences
    public void getSharedPrefrences() {
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo",MODE_PRIVATE);
        editorLogInInfo = sharedPreferencesLogInInfo.edit();
    }

    public void buttonSaveUserSetOnClickListener() {
        buttonSaveUser.setOnClickListener(view-> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_LOGIN, "Sakin Ol Şampiyon !");
                return;
            }

            mLastClickTime = SystemClock.elapsedRealtime();

            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            Intent intent = new Intent(LoginActivity.this, SaveUserActivity.class);
            intent.putExtra("username_from_loginpage", username);
            intent.putExtra("password_from_loginpage", password);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        });
    }

    public void buttonLoginSetOnClickListener() {
        buttonLogin.setOnClickListener(view-> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_LOGIN, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            enteredUsername = editTextUsername.getText().toString().trim();
            enteredPassword = editTextPassword.getText().toString().trim();

            User enteredUser = new User(enteredUsername, enteredPassword);

            int enteredUsernameLength = enteredUsername.length();
            int enteredPasswordLength = enteredPassword.length();

            if(enteredUsernameLength < 4 || enteredPasswordLength < 4) {
                AlertManager.showErrorAlert(CL_LOGIN, "Kullanıcı Adı ve Şifre en az 4 karakterden oluşur.");
            } else {
                //LOGIN USER
                databaseRepository.loginUser(enteredUser, editorLogInInfo);
            }
        });
    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(LoginActivity.this, CL_LOGIN);
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_LOGIN.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(LoginActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_LOGIN = findViewById(R.id.CL_LOGIN);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSaveUser = findViewById(R.id.buttonSaveUser);
        editTextUsername = findViewById(R.id.editTextUsername_SU);
        editTextPassword = findViewById(R.id.editTextPassword_DeleteAccount);
        adviewBanner_LOGIN = findViewById(R.id.adviewBanner_LOGIN);
    }

}