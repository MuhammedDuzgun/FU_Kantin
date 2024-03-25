package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gundemgaming.fukantin.dbrepository.DatabaseRepository;
import com.gundemgaming.fukantin.fragments.FragmentPosts_UP;
import com.gundemgaming.fukantin.fragments.FragmentReplies_UP;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    //ActivityElements
    private ConstraintLayout CL_USER_PROFILE;
    private TabLayout tabLayout_UP;
    private ViewPager2 viewpager_UP;
    private TextView textViewUsername_UP;
    private TextView textViewDepartment_UP;
    private TextView textViewInstagramAddress_UP;
    private TextView textViewBiography_UP;
    private List<Fragment> fragmentList;
    private List<String> fragmentTitleList;
    private AdView adviewBanner_USER_PROFILE;

    //Database Repository
    private DatabaseRepository databaseRepository;

    //Intent Data
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //Intent Data
        getIntentData();

        //Database Repository
        getRepository();

        //Fragments
        fragments();

        //Get UserProfile
        databaseRepository.getUserProfile(userId, textViewUsername_UP, textViewDepartment_UP, textViewInstagramAddress_UP, textViewBiography_UP);

    }

    public void getIntentData() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId" ,0);
    }

    public void fragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentPosts_UP());
        fragmentList.add(new FragmentReplies_UP());

        fragmentTitleList = new ArrayList<>();
        fragmentTitleList.add("Postlar");
        fragmentTitleList.add("Cevaplar");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(UserProfileActivity.this);
        viewpager_UP.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout_UP, viewpager_UP,
                (tab, position) -> tab.setText(fragmentTitleList.get(position))).attach();

    }

    public void getRepository() {
        databaseRepository = new DatabaseRepository(UserProfileActivity.this, CL_USER_PROFILE);
    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_USER_PROFILE.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(UserProfileActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_USER_PROFILE = findViewById(R.id.CL_USER_PROFILE);
        tabLayout_UP = findViewById(R.id.tabLayout_UP);
        viewpager_UP = findViewById(R.id.viewpager_UP);
        textViewUsername_UP = findViewById(R.id.textViewUsername_UP);
        textViewDepartment_UP = findViewById(R.id.textViewDepartment_UP);
        textViewInstagramAddress_UP = findViewById(R.id.textViewInstagramAddress_UP);
        textViewBiography_UP = findViewById(R.id.textViewBiography_UP);
        adviewBanner_USER_PROFILE = findViewById(R.id.adviewBanner_USER_PROFILE);
    }

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

}