package com.sxdsf.transmit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private String msg;
    public static final Topic topic = new TransmitTopic("Main2Activity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.msg = MyApplication.asyncTransmitService.receive(topic);
        this.text = (TextView) this.findViewById(R.id.textView);
        this.text.setText(this.msg);
        this.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.this.finish();
                MyApplication.asyncTransmitService.asyncPublish(MainActivity.topic, Message.create("测试"));
            }
        });
    }
}
