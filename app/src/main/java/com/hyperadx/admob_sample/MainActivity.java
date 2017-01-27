package com.hyperadx.admob_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;


public class MainActivity extends AppCompatActivity {

    private com.google.android.gms.ads.InterstitialAd mAdapterInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {


        mAdapterInterstitial = new com.google.android.gms.ads.InterstitialAd(this);
        mAdapterInterstitial.setAdUnitId(
                "ca-app-pub-6172762133617463/5529648238"
        );
        mAdapterInterstitial.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(MainActivity.this,
                        "Error loading adapter interstitial, code " + errorCode,
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdLoaded() {
                Toast.makeText(MainActivity.this,
                        "onAdLoaded()",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdOpened() {


            }

            @Override
            public void onAdClosed() {
                mAdapterInterstitial.loadAd(new AdRequest.Builder().build());
            }
        });
        mAdapterInterstitial.loadAd(new AdRequest.Builder().build());


    }

    public void showInterstitial(View view) {
        if (mAdapterInterstitial.isLoaded())
            mAdapterInterstitial.show();
        else
            Toast.makeText(this, "The Interstitial AD not ready yet. Try again!", Toast.LENGTH_LONG).show();
    }
}
