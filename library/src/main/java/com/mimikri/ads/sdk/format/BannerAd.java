package com.mimikri.ads.sdk.format;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.applovin.adview.AppLovinAdView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.ads.AdRequest;
import com.mimikri.ads.sdk.R;
import com.mimikri.ads.sdk.helper.AppLovinCustomEventBanner;

import static com.mimikri.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN_DISCOVERY;
import static com.mimikri.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikri.ads.sdk.util.Constant.NONE;


public class BannerAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;

        private AppLovinAdView appLovinAdView;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String appLovinBannerId = "";
        private String appLovinBannerZoneId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadBannerAd();
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

        public Builder setAppLovinBannerId(String appLovinBannerId) {
            this.appLovinBannerId = appLovinBannerId;
            return this;
        }

        public Builder setAppLovinBannerZoneId(String appLovinBannerZoneId) {
            this.appLovinBannerZoneId = appLovinBannerZoneId;
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

        public void loadBannerAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                        RelativeLayout appLovinAdView = activity.findViewById(R.id.applovin_banner_view_container);
                        MaxAdView maxAdView = new MaxAdView(appLovinBannerId, activity);
                        maxAdView.setListener(new MaxAdViewAdListener() {
                            @Override
                            public void onAdExpanded(MaxAd ad) {

                            }

                            @Override
                            public void onAdCollapsed(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                appLovinAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {

                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {

                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                appLovinAdView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                            }
                        });

                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height);
                        maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                        if (darkTheme) {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundDark));
                        } else {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundLight));
                        }
                        appLovinAdView.addView(maxAdView);
                        maxAdView.loadAd();
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + appLovinBannerId);
                        break;

                    case APPLOVIN_DISCOVERY:
                        RelativeLayout appLovinDiscoveryAdView = activity.findViewById(R.id.applovin_discovery_banner_view_container);
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", appLovinBannerZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        this.appLovinAdView = new AppLovinAdView(adSize, activity);
                        this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinDiscoveryAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                appLovinDiscoveryAdView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                            }
                        });
                        appLovinDiscoveryAdView.addView(this.appLovinAdView);
                        this.appLovinAdView.loadNextAd();
                        break;


                    case NONE:
                        //do nothing
                        break;
                }
                Log.d(TAG, "Banner Ad is enabled");
            } else {
                Log.d(TAG, "Banner Ad is disabled");
            }
        }

        public void loadBackupBannerAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {


                    case APPLOVIN:
                    case APPLOVIN_MAX:
                        RelativeLayout appLovinAdView = activity.findViewById(R.id.applovin_banner_view_container);
                        MaxAdView maxAdView = new MaxAdView(appLovinBannerId, activity);
                        maxAdView.setListener(new MaxAdViewAdListener() {
                            @Override
                            public void onAdExpanded(MaxAd ad) {

                            }

                            @Override
                            public void onAdCollapsed(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                appLovinAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {

                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {

                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                appLovinAdView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                            }
                        });

                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height);
                        maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                        if (darkTheme) {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundDark));
                        } else {
                            maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.colorBackgroundLight));
                        }
                        appLovinAdView.addView(maxAdView);
                        maxAdView.loadAd();
                        Log.d(TAG, adNetwork + " Banner Ad unit Id : " + appLovinBannerId);
                        break;

                    case APPLOVIN_DISCOVERY:
                        RelativeLayout appLovinDiscoveryAdView = activity.findViewById(R.id.applovin_discovery_banner_view_container);
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", appLovinBannerZoneId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        this.appLovinAdView = new AppLovinAdView(adSize, activity);
                        this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinDiscoveryAdView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                appLovinDiscoveryAdView.setVisibility(View.GONE);
                            }
                        });
                        appLovinDiscoveryAdView.addView(this.appLovinAdView);
                        this.appLovinAdView.loadNextAd();
                        break;


                }
                Log.d(TAG, "Banner Ad is enabled");
            } else {
                Log.d(TAG, "Banner Ad is disabled");
            }
        }

    }

}
