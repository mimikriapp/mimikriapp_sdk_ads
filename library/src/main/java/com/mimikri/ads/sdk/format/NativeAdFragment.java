package com.mimikri.ads.sdk.format;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.google.android.gms.ads.nativead.MediaView;
import com.mimikri.ads.sdk.R;
import com.mimikri.ads.sdk.util.Constant;

import static com.mimikri.ads.sdk.util.Constant.ADMOB;
import static com.mimikri.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikri.ads.sdk.util.Constant.NONE;
import static com.mimikri.ads.sdk.util.Constant.UNITY;

public class NativeAdFragment {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        View view;

        LinearLayout native_ad_view_container;
        MediaView mediaView;


        private final String adMobNativeId = "";
        FrameLayout applovin_native_ad;
        MaxNativeAdLoader nativeAdLoader;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        MaxAd nativeAd;
        private String appLovinNativeId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        private String nativeAdStyle = "";

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadNativeAd();
            return this;
        }

        public Builder setPadding(int left, int top, int right, int bottom) {
            setNativeAdPadding(left, top, right, bottom);
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
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

        public Builder setNativeAdStyle(String nativeAdStyle) {
            this.nativeAdStyle = nativeAdStyle;
            return this;
        }

        public void loadNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                native_ad_view_container = view.findViewById(R.id.native_ad_view_container);


                applovin_native_ad = view.findViewById(R.id.applovin_native_ad_container);

                switch (adNetwork) {
                    case ADMOB:

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
                                    native_ad_view_container.setVisibility(View.VISIBLE);
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
                                nativeAdLoader.loadAd(createNativeAdViewDark(nativeAdStyle));
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView(nativeAdStyle));
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
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

                native_ad_view_container = view.findViewById(R.id.native_ad_view_container);

                applovin_native_ad = view.findViewById(R.id.applovin_native_ad_container);

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
                                    native_ad_view_container.setVisibility(View.VISIBLE);
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
                                nativeAdLoader.loadAd(createNativeAdViewDark(nativeAdStyle));
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView(nativeAdStyle));
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
                        }
                        break;

                    case NONE:
                        native_ad_view_container.setVisibility(View.GONE);
                        break;

                }

            }

        }

        public void setNativeAdPadding(int left, int top, int right, int bottom) {
            native_ad_view_container = view.findViewById(R.id.native_ad_view_container);
            native_ad_view_container.setPadding(left, top, right, bottom);
        }

        public MaxNativeAdView createNativeAdView(String nativeAdStyle) {
            MaxNativeAdViewBinder binder;
            if (Constant.STYLE_RADIO.equals(nativeAdStyle)) {
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_radio_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
            } else {
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_medium_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
            }
            return new MaxNativeAdView(binder, activity);
        }

        public MaxNativeAdView createNativeAdViewDark(String nativeAdStyle) {
            MaxNativeAdViewBinder binder;
            if (Constant.STYLE_RADIO.equals(nativeAdStyle)) {
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_radio_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
            } else {
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_medium_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
            }
            return new MaxNativeAdView(binder, activity);
        }

    }

}