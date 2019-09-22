package ph.roadtrip.roadtrip.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;


public class ListMessagesFragment extends Fragment {


    // Log tag
    private static final String TAG = ListMessagesFragment.class.getSimpleName();

    private static final String KEY_STATUS = "status1";
    private static final String KEY_MESSAGE = "message1";
    private static final String KEY_CHAT_ID = "chatID";
    private static final String KEY_CHAT_MESSAGE = "message";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_EMPTY = "";

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<Messages> myMessagesList = new ArrayList<>();
    private ListView listView;
    private CustomMessagesAdapter adapter;


    private ListView lv;
    private ImageView add_pic;
    private ImageButton sendMessage;
    private EditText message;
    private String getPicUrl;
    private String carPicture;
    private int userID;
    private int chatID;
    private String send_message_url;
    private String chatMessage;
    private Thread t;

    private SessionHandler session;
    //public static ParseUser mUserClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        myMessagesList.clear();
        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomMessagesAdapter(getActivity(), myMessagesList);
        listView.setAdapter(adapter);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());

        User user = session.getUserDetails();
        userID = user.getUserID();

        Messages messages = session.getChatID();
        //userID = user.getUserID();
        chatID = messages.getChatID();

        //Url for getting JSON files
        UrlBean getUrl = new UrlBean();
        url = getUrl.getChatMessages()+chatID;

        //Get Picture
        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getProfilePicUrl();

        sendMessage = view.findViewById(R.id.sendMessage);
        message = view.findViewById(R.id.editText);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatMessage = message.getText().toString();

                if (!chatMessage.equals(KEY_EMPTY)){
                    insertMessage();
                }
            }
        });

        // Creating volley request obj
        final JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());


                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Messages movie = new Messages();
                        movie.setSender_firstName(obj.getString("firstName"));
                        movie.setSender_lastName(obj.getString("lastName"));
                        movie.setMessage(obj.getString("message"));
                        movie.setDateAdded(obj.getString("dateAdded"));
                        movie.setImageUrl(getPicUrl+obj.getString("profilePicture"));
                        movie.setChatcontent_sender(obj.getInt("chatcontent_sender"));
                        // adding movie to movies array
                        myMessagesList.add(movie);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(movieReq);


        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setStackFromBottom(true);

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                // Creating volley request obj
                final JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        myMessagesList.clear();
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Messages movie = new Messages();
                                movie.setSender_firstName(obj.getString("firstName"));
                                movie.setSender_lastName(obj.getString("lastName"));
                                movie.setMessage(obj.getString("message"));
                                movie.setDateAdded(obj.getString("dateAdded"));
                                movie.setImageUrl(getPicUrl+obj.getString("profilePicture"));
                                movie.setChatcontent_sender(obj.getInt("chatcontent_sender"));
                                // adding movie to movies array
                                myMessagesList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());


                    }
                });

                // Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(movieReq);


                adapter.notifyDataSetChanged();
                handler.postDelayed( this, 60 * 60 );
            }
        }, 60 * 100 );



        return view;
    }

    public void insertMessage() {
        UrlBean url = new UrlBean();
        send_message_url = url.getSend_message_url();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_CHAT_ID, chatID);
            request.put(KEY_USER_ID, userID);
            request.put(KEY_CHAT_MESSAGE, chatMessage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, send_message_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                message.setText(KEY_EMPTY);
                                hideSoftKeyboard(getActivity());



                            } else{
                                Toast.makeText(getActivity(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Display error message whenever an error occurs
                        Toast.makeText(getActivity(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsArrayRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
