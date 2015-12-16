package com.sxdsf.transmit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sxdsf.transmit.core.Transmit;

public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.msg = Transmit.getInstance().get("test", String.class);
        this.text = (TextView) this.findViewById(R.id.textView);
        this.text.setText(this.msg);
        this.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.this.finish();
                Event<String> event = new Event<>();
                event.content = "测试";
                Transmit.getInstance().post("fuck", event);
            }
        });
    }
}
