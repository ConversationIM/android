package com.example.sam.conversationalim;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    EditText et1, et2;
    boolean isOpen = false;
    AboutFrag f1;
    Button b;
    ProgressDialog progress;

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

//                Intent intent = new Intent();
//                intent.setClass(v.getContext(), convoBoardActivity.class);
////                String str = et2.getText().toString();
////                intent.putExtra("mystring", str);
//                startActivity(intent);

                // Instantiate the RequestQueue.
                progress = ProgressDialog.show(MainActivity.this, "Loading",
                        "finding user", true);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "http://staging-magerko2.rhcloud.com/v1/auth";

                // Request a string response from the provided URL.
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", et2.getText());//"admin@example.com");
                    jsonBody.put("password", et1.getText());//"password77");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Display the first 500 characters of the response string.
//                                mTextView.setText("Response is: "+ response.substring(0,500));
                                try {
                                    Intent intent = new Intent();
                                    intent.setClass(getApplicationContext(), convoBoardActivity.class);
                                    String str = response.getJSONObject("data").getString("token");
                                    intent.putExtra("token", str);

                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "error",
                                Toast.LENGTH_LONG).show();
                    }
                });
                // Add the request to the RequestQueue.
                                queue.add(request);


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
}
