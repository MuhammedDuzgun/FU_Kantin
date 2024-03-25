package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

public class SharePostActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_SHARE_ENTRY;
    private TextView textViewCategory;
    private EditText editTextPostTitle;
    private EditText editTextPost;
    private Button buttonChooseCategory;
    private Button buttonShareEntry;
    private AdView adviewBanner_SHARE_POST;

    //Activity Datas
    private String title;
    private String post;
    private int categoryId;
    private int userId;

    //Database Repossitory
    private DatabaseRepository databaseRepository;

    //Popup Menu
    private PopupMenu popupMenuCategory;

    //SharedPreferences
    private SharedPreferences sharedPreferencesLogInInfo;

    //SharedPreferences Editors
    private SharedPreferences.Editor editorLogInInfo;

    //BannedWords
    private BannedWordManager bannedWordManager;
    private boolean isTextBanned;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //SharedPrefrences
        getSharedPrefrences();

        //Database Repository
        getRepository();

        //Button - Share Entry
        buttonShareEntrySetOnClickListener();

        //Button - Category
        buttonChooseCategorySetOnClickListener();

    }

    public void getSharedPrefrences() {
        //SharedPreferences
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        editorLogInInfo = sharedPreferencesLogInInfo.edit();

        userId = sharedPreferencesLogInInfo.getInt("userId", 0);
    }

    public void buttonChooseCategorySetOnClickListener() {
        buttonChooseCategory.setOnClickListener(view->{
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_SHARE_ENTRY, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            popupMenuCategory = new PopupMenu(SharePostActivity.this, buttonChooseCategory);
            MenuInflater menuInflater = popupMenuCategory.getMenuInflater();
            menuInflater.inflate(R.menu.menu_category, popupMenuCategory.getMenu());
            popupMenuCategory.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    textViewCategory.setText(item.getTitle());

                    if(item.getTitle().equals("Haber")) {
                        categoryId = 1;
                    } else if (item.getTitle().equals("Sinema")) {
                        categoryId = 2;
                    } else if (item.getTitle().equals("Bilim")) {
                        categoryId = 3;
                    } else if (item.getTitle().equals("Eğitim")) {
                        categoryId = 4;
                    } else if (item.getTitle().equals("Müzik")) {
                        categoryId = 5;
                    } else if (item.getTitle().equals("Edebiyat")) {
                        categoryId = 6;
                    } else if (item.getTitle().equals("Ekonomi")) {
                        categoryId = 7;
                    } else if (item.getTitle().equals("Tarih")) {
                        categoryId = 8;
                    } else if (item.getTitle().equals("Yeme-İçme")) {
                        categoryId = 9;
                    } else if (item.getTitle().equals("İlişkiler")) {
                        categoryId = 10;
                    } else if (item.getTitle().equals("Teknoloji")) {
                        categoryId = 11;
                    } else if (item.getTitle().equals("Siyaset")) {
                        categoryId = 12;
                    } else if (item.getTitle().equals("Sanat")) {
                        categoryId = 13;
                    } else if (item.getTitle().equals("Moda")) {
                        categoryId = 14;
                    } else if (item.getTitle().equals("Otomativ")) {
                        categoryId = 15;
                    } else if (item.getTitle().equals("Magazin")) {
                        categoryId = 16;
                    } else if (item.getTitle().equals("Spor")) {
                        categoryId = 17;
                    } else if (item.getTitle().equals("Sağlık")) {
                        categoryId = 18;
                    } else if (item.getTitle().equals("Oyun")) {
                        categoryId = 19;
                    } else if (item.getTitle().equals("Anket")) {
                        categoryId = 20;
                    } else if (item.getTitle().equals("Programlama")) {
                        categoryId = 21;
                    } else if (item.getTitle().equals("Televizyon")) {
                        categoryId = 22;
                    } else if (item.getTitle().equals("Seyehat")) {
                        categoryId = 23;
                    } else if (item.getTitle().equals("Havacılık")) {
                        categoryId = 24;
                    } else if (item.getTitle().equals("Fırat Üniversitesi")) {
                        categoryId = 25;
                    } else if (item.getTitle().equals("Duyuru")) {
                        categoryId = 26;
                    } else if (item.getTitle().equals("Diğer")) {
                        categoryId = 27;
                    }

                    return false;
                }
            });
            popupMenuCategory.show();
        });
    }

    public void buttonShareEntrySetOnClickListener() {
        buttonShareEntry.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_SHARE_ENTRY, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            boolean isValuesReady = postValuesChecker();
            if(isValuesReady) {
                if(categoryId == 0) {
                    AlertManager.showErrorAlert(CL_SHARE_ENTRY, "Kategori Seçmediniz.");
                } else {
                    title = editTextPostTitle.getText().toString().trim();
                    post = editTextPost.getText().toString().trim();

                    if(isTextContainsBannedWord(title) || isTextContainsBannedWord(post)) {
                        AlertManager.showErrorAlert(CL_SHARE_ENTRY, "Sakıncalı Kelimeler Tespit Edildi.");
                    } else {
                        //INSERT POST
                        databaseRepository.insertPost(userId, title, post, categoryId);
                    }
                }
            } else {
                AlertManager.showErrorAlert(CL_SHARE_ENTRY, "Başlık Veya Post Alanı Boş Bırakılamaz.");
            }
        });

    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(SharePostActivity.this, CL_SHARE_ENTRY);
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

    public boolean postValuesChecker() {
        title = editTextPostTitle.getText().toString();
        post = editTextPost.getText().toString();
        if(title.equals("") || post.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_SHARE_POST.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(SharePostActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_SHARE_ENTRY = findViewById(R.id.CL_SHARE_POST);
        textViewCategory = findViewById(R.id.textViewCategory);
        editTextPostTitle = findViewById(R.id.editTextPostTitle);
        editTextPost = findViewById(R.id.editTextPost);
        buttonChooseCategory = findViewById(R.id.buttonChooseCategory);
        buttonShareEntry = findViewById(R.id.buttonSharePost);
        adviewBanner_SHARE_POST = findViewById(R.id.adviewBanner_SHARE_POST);
    }

}