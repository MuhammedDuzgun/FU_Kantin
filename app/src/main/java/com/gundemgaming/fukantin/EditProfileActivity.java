package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.tools.AlertManager;
import com.gundemgaming.fukantin.tools.BannedWordManager;

public class EditProfileActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_EDIT_PROFILE;
    private TextView textViewDepartment_EP;
    private EditText editTextBio;
    private EditText editTextInstagram;
    private ImageButton imageButtonSaveBio;
    private ImageButton imageButtonSaveInstagram;
    private Button buttonChooseDepartment;
    private ImageButton imageButtonSaveDepartment;
    private Button buttonDeleteAccount;
    private AdView adviewBanner_EDIT_PROFILE;

    //Activity Datas
    private String bio;
    private String instagramAddress;
    private String department = null;

    //Databaase Repository
    private DatabaseRepository databaseRepository;

    //Popup Menu
    private PopupMenu popupMenuDepartment;

    //SharedPreferences Data
    private int userId;
    private String password;

    //SharedPreferences
    private SharedPreferences sharedPreferencesLogInInfo;

    //BannedWordManager;
    private BannedWordManager bannedWordManager;
    private boolean isUsernameBanned;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //Intent Data
        getIntentDatas();

        //SharedPreferences Data
        getSharedPreferencesData();

        //Database Repository
        getRepository();

        //Button - Update Bio
        imageButtonSaveBioSetOnClickListener();

        //Button - Update InstagramAddress
        imageButtonSaveInstagramSetOnClickListener();

        //Button - Choose Department
        buttonChooseDepartmentSetOnClickListener();

        //Button - UpdateDepartment
        imageButtonSaveDepartmentSetOnClickListener();

        //Button - DeleteAccount
        buttonDeleteAccountSetOnClickListener();

    }

    public void getSharedPreferencesData() {
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo",MODE_PRIVATE);
        userId = sharedPreferencesLogInInfo.getInt("userId", 0);
        password = sharedPreferencesLogInInfo.getString("password", "null");
    }

    public void getIntentDatas() {
        Intent intent = getIntent();

        editTextBio.setText(intent.getStringExtra("bio"));
        editTextInstagram.setText(intent.getStringExtra("instagram"));
        textViewDepartment_EP.setText(intent.getStringExtra("department"));

    }

    public void imageButtonSaveBioSetOnClickListener() {
        imageButtonSaveBio.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            bio = editTextBio.getText().toString().trim();

            if(!isTextContainsBannedWord(bio)) {
                //UPDATE BIO
                databaseRepository.updateBio(userId, bio);
            } else {
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Bio'da Sakıncalı Kelimeler Tespit Edildi");
            }

        });
    }

    public void imageButtonSaveInstagramSetOnClickListener() {
        imageButtonSaveInstagram.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            instagramAddress = editTextInstagram.getText().toString().trim();
            if(!isTextContainsBannedWord(instagramAddress)) {
                //UPDATE INSTAGRAM ADDRESS
                databaseRepository.updateInstagramAddress(userId, instagramAddress);
            } else {
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "İnstagram Adresinizde Sakıncalı Kelimeler Tespit Edildi");
            }

        });
    }

    public void buttonChooseDepartmentSetOnClickListener() {
        buttonChooseDepartment.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            popupMenuDepartment = new PopupMenu(EditProfileActivity.this, buttonChooseDepartment);
            MenuInflater menuInflater = popupMenuDepartment.getMenuInflater();
            menuInflater.inflate(R.menu.menu_department, popupMenuDepartment.getMenu());

            popupMenuDepartment.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    textViewDepartment_EP.setText(item.getTitle());
                    department = item.getTitle().toString();
                    return false;
                }
            });

            popupMenuDepartment.show();

        });
    }

    public void imageButtonSaveDepartmentSetOnClickListener() {
        imageButtonSaveDepartment.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            if(department == null) {
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Fakülte Seçilmemiş");
            } else {
                //UPDATE DEPARTMENT
                databaseRepository.updateDepartment(userId, department);
            }
        });
    }

    public void buttonDeleteAccountSetOnClickListener() {
        buttonDeleteAccount.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            final View customAlertDialog = getLayoutInflater().inflate(R.layout.alertdialog_delete_account, null, false);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivity.this);
            alertDialog.setView(customAlertDialog);

            alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText editTextPassword = customAlertDialog.findViewById(R.id.editTextPassword_DeleteAccount);
                    String passwordFromAlertDialog = editTextPassword.getText().toString().trim();
                    if(passwordFromAlertDialog.equals("")) {
                        AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Şifre Girmediniz");
                    } else if(password.equals(passwordFromAlertDialog)) {
                        //DELETE ACCOUNT
                        databaseRepository.deleteAccount(userId, sharedPreferencesLogInInfo);
                    } else {
                        AlertManager.showErrorAlert(CL_EDIT_PROFILE, "Girdiğiniz Şifre Yanlış");
                    }
                }
            });

            alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alertDialog.create().show();

        });
    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(EditProfileActivity.this, CL_EDIT_PROFILE);
    }

    public boolean isTextContainsBannedWord(String text) {
        bannedWordManager = new BannedWordManager(text);
        isUsernameBanned = bannedWordManager.isUsernameBanned();

        if(isUsernameBanned) {
            return true;
        } else {
            return false;
        }
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_EDIT_PROFILE.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(EditProfileActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_EDIT_PROFILE = findViewById(R.id.CL_EDIT_PROFILE);
        textViewDepartment_EP = findViewById(R.id.textViewDepartment_EP);
        editTextBio = findViewById(R.id.editTextBio);
        editTextInstagram = findViewById(R.id.editTextInstagram);
        imageButtonSaveBio = findViewById(R.id.imageButtonSaveBio);
        imageButtonSaveInstagram = findViewById(R.id.imageButtonSaveInstagram);
        buttonChooseDepartment = findViewById(R.id.buttonChooseDepartment);
        imageButtonSaveDepartment = findViewById(R.id.imageButtonSaveDepartment);
        buttonDeleteAccount = findViewById(R.id.buttonDeleteAccount);
        adviewBanner_EDIT_PROFILE = findViewById(R.id.adviewBanner_EDIT_PROFILE);
    }

}