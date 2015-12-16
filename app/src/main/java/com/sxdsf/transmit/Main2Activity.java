package com.sxdsf.transmit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.msg = Transmit.getInstance().receive(Main2Activity.class, String.class);
        this.text = (TextView) this.findViewById(R.id.textView);
        this.text.setText(this.msg);
        this.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.this.finish();
                Event<String> event = new Event<>();
                event.content = "测试";
                Transmit.getInstance().publish("fuck", event);
            }
        });
        /*
         * 在第二个Activity中来主动收取这个事件,其中第一个参数为Tag，是前一个Activity发出事件时填写的Tag，第二个参数为这个事件的消息会被转换成的类型
         */
    }
}
