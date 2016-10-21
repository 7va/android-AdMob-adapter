
![HyperADx Logo](http://d2n7xvwjxl8766.cloudfront.net/assets/site/logo-e04518160888e1f8b3795f0ce01e1909.png) 
 
![AdMob Logo](https://www.google.com/images/logos/admob-logo.svg)

##Contents

* [Introduction](#introduction)
* [Interstitial](#interstitial)


# Introduction

* [Download](https://github.com/hyperads/android-AdMob-adapter/releases) and extract the AdMob adapter if needed.

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

* Fill `Class Name` field with a `com.hyperadx.admob.HADInterstitialEvent` string. And a `Parameter` with your HyperAdx statement string.

<img src="/docs/images/6.png" title="sample"/>

* Setup eCPM for new network 
<img src="/docs/images/7.png" title="sample"/>

Now you can setting up your android project.

* Put HyperAdx-SDK and AdMob-adapter in 'libs' folder.
* Add those lines in your `build.gradle` file:

**NOTE** - Admob interstitial may not work in emulator. Use real devices even for tests!

```groove
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:23.4.0'
    compile 'com.google.android.gms:play-services-ads:9.0.2'
    compile 'com.google.android.gms:play-services-base:9.0.2'
}
```
Just create AdMob interstitial Ad as usually:

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
