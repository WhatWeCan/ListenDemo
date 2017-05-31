package com.tjstudy.listendemo;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tjstudy.listendemo.base.BaseActivity;
import com.tjstudy.listendemo.interfaces.ISpeechCallBack;
import com.tjstudy.listendemo.net.NetUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private Button btnStartSpeech;
    private EditText etSpeechContent;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        etSpeechContent = (EditText) findViewById(R.id.et_speech_content);
        btnStartSpeech = (Button) findViewById(R.id.btn_speech_start);
        btnStartSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetUtils.getInstance()
                        .uploadParamGetWithField("test upload Param")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                loadDataError(e.getMessage());
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    responseBody.string();
                                    // TODO: 2017/5/31 正确的时候 直接在这里进行操作
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    loadDataError(e.getMessage());
                                }
                            }
                        });

                showSpeechDialog(new ISpeechCallBack() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResult(String result) {
                        if (result == null) {
                            return;
                        }
                        etSpeechContent.setText(result);
                        etSpeechContent.setSelection(result.length());
                    }
                });
            }
        });
    }
}
