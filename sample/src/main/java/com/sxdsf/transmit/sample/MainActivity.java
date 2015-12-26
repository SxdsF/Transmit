package com.sxdsf.transmit.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.Topic;
import com.sxdsf.transmit.TransmitTopic;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    private Observable<Integer> observable;
    public static final Topic topic = new TransmitTopic("MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sxdsf.transmit.sample.R.layout.activity_main);
        Processor.autoBindView(this);
        this.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Main2Activity.class);
                MainActivity.this.startActivity(intent);
                MyApplication.syncTransmitService.post(Main2Activity.destination, Message.create("测试"));
            }
        });
        this.observable = MyApplication.syncTransmitService.register(topic, Integer.class);
        this.observable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer s) {
                        text.setText(s);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        MyApplication.syncTransmitService.unRegister(topic, this.observable);
        super.onDestroy();
    }
}
