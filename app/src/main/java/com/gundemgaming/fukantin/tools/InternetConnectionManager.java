package com.gundemgaming.fukantin.tools;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetConnectionManager {

    public interface InternetCheckListener {
        void onInternetCheckResult(boolean isConnected);
    }

    public static void checkInternetConnection(InternetCheckListener listener) {
        new InternetCheckTask(listener).execute();
    }

    private static class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {

        private InternetCheckListener listener;

        public InternetCheckTask(InternetCheckListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Google'ın bir URL'sine bağlanma denemesi
                URL url = new URL("https://www.google.com");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                return (responseCode == HttpURLConnection.HTTP_OK);

            } catch (IOException e) {
                e.printStackTrace();
                return false;

            }
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (listener != null) {
                listener.onInternetCheckResult(isConnected);
            }
        }
    }

}
