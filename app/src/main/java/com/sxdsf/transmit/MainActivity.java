package com.sxdsf.transmit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sxdsf.transmit.service.filter.impl.ClassFilter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private Observable<String> observable;
    public static final Topic topic = new TransmitTopic("MainActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.text = (TextView) this.findViewById(R.id.text);
        this.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Main2Activity.class);
                MainActivity.this.startActivity(intent);
                MyApplication.syncTransmitService.post(Main2Activity.destination, Message.create("测试"));
            }
        });
        this.observable = MyApplication.syncTransmitService.register(topic, new ClassFilter(String.class));
        this.observable.
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
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
