package com.example.sam.conversationalim;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */


public class SOCKETZANDSHIT extends Service {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.sam.conversationalim.action.FOO";
    private static final String ACTION_BAZ = "com.example.sam.conversationalim.action.BAZ";
    public final static String MY_ACTION = "MY_ACTION";

    public ArrayList<Conversation> getListConversations() {
        return listConversations;
    }

    ArrayList<Conversation> listConversations = new ArrayList<>();


    Handler handler;
    private Socket conversationSocket;
    private Socket global;
    private String token;
    private String instanceOfConversationId;

    private String conversationName;
    private String globalId;
    private final IBinder bindItBitch = new MyLocalBinder();
    private boolean received = false;

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 5000); // 5 seconds
        }
    };

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.sam.conversationalim.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.sam.conversationalim.extra.PARAM2";


    public class MyLocalBinder extends Binder {
        SOCKETZANDSHIT getService() {
            return SOCKETZANDSHIT.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bindItBitch;
    }

    private void DisplayLoggingInfo() {

    }

    @Override
    public int onStartCommand(Intent intent, int i, int j) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

        try {
            token = intent.getExtras().getString("token");
        } catch (NullPointerException e) {
            Log.w("No token in intent", "no biggie though");
        }
        Log.w("token value: ", token);
        global.connect();
        try {
            JSONObject obj = new JSONObject();
            obj.put("token", token);

            //global.emit("initialize", obj.toString());
        } catch (JSONException e) {
            Log.w("Failed to initialize", "global");
        }


        return START_STICKY;
    }


    @Override
    public void onCreate() {
        handler = new Handler();
        super.onCreate();

        {
            try {
                global = IO.socket("http://staging-magerko2.rhcloud.com/global");

                conversationSocket= IO.socket("http://staging-magerko2.rhcloud.com/conversation");

                Log.i("ConversationIM", "Initializing socket connection");
            } catch (URISyntaxException e) {
                Log.e("ConversationIM", "Problem while initializing", e);
            }
        }


        global.on("added", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    Log.w("ADDED TO GLOBAL", obj.toString());
                    //Toast.makeText(getApplicationContext(), "Added to Conversation" + obj.getString("conversationId"), Toast.LENGTH_SHORT).show();
                    listConversations.add(new Conversation(obj.getString("conversationId"), obj.getString("creator")));
                } catch (JSONException e) {
                    Log.w("cannot parse JSON", "added event was triggered");
                }
            }
        }).on("connect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.w("connected to Global", "CONGRADULATIONS");
                conversationSocket.connect();
            }
        });

        conversationSocket.on("error", new Emitter.Listener() {
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
                        Log.d("ConversationIM", String.format("An error ocurred: %s", sw.toString()));
                        //Toast.makeText(getActivity().getApplicationContext(), "error: " + error, Toast.LENGTH_LONG).show();
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
                        Intent intent = new Intent();
                        intent.setAction(MY_ACTION);
                        intent.putExtra("updateEvent", obj.toString());
                        sendBroadcast(intent);
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
                Log.i("ConversationIM", "Connected: to conversation");
                try {
                    JSONObject obj2 = new JSONObject();
                    obj2.put("token", token);
                    final JSONObject obj = obj2;
                    global.emit("initialize", obj.toString()/*, new Ack(){
                    @Override
                    public void call(Object... args){
                        conversationSocket.emit("initialize", obj.toString());
                    }
                }*/);
                } catch (JSONException e) {
                    Log.w("Failed to initialize", "global");
                }
                //Log.w("CONVIM", "connect has executed");
                //Toast.makeText(getApplicationContext(), "connect", Toast.LENGTH_SHORT).show();
                //conversationSocket.disconnect();
            }
        });


    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public void joinSpecifiedConversation(Conversation c) {
        received = false;
        instanceOfConversationId = c.getConversationID();
        received = false;
    }

    public void emit(String s, String t) {
        conversationSocket.emit(s, t);
    }

    public void addConversation(final EditText eT3, final EditText eT4) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String conversationId;
                    String serverResponse;
                    conversationName = eT3.getText().toString();
                    String list = eT4.getText().toString();
                    InputStream inputStream = null;
                    JSONObject participants = new JSONObject("{\n\t" + "\"participants\": [\"" + list + "\"]\n}");

                    String url = "http://staging-magerko2.rhcloud.com/v1/conversation";

                    try {

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        httpPost.setHeader("Authorization", token);
                        StringEntity payload = new StringEntity(participants.toString());

                        Log.w("parants.toString() ", participants.toString());

                        httpPost.setEntity(payload);

                        HttpResponse httpResponse = httpclient.execute(httpPost);


                        inputStream = httpResponse.getEntity().getContent();
                        serverResponse = convertInputStreamToString(inputStream);


                        if (serverResponse != null) {
                            Log.w("SERVER RESPONSE!!!", serverResponse);
                            Log.w("Very ", "Noice");

                            try {
                                JSONObject conversationID = new JSONObject(serverResponse);
                                JSONObject data = new JSONObject(conversationID.getString("data"));
                                conversationId = data.getString("conversationId");
                                Conversation c = new Conversation(conversationId, conversationName);
                                listConversations.add(c);
                                Log.w("Added Conversation", c.toString());

                            } catch (JSONException e) {
                                Log.w("conversationId", "null");
                            }

                            return;
                        } else {
                            serverResponse = "Did not work!";
                            Log.w("server response", serverResponse);
                            serverResponse = null;
                            return;
                        }

                    } catch (IOException e) {
                        Log.w("post error", e);
                        serverResponse = null;
                        return;
                    }
                } catch (JSONException e) {
                    Log.w("JSON error", e);
                    return;
                }
            }
        });
        thread.start();
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

    public JSONObject makeJoinRequest(String id) {
        JSONObject joinRequest = null;
        try {
            joinRequest = new JSONObject("{ \n\"token\": " + token + ",\n\"conversationId\": \"" + id + "\" \n}");
        } catch (JSONException e) {
            Log.w("JSON error : ", e);
        }

        return joinRequest;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Socket getGlobal() {
        return global;
    }

    public void setGlobal(Socket global) {
        this.global = global;
    }

    public Socket getconversationSocket() {
        return conversationSocket;
    }

    public void setconversationSocket(Socket conversationSocket) {
        this.conversationSocket = conversationSocket;
    }

    public String getConversationId() {
        return instanceOfConversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }
}
