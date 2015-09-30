package com.example.sam.conversationalim;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {
    EditText et1, et2;
    boolean isOpen = false;
    AboutFrag f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.conversation_im);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);


    }

    /*
    public void frag1(View view){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Frag1 f1 = new Frag1();
        ft.add(R.id.fr1, f1);
        ft.commit();
    }
    */

    public void about(View view){
        if (isOpen) {//getFragmentManager().beginTransaction().remove(this).commit();
            MainActivity.this.getFragmentManager().beginTransaction().remove(f1).commit();
            isOpen = !isOpen;
            return;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        f1 = new AboutFrag();
        ft.add(R.id.about_frag, f1);
        ft.commit();
        isOpen = !isOpen;
    }

    public void resetTextEt1(View view) {
        et1.setText("");
    }

    public void resetTextEt2(View view) {
        et2.setText("");
    }
}
