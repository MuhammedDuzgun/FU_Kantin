package com.gundemgaming.fukantin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.gundemgaming.fukantin.tools.AlertManager;

public class AboutAppActivity extends AppCompatActivity {

    //Activity Elements
    private ConstraintLayout CL_ABOUT_GAME;
    private TextView textViewEmail;
    private TextView textViewPatreonLink;
    private Button buttonShareApp;
    private ImageView imageViewCopyPaparaIdToClipboard;
    private AdView adviewBanner_ABOUT_APP;

    //Preventing Double Click On A Button
    private  long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        findViewById();

        //Adversitements
        adManager();
        showBannerAd();

        //TextView - Email
        textViewEmailSetOnClickListener();

        //Button - Share App
        buttonShareAppSetOnClickListener();

        //ImageView - Copy PaparaId
        imageViewCopyPaparaIdToClipboardSetOnClickListener();

    }

    public void textViewEmailSetOnClickListener() {
        String emailText = "<a href=\"mailto:gundemgaming@gmail.com\">Mail Yolla</a>";
        textViewEmail.setText(Html.fromHtml(emailText));
        textViewEmail.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void buttonShareAppSetOnClickListener() {
        buttonShareApp.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String body = "Uygulamayı Paylaş";
            String sub = "https://play.google.com/store/apps/details?id=com.gundemgaming.vergimaster&gl=TR";
            intent.putExtra(Intent.EXTRA_TEXT, body);
            intent.putExtra(Intent.EXTRA_TEXT, sub);
            startActivity(Intent.createChooser(intent, "Share Using"));

        });
    }

    public void imageViewCopyPaparaIdToClipboardSetOnClickListener() {

        imageViewCopyPaparaIdToClipboard.setOnClickListener(view -> {
            //Preventing Double Click On A Button
            if (SystemClock.elapsedRealtime() - mLastClickTime < 750){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("paparaId", "1412537112");
            clipboard.setPrimaryClip(clip);
            AlertManager.showSuccessfulAlert(CL_ABOUT_GAME, "Papara No Kopyalandı");
        });

    }

    public void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adviewBanner_ABOUT_APP.loadAd(adRequest);
    }

    public void adManager() {

        MobileAds.initialize(AboutAppActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

    }

    public void findViewById() {
        CL_ABOUT_GAME = findViewById(R.id.CL_ABOUT_APP);
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonShareApp = findViewById(R.id.buttonShareApp);
        imageViewCopyPaparaIdToClipboard = findViewById(R.id.imageViewCopyPaparaIdToClipboard);
        adviewBanner_ABOUT_APP = findViewById(R.id.adviewBanner_ABOUT_APP);
    }

}