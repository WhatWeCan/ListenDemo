package com.tjstudy.listendemo;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tjstudy.listendemo.base.BaseActivity;
import com.tjstudy.listendemo.listener.OnSpeechCallBackListener;

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
                showSpeechDialog(new OnSpeechCallBackListener() {
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
