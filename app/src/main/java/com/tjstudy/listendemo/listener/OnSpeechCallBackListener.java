package com.tjstudy.listendemo.listener;

/**
 * 语音转文本 反馈接口
 */

public interface OnSpeechCallBackListener {
    void onError(String error);

    void onResult(String result);
}
