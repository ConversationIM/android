package com.example.sam.conversationalim;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    EditText et1, et2;
    boolean isOpen = false;
    AboutFrag f1;
    Button b;

    //JSON final Variables
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.conversation_im);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);

        addListenerOnButton();


    }

    public void login(){
        Button b2 = (Button) findViewById(R.id.nextButton);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), messageBoardActivity.class); //will need to change
                startActivity(intent);
            }
        });


    }

    public void addListenerOnButton() {
        b = (Button) findViewById(R.id.nextButton);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(v.getContext(), convoBoardActivity.class);
                String str = et2.getText().toString();
                intent.putExtra("mystring", str);
                startActivity(intent);



            }
        });

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

    public class JSONDataAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            //Not sure how to do this yet, working on it.

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
    private void parseMessage(final String msg){

        try{
            JSONObject jObj = new JSONObject();
            String flag = jObj.getString("flag");

            if (flag.equalsIgnoreCase(TAG_NEW)){
                String name = jObj.getString("sender");
                String message = jObj.getString("message");

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
