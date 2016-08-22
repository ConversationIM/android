package com.example.sam.conversationalim;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class CreateConversationActivity extends AppCompatActivity {

    private EditText eT3;
    private EditText eT4;
    private SocketsMain socketService;


    @Override
    protected void onStart() {
        super.onStart();
        ServiceConnection mServerConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("connected to service", "onServiceConnected");
                SocketsMain.MyLocalBinder binder = (SocketsMain.MyLocalBinder) service;
                socketService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("disconnected service", "onServiceDisconnected");
            }
        };

        Intent serviceIntent = new Intent();
        serviceIntent.setClass(getApplicationContext(), SocketsMain.class);
        getApplicationContext().bindService(serviceIntent, mServerConn, getApplicationContext().BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_new_conversation2);


        eT3 = (EditText)

                findViewById(R.id.editText3);

        eT4 = (EditText)

                findViewById(R.id.editText4);


    }

    public void CreateConversation(View view) {
        socketService.addConversation(eT3, eT4);
        Toast.makeText(getApplicationContext(), "Conversation Added!", Toast.LENGTH_SHORT).show();
    }



    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void resetTexteT3(View view) {
        eT3.setText("");
    }

    public void resetTexteT4(View view) {
        eT4.setText("");
    }

}
