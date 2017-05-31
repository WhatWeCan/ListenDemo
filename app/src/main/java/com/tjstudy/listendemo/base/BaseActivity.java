package com.tjstudy.listendemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tjstudy.listendemo.interfaces.ISpeechCallBack;
import com.tjstudy.listendemo.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class BaseActivity extends AppCompatActivity {
    HashMap<String, String> mIatResults = new LinkedHashMap<>();

    protected void initVariables() {
    }

    public abstract void initView();

    protected void loadData() {
    }

    protected void loadDataError(String error) {
        // TODO: 2017/5/31 统一的网络访问错误处理--提示
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView();
        loadData();
    }

    /**
     * 显示对话框：语音转文本
     *
     * @param onSpeechListener 接口
     */
    protected void showSpeechDialog(final ISpeechCallBack onSpeechListener) {
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能,放置布局文件和图片资源
        //使用云服务InitListener参数写成null
        RecognizerDialog mIatDialog = new RecognizerDialog(this, null);
        mIatDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                String text = JsonParser.parseIatResult(recognizerResult.getResultString());

                String sn = null;
                // 读取json结果中的sn字段
                try {
                    JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                    sn = resultJson.optString("sn");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mIatResults.put(sn, text);
                StringBuilder resultBuffer = new StringBuilder();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults.get(key));
                }
                onSpeechListener.onResult(resultBuffer.toString());
            }

            @Override
            public void onError(SpeechError speechError) {
                onSpeechListener.onError(speechError.getPlainDescription(true));
            }
        });
        mIatDialog.show();
    }
}
