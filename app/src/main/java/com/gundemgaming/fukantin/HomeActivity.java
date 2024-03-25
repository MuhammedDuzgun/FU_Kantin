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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.gundemgaming.fukantin.adapters.PostAdapter;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.models.Post;
import com.gundemgaming.fukantin.tools.AlertManager;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_HOME;
    private SwipeRefreshLayout swipeRefreshHome;
    private RecyclerView recyclerView;
    private TextView textViewFilter;
    private TextView textViewZeroPostInfo;
    private ImageButton imageButtonFilter;
    private ImageButton imageButtonCleanFilter;
    private ImageButton imageButtonMyProfile;
    private ImageButton imageButtonSharePost;
    private ImageButton imageButtonAboutApp;

    private AdView adviewBanner_HOME;

    //Activity Data
    private int categoryId;

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    //Repository
    private DatabaseRepository databaseRepository;

    //Popup Menu
    private PopupMenu popupMenuCategory;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById();
        recyclerViewSettings();

        //Adversitements
        adManager();
        showBannerAd();

        //Repository
        getRepository();

        //Posts
        databaseRepository.getPosts(recyclerView);

        //Button - ShareEntry
        imageButtonSharePostSetOnClickListener();

        //Button - Profile
        imageButtonMyProfileSetOnClickListener();

        //Button - About App
        imageButtonAboutAppSetOnClickListener();

        //Button - Filter Posts
        imageButtonFilterSetOnClickListener();

        //Button - Clear Filter
        imageButtonClearFilterSetOnClickListener();

        //Refresh Activity
        refreshActivity();

    }

    public void imageButtonFilterSetOnClickListener() {
        imageButtonFilter.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_HOME, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            popupMenuCategory = new PopupMenu(HomeActivity.this, imageButtonFilter);
            MenuInflater menuInflater = popupMenuCategory.getMenuInflater();
            menuInflater.inflate(R.menu.menu_category, popupMenuCategory.getMenu());

            popupMenuCategory.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    textViewFilter.setText(item.getTitle());

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

                    //GET POSTS WITH CATEGORY
                    databaseRepository.getPostsWithCategory(recyclerView, textViewZeroPostInfo, categoryId);

                    return false;
                }
            });
            popupMenuCategory.show();

        });
    }

    public void imageButtonClearFilterSetOnClickListener() {
        imageButtonCleanFilter.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_HOME, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            recyclerView.setVisibility(View.VISIBLE);
            textViewZeroPostInfo.setVisibility(View.INVISIBLE);
            databaseRepository.getPosts(recyclerView);
            textViewFilter.setText("Güncel Postlar");
            switch (textViewZeroPostInfo.getVisibility()) {
                case 1:
                    textViewZeroPostInfo.setVisibility(View.INVISIBLE);
            }

        });
    }

    public void imageButtonMyProfileSetOnClickListener() {
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        boolean isUserEntered = sharedPreferencesLogInInfo.getBoolean("isUserEntered", false);

        imageButtonMyProfile.setOnClickListener(view -> {
            if(isUserEntered) {
                Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void imageButtonSharePostSetOnClickListener() {
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        boolean isUserEntered = sharedPreferencesLogInInfo.getBoolean("isUserEntered", false);

        imageButtonSharePost.setOnClickListener(view -> {
            if(isUserEntered) {
                Intent intent = new Intent(HomeActivity.this, SharePostActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void imageButtonAboutAppSetOnClickListener() {
        //Preventing Double Click On A Button
        if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        imageButtonAboutApp.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, AboutAppActivity.class));
        });
    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(HomeActivity.this, CL_HOME);
    }

    public void refreshActivity() {
        swipeRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                textViewFilter.setText("Güncel Postlar");
                textViewZeroPostInfo.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                databaseRepository.getPosts(recyclerView);
                swipeRefreshHome.setRefreshing(false);

            }
        });
    }

    public void recyclerViewSettings() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_HOME.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(HomeActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }


    public void findViewById() {
        CL_HOME = findViewById(R.id.CL_HOME);
        swipeRefreshHome = findViewById(R.id.swipeRefreshHome);
        recyclerView = findViewById(R.id.recyclerviewPost_HA);
        textViewFilter = findViewById(R.id.textViewFilter);
        textViewZeroPostInfo = findViewById(R.id.textViewZeroPostInfo);
        imageButtonFilter = findViewById(R.id.imageButtonFilter);
        imageButtonCleanFilter = findViewById(R.id.imageButtonCleanFilter);
        imageButtonMyProfile = findViewById(R.id.imageButtonMyProfile);
        imageButtonSharePost = findViewById(R.id.imageButtonSharePost);
        imageButtonAboutApp = findViewById(R.id.imageButtonAboutApp);
        adviewBanner_HOME = findViewById(R.id.adviewBanner_HOME);
    }

}