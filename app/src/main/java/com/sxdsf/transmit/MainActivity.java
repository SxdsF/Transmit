package com.sxdsf.transmit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private Observable<Event<String>> observable;

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
                Transmit.getInstance().post(Main2Activity.class, "测试");
            }
        });
        this.observable = Transmit.getInstance().<Event<String>>register("fuck");
        this.observable.
                subscribeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<Event<String>>() {
                    @Override
                    public void call(Event<String> s) {
                        text.setText(s.content);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        Transmit.getInstance().unRegister("fuck", this.observable);
        super.onDestroy();
    }
}
