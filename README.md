
![HyperADX Logo](http://d2n7xvwjxl8766.cloudfront.net/assets/site/logo-e04518160888e1f8b3795f0ce01e1909.png) 
 
![AdMob Logo](https://www.google.com/images/logos/admob-logo.svg)

##Contents

* [Introduction](#introduction)
* [Interstitial](#interstitial)


# Introduction

* First of all you need to add new app in AdMob console.

<img src="/docs/images/1.png" title="sample"/>

<img src="/docs/images/2.png" title="sample" width="500" height="400" />

* You will get UnitId string like 'ca-app-pub-*************/*************'.
For the next few hours you may get the AdMob errors with codes 0 or 2. Just be patient.

Then you need to add new mediation source.

<img src="/docs/images/3.png" title="sample"/>

<img src="/docs/images/4.png" title="sample"/>

<img src="/docs/images/5.png" title="sample"/>

### Interstitial

* Fill `Class Name` field with a `com.hyperadx.admob.HADInterstitialEvent` string. And a `Parameter` with your HyperADX statement string.

<img src="/docs/images/6.png" title="sample"/>

* Setup eCPM for new network 
<img src="/docs/images/7.png" title="sample"/>

Now you can setting up your android project.

* Put 'hypernetwork-release.aar' in 'libs' folder.
* Add those lines in your `build.gradle` file:

**NOTE** - Admob interstitial may not work in emulator. Use real devices even for tests!

```groove
allprojects {
    repositories {
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile(name: 'hypernetwork-release', ext: 'aar')
    compile 'com.google.android.gms:play-services-base:10.0.1'
    compile 'com.google.android.gms:play-services-ads:10.0.1'
    compile 'com.android.support:design:25.1.0'
    compile 'com.android.support:appcompat-v7:25.1.0'
}
```

Create 'HADInterstitialEvent' class in 'com.hyperadx.admob' package.
Fill it like this:

```java
package com.hyperadx.admob;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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

```


As final step create AdMob interstitial Ad as usually:

```java
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
```
