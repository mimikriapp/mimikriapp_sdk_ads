package com.mimikri.ads.sdk.format;

import static com.mimikri.ads.sdk.util.Constant.ADMOB;
import static com.mimikri.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN;
import static com.mimikri.ads.sdk.util.Constant.NONE;
import static com.mimikri.ads.sdk.util.Constant.UNITY;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.mimikri.ads.sdk.R;
import com.mimikri.ads.sdk.util.NativeTemplateStyle;
import com.mimikri.ads.sdk.util.TemplateView;
import com.mimikri.ads.sdk.util.Tools;

public class NativeAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        LinearLayout native_ad_view_container;

        MediaView mediaView;
        TemplateView admob_native_ad;
        LinearLayout admob_native_background;


        FrameLayout applovin_native_ad;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobNativeId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public NativeAd.Builder build() {
            loadNativeAd();
            return this;
        }

        public NativeAd.Builder setPadding(int left, int top, int right, int bottom) {
            setNativeAdPadding(left, top, right, bottom);
            return this;
        }

        public NativeAd.Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public NativeAd.Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public NativeAd.Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public NativeAd.Builder setAdMobNativeId(String adMobNativeId) {
            this.adMobNativeId = adMobNativeId;
            return this;
        }

        public NativeAd.Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public NativeAd.Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public NativeAd.Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                native_ad_view_container = activity.findViewById(R.id.native_ad_view_container);
                admob_native_ad = activity.findViewById(R.id.admob_native_ad_container);
                mediaView = activity.findViewById(R.id.media_view);
                admob_native_background = activity.findViewById(R.id.background);

                applovin_native_ad = activity.findViewById(R.id.applovin_native_ad_container);

                switch (adNetwork) {
                    case ADMOB:
                        if (admob_native_ad.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admob_native_ad.setNativeAd(NativeAd);
                                        admob_native_ad.setVisibility(View.VISIBLE);
                                        native_ad_view_container.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd();
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                        }
                        break;



                    case APPLOVIN:

                    case UNITY:
                        //do nothing
                        break;
                }

            }

        }

        public void loadBackupNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                native_ad_view_container = activity.findViewById(R.id.native_ad_view_container);
                admob_native_ad = activity.findViewById(R.id.admob_native_ad_container);
                mediaView = activity.findViewById(R.id.media_view);
                admob_native_background = activity.findViewById(R.id.background);

                applovin_native_ad = activity.findViewById(R.id.applovin_native_ad_container);

                switch (backupAdNetwork) {
                    case ADMOB:
                        if (admob_native_ad.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admob_native_ad.setNativeAd(NativeAd);
                                        admob_native_ad.setVisibility(View.VISIBLE);
                                        native_ad_view_container.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admob_native_ad.setVisibility(View.GONE);
                                            native_ad_view_container.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                        }
                        break;



                    case APPLOVIN:

                    case UNITY:

                    case NONE:
                        native_ad_view_container.setVisibility(View.GONE);
                        break;
                }

            }

        }

        public void setNativeAdPadding(int left, int top, int right, int bottom) {
            native_ad_view_container = activity.findViewById(R.id.native_ad_view_container);
            native_ad_view_container.setPadding(left, top, right, bottom);
        }

    }

}
