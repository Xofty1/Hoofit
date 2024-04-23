package com.example.hoofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoofit.R;
import com.example.hoofit.databinding.FragmentTrailBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class TrailFragment extends Fragment {
FragmentTrailBinding binding;
    String file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrailBinding.inflate(getLayoutInflater());
        new Thread(new Runnable() {
            public void run() {
                try {
                    file = download("https://firebasestorage.googleapis.com/v0/b/hoofit-5c8bd.appspot.com/o/way1.json?alt=media&token=c7ceeece-b1b1-43c7-9816-9bbaefeef014");
                    Log.d("GGGG", file.toString());
                } catch (IOException e) {
                    file = "error";
                }
            }
        }).start();
        return binding.getRoot();
    }
    private String download(String urlPath) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}