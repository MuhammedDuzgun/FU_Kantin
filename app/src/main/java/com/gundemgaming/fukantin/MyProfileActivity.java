package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.fragments.FragmentPosts_MP;
import com.gundemgaming.fukantin.fragments.FragmentReplies_MP;
import com.gundemgaming.fukantin.tools.AlertManager;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    //ActivityElements
    private ConstraintLayout CL_MY_PROFILE;
    private TabLayout tabLayout_MP;
    private ViewPager2 viewpager_MP;
    private TextView textViewUsername_MP;
    private TextView textViewDepartment_MP;
    private TextView textViewInstagramAddress_MP;
    private TextView textViewBiography_MP;
    private ImageButton imageButtonEditProfile;
    private List<Fragment> fragmentList;
    private List<String> fragmentTitleList;
    private AdView adviewBanner_MY_PROFILE;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //SharedPrefrences Data
    private int userId;

    //SharedPreferences
    public SharedPreferences sharedPreferencesLogInInfo;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //Get SharedPreferences Data
        getSharedPreferencesDatas();

        //Database Repository
        getRepository();

        //Fragments
        fragments();

        //Get MyProfile
        databaseRepository.getMyProfile(userId, textViewUsername_MP, textViewDepartment_MP, textViewInstagramAddress_MP, textViewBiography_MP);

        //Button - EditProfile
        imageButtonEditProfileSetOnClickListener();

    }

    public void getSharedPreferencesDatas() {
        sharedPreferencesLogInInfo = getSharedPreferences("LogInInfo", Context.MODE_PRIVATE);
        userId = sharedPreferencesLogInInfo.getInt("userId", 0);
    }

    public void fragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentPosts_MP());
        fragmentList.add(new FragmentReplies_MP());

        fragmentTitleList = new ArrayList<>();
        fragmentTitleList.add("Postlar");
        fragmentTitleList.add("Cevaplar");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(MyProfileActivity.this);
        viewpager_MP.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout_MP, viewpager_MP,
                (tab, position) -> tab.setText(fragmentTitleList.get(position))).attach();

    }
    public void imageButtonEditProfileSetOnClickListener() {
        imageButtonEditProfile.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                AlertManager.showErrorAlert(CL_MY_PROFILE, "Sakin Ol Şampiyon !");
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("bio", textViewBiography_MP.getText().toString());
            intent.putExtra("instagram", textViewInstagramAddress_MP.getText().toString());
            intent.putExtra("department", textViewDepartment_MP.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(MyProfileActivity.this, CL_MY_PROFILE);
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_MY_PROFILE.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(MyProfileActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_MY_PROFILE = findViewById(R.id.CL_MY_PROFILE);
        tabLayout_MP = findViewById(R.id.tablayout_MP);
        viewpager_MP = findViewById(R.id.viewpager_MP);
        textViewUsername_MP = findViewById(R.id.textViewUsername_MP);
        textViewDepartment_MP = findViewById(R.id.textViewDepartment_MP);
        textViewInstagramAddress_MP = findViewById(R.id.textViewInstagramAddress_MP);
        textViewBiography_MP = findViewById(R.id.textViewBiography_MP);
        imageButtonEditProfile = findViewById(R.id.imageButtonEditProfile);
        adviewBanner_MY_PROFILE = findViewById(R.id.adviewBanner_MY_PROFILE);
    }

    //ViewPagerAdapter
    private class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //My Profile
        databaseRepository.getMyProfile(userId, textViewUsername_MP, textViewDepartment_MP, textViewInstagramAddress_MP, textViewBiography_MP);
    }

}