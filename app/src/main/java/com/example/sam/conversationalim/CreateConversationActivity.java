package com.example.sam.conversationalim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;




public class CreateConversationActivity extends AppCompatActivity {

    private EditText eT3;
    private EditText eT4;
    private Socket mSocket;
    private String token;
    private String conversationId;
    boolean isUpdated = false;

    String serverResponse;





    {
        try {
            mSocket = IO.socket("http://staging-magerko2.rhcloud.com/v1/conversation");
            //mSocket = IO.socket("http://nma55251.pagekite.me/mvp");
            Log.i("ConversationIM", "Initializing socket connection");
        } catch (URISyntaxException e) {
            Log.e("ConversationIM", "Problem while initializing", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        token = extras.getString("token");

        mSocket.on("error", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ConversationIM", "args length: " + args.length);
                        Error error = new Error(args[0].toString());
                        Throwable t = (Throwable) args[0];
                        StringWriter sw = new StringWriter();
                        t.printStackTrace(new PrintWriter(sw));
                        Log.d("ConversationIM", String.format("An error ocurred: %s", error));
                        //Toast.makeText(getActivity().getApplicationContext(), "error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).on("added", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }).on("updated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject obj = (JSONObject) args[0];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            conversationId = obj.get("conversationId").toString();
                        }catch(JSONException e){
                            conversationId = "ERROR";
                        }
                        isUpdated = true;

                    }
                });
            }
        }).on("joined", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        String joinReceipt = args[0].toString();
                        Log.w("ConversationIM", "joined successfully: " + joinReceipt);
                    }
                }
        ).on("connect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

            }
        });
        mSocket.connect();

        setContentView(R.layout.activity_new_conversation2);






        eT3 = (EditText)

                findViewById(R.id.editText3);

        eT4 = (EditText)

                findViewById(R.id.editText4);


    }

    public void CreateConversation(View view) {
        Button newConversationButton = (Button) findViewById(R.id.convoButton);
        newConversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = addConversation();
                Log.w("success value: ", (success) ? "true" : "false");
                if (success) {
                    Log.w("Very ", "Noice");

                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), convoBoardActivity.class);
                    String str = token;
                    String newRoom = eT3.getText().toString();
                    intent.putExtra("token", str);
                    intent.putExtra("newRoom", newRoom);
                    intent.putExtra("id", conversationId);

                    startActivity(intent);

                } else {
                    Log.w("try again", "couldn't create convo");
                }
            }
        });


    }

    public boolean addConversation() {
        try {
            String list = eT4.getText().toString();
            InputStream inputStream = null;
            JSONObject participants = new JSONObject("{\n\t" + "\"participants\": [\" " + list + "\"]\n}");
            Log.w("parants.toString(): ", participants.toString());

            String url = "http://staging-magerko2.rhcloud.com/v1/conversation";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                StringEntity se = new StringEntity(participants.toString());
                httpPost.setEntity(se);


                httpPost.setHeader("Authorization", token);
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null) {
                    serverResponse = convertInputStreamToString(inputStream);
                    return true;
                }
                else{
                    serverResponse = "Did not work!";
                    return false;
                }

            } catch (Exception e) {
                Log.w("post error", "error posting");
                return false;
            }
        } catch (JSONException e) {
            Log.w("JSON error", "error JSON");
            return false;
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
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
