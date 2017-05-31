package com.tjstudy.listendemo.interfaces;

/**
 * 语音转文本 反馈接口
 */

public interface ISpeechCallBack {
    void onError(String error);

    void onResult(String result);
}
