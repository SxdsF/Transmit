package com.sxdsf.transmit.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sxdsf.transmit.Destination;
import com.sxdsf.transmit.Message;
import com.sxdsf.transmit.TransmitDestination;
import com.sxdsf.transmit.service.producer.MessageProducer;

public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private String msg;
    public static final Destination destination = new TransmitDestination("Main2Activity");
    private MessageProducer producer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.msg = MyApplication.syncTransmitService.receive(destination, String.class);
        this.producer = MyApplication.syncTransmitService.createSyncProducer(MainActivity.topic);
        this.text = (TextView) this.findViewById(R.id.textView);
        this.text.setText(this.msg);
        this.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.this.finish();
                producer.send(Message.create(new Object()));
            }
        });
    }
}
