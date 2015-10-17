package com.example.sam.conversationalim;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Sam on 10/17/2015.
 */
public class SecondActivity extends Activity {
TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        tv = (TextView) findViewById(R.id.textView3);

        String str = getIntent().getExtras().getString("mystring");
        tv.setText(str);



    }


}
