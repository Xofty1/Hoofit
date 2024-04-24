package com.example.hoofit.dataHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.hoofit.data.Trail;
import com.example.hoofit.data.TrailData;
import com.example.hoofit.ui.TrailFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class DataHandler extends AsyncTask<Void, Void, String> {
    Context context;
    private DataInterface dataInterface;

    String Url = "https://firebasestorage.googleapis.com/v0/b/hoofit-5c8bd.appspot.com/o/way1.json?alt=media&token=36882818-c761-4caa-8b0c-dd788e27bbcf";
    public static TrailData importFromJSON(String resource) {
        try {
            return new Gson().fromJson(resource, TrailData.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DataHandler(Context context, DataInterface dataInterface) {
        this.context = context;
        this.dataInterface = dataInterface;
    }

    @Override
    protected String doInBackground(Void... voids) {
        InputStream inputStream = null;
        HttpsURLConnection httpsURLConnection = null;
        String info = null;
        try {
            URL url = new URL(Url);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                inputStream = httpsURLConnection.getInputStream();
                info = convertInputStreamToString(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    @Override
    protected void onPostExecute(String resource) {
        if (resource != null) {
            TrailData trailData = importFromJSON(resource);
            dataInterface.onTrailDataReceived(trailData);
        } else {
            Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_LONG).show();
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}
