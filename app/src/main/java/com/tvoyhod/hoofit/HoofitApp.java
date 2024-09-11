package com.tvoyhod.hoofit;

import android.app.Application;

import com.tvoyhod.hoofit.data.Interesting;
import com.tvoyhod.hoofit.data.ReserveData;
import com.tvoyhod.hoofit.data.Trail;
import com.tvoyhod.hoofit.data.User;
import com.yandex.mapkit.MapKitFactory;

import java.util.ArrayList;
import java.util.List;

public class HoofitApp extends Application {
    public static User user;
    public static ReserveData reserves = null;
    public static List<Trail> allTrails;
    public static List<Interesting> interestings =new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("a075c213-cf50-41fb-994d-60f735f08f1b");
    }
}
