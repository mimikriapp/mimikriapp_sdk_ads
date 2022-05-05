package com.mimikri.ads.sdk.format;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.google.android.gms.ads.nativead.MediaView;
import com.mimikri.ads.sdk.R;

import static com.mimikri.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikri.ads.sdk.util.Constant.NONE;
import static com.mimikri.ads.sdk.util.Constant.UNITY;

public class NativeAdViewPager {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;

        View view;
        MediaView mediaView;


        FrameLayout applovin_native_ad;
        MaxNativeAdLoader nativeAdLoader;
        MaxAd nativeAd;

        ProgressBar progress_bar_ad;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobNativeId = "";
        private String appLovinNativeId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        public Builder(Activity activity, View view) {
            this.activity = activity;
            this.view = view;
        }

        public Builder build() {
            loadNativeAd();
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobNativeId(String adMobNativeId) {
            this.adMobNativeId = adMobNativeId;
            return this;
        }

        public Builder setAppLovinNativeId(String appLovinNativeId) {
            this.appLovinNativeId = appLovinNativeId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {


                applovin_native_ad = view.findViewById(R.id.applovin_native_ad_container);
                progress_bar_ad = view.findViewById(R.id.progress_bar_ad);
                progress_bar_ad.setVisibility(View.VISIBLE);

                switch (adNetwork) {

                    case APPLOVIN_MAX:
                    case APPLOVIN:
                        if (applovin_native_ad.getVisibility() != View.VISIBLE) {
                            nativeAdLoader = new MaxNativeAdLoader(appLovinNativeId, activity);
                            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                                @Override
                                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                                    // Clean up any pre-existing native ad to prevent memory leaks.
                                    if (nativeAd != null) {
                                        nativeAdLoader.destroy(nativeAd);
                                    }

                                    // Save ad for cleanup.
                                    nativeAd = ad;

                                    // Add ad view to view.
                                    applovin_native_ad.removeAllViews();
                                    applovin_native_ad.addView(nativeAdView);
                                    applovin_native_ad.setVisibility(View.VISIBLE);
                                    progress_bar_ad.setVisibility(View.GONE);
                                }

                                @Override
                                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                                    // We recommend retrying with exponentially higher delays up to a maximum delay
                                    loadBackupNativeAd();
                                }

                                @Override
                                public void onNativeAdClicked(final MaxAd ad) {
                                    // Optional click callback
                                }
                            });
                            if (darkTheme) {
                                nativeAdLoader.loadAd(createNativeAdViewDark());
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView());
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
                            progress_bar_ad.setVisibility(View.GONE);
                        }
                        break;

                    case UNITY:
                        //do nothing
                        break;

                }

            }

        }

        public void loadBackupNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {


                progress_bar_ad = view.findViewById(R.id.progress_bar_ad);
                progress_bar_ad.setVisibility(View.VISIBLE);

                switch (backupAdNetwork) {

                    case APPLOVIN_MAX:
                    case APPLOVIN:
                        if (applovin_native_ad.getVisibility() != View.VISIBLE) {
                            nativeAdLoader = new MaxNativeAdLoader(appLovinNativeId, activity);
                            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                                @Override
                                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                                    // Clean up any pre-existing native ad to prevent memory leaks.
                                    if (nativeAd != null) {
                                        nativeAdLoader.destroy(nativeAd);
                                    }

                                    // Save ad for cleanup.
                                    nativeAd = ad;

                                    // Add ad view to view.
                                    applovin_native_ad.removeAllViews();
                                    applovin_native_ad.addView(nativeAdView);
                                    applovin_native_ad.setVisibility(View.VISIBLE);
                                    progress_bar_ad.setVisibility(View.GONE);
                                }

                                @Override
                                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                                    // We recommend retrying with exponentially higher delays up to a maximum delay
                                }

                                @Override
                                public void onNativeAdClicked(final MaxAd ad) {
                                    // Optional click callback
                                }
                            });
                            if (darkTheme) {
                                nativeAdLoader.loadAd(createNativeAdViewDark());
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView());
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
                            progress_bar_ad.setVisibility(View.GONE);
                        }
                        break;

                    case UNITY:

                    case NONE:
                        //do nothing
                        break;

                }

            }

        }

        public MaxNativeAdView createNativeAdView() {
            MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_large_template_view)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.icon_image_view)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setOptionsContentViewGroupId(R.id.ad_options_view)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build();
            return new MaxNativeAdView(binder, activity);
        }

        public MaxNativeAdView createNativeAdViewDark() {
            MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_large_template_view)
                    .setTitleTextViewId(R.id.title_text_view)
                    .setBodyTextViewId(R.id.body_text_view)
                    .setAdvertiserTextViewId(R.id.advertiser_textView)
                    .setIconImageViewId(R.id.icon_image_view)
                    .setMediaContentViewGroupId(R.id.media_view_container)
                    .setOptionsContentViewGroupId(R.id.ad_options_view)
                    .setCallToActionButtonId(R.id.cta_button)
                    .build();
            return new MaxNativeAdView(binder, activity);
        }

    }

}
