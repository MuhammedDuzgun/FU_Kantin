package com.gundemgaming.fukantin.tools;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;

public class AlertManager {

    public static void showSuccessfulAlert(ViewGroup layout, String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.argb(200, 15, 255,100));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }

    public static void showErrorAlert(ViewGroup layout, String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.argb(200, 255, 70,70));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }

}
