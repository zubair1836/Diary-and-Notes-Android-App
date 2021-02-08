package com.diary.diaryandnotes;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Ads_Initializer extends Application {
    private long load_time‬ = 1200;
    Context c;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("TAG", "onCreate:ads initializer ");

        c = getApplicationContext();

        AudienceNetworkAds.initialize(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });






    }
}
