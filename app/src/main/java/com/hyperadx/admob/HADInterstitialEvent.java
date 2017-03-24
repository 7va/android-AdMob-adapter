package com.hyperadx.admob;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.hyperadx.hypernetwork.ads.Ad;
import com.hyperadx.hypernetwork.ads.AdError;
import com.hyperadx.hypernetwork.ads.InterstitialAd;
import com.hyperadx.hypernetwork.ads.InterstitialAdListener;


public class HADInterstitialEvent implements CustomEventInterstitial {

    private InterstitialAd interstitialAd;


    @Override
    public void onDestroy() {

    }

    /**
     * The app is being paused. This call will only be forwarded to the adapter if the developer
     * notifies mediation that the app is being paused.
     */
    @Override
    public void onPause() {
        // The sample ad network doesn't have an onPause method, so it does nothing.
    }

    /**
     * The app is being resumed. This call will only be forwarded to the adapter if the developer
     * notifies mediation that the app is being resumed.
     */
    @Override
    public void onResume() {
        // The sample ad network doesn't have an onResume method, so it does nothing.
    }


    @Override
    public void requestInterstitialAd(final Context context,
                                      final CustomEventInterstitialListener listener,
                                      String serverParameter,
                                      MediationAdRequest mediationAdRequest,
                                      Bundle customEventExtras) {

        /*
         * In this method, you should:
         *
         * 1. Create your interstitial ad.
         * 2. Set your ad network's listener.
         * 3. Make an ad request.
         *
         * When setting your ad network's listener, don't forget to send the following callbacks:
         *
         * listener.onAdLoaded(this);
         * listener.onAdFailedToLoad(this, AdRequest.ERROR_CODE_*);
         * listener.onAdOpened(this);
         * listener.onAdLeftApplication(this);
         * listener.onAdClosed(this);
         */

        interstitialAd = new InterstitialAd(context, serverParameter); //Interstitial AD constructor
        interstitialAd.setAdListener(new InterstitialAdListener() { // Set Listener
            @Override
            public void onAdLoaded(Ad ad) { // Called when AD is Loaded

                if (ad != interstitialAd) {
                    return; // Race condition, load() called again before last ad was displayed
                }
                Log.i("HAD AD", "Interstitial Ad was loaded");
                listener.onAdLoaded();
            }

            @Override
            public void onError(Ad Ad, AdError error) { // Called when load is fail
                Log.e("HAD AD", "Interstitial Ad failed to load with error: " + error.getErrorMessage());

                int AdMobErrorCode = AdRequest.ERROR_CODE_INTERNAL_ERROR;

                switch (error.getErrorCode()) {
                    case AdError.INTERNAL_ERROR_CODE:
                        AdMobErrorCode = AdRequest.ERROR_CODE_INTERNAL_ERROR;
                        break;


                    case AdError.NETWORK_ERROR_CODE:
                        AdMobErrorCode = AdRequest.ERROR_CODE_NETWORK_ERROR;
                        break;

                    case AdError.NO_FILL_ERROR_CODE:
                        AdMobErrorCode = AdRequest.ERROR_CODE_NO_FILL;
                        break;

                    case AdError.SERVER_ERROR_CODE:
                        AdMobErrorCode = AdRequest.ERROR_CODE_INVALID_REQUEST;
                        break;

                    default:
                        AdMobErrorCode = AdRequest.ERROR_CODE_INTERNAL_ERROR;
                        break;
                }


                listener.onAdFailedToLoad(AdMobErrorCode);
            }

            @Override
            public void onInterstitialDisplayed(Ad Ad) { // Called when Ad was impressed

                Log.i("HAD AD", "Interstitial Ad was displayed");
                listener.onAdOpened();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) { // Called when Ad was dismissed by user
                Log.i("HAD AD", "Interstitial Ad was dismissed");
                listener.onAdClosed();
            }

            @Override
            public void onAdClicked(Ad ad) { // Called when user click on AD
                Log.i("HAD AD", "Interstitial Ad was clicked");
                listener.onAdClicked();
            }

            @Override
            public void onVideoCompleted(Ad ad) {
                Log.i("HAD AD", "Rewarded Video Completed. Now you may gift some profit to user!");
            }


        });

        interstitialAd.loadAd();
    }

    @Override
    public void showInterstitial() {

        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            // Ad not ready to show.
            Log.e("HAD AD", "The Interstitial AD not ready yet. Try again!");
        } else {
            // Ad was loaded, show it!
            interstitialAd.show();

        }

    }

}
