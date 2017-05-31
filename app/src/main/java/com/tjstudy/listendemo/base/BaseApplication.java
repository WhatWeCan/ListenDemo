package com.tjstudy.listendemo.base;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * 自定义Application
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this, "appid=" + GloableVariables.IFLYTEK_APPID);
    }
}
