package com.gundemgaming.fukantin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gundemgaming.fukantin.tools.InternetConnectionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check Users Internet Connection
        checkUsersInternetConnection();

    }

    public void checkUsersInternetConnection() {
        InternetConnectionManager.checkInternetConnection(new InternetConnectionManager.InternetCheckListener() {
            @Override
            public void onInternetCheckResult(boolean isConnected) {
                if (!isConnected) {
                    showExitAlertDialog();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                }
            }
        });
    }

    //Exit AlertDialog
    public void showExitAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("İnternet Bağlantınız Yok");

        alertDialog.setPositiveButton("Tekrar Dene", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkUsersInternetConnection();
            }
        });

        alertDialog.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                System.exit(0);
            }
        });

        alertDialog.create().show();
    }

}