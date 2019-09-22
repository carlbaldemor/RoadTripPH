package ph.roadtrip.roadtrip.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

public class ListChatsOwnerFragment extends Fragment {

    // Log tag
    private static final String TAG = ListChatsOwnerFragment.class.getSimpleName();

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<Messages> chatList = new ArrayList<Messages>();
    private ListView listView;
    private ChatListOwnerAdapter adapter;

    private ListView lv;
    private ImageView add_pic;
    private String getPicUrl;
    private String carPicture;
    private int ownerID;
    private int userID;

    private SessionHandler session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        listView = (ListView) view.findViewById(R.id.listview_chat);
        adapter = new ChatListOwnerAdapter(getActivity(), chatList);
        listView.setAdapter(adapter);

        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();

        UrlBean getUrl = new UrlBean();
        url = getUrl.getChatlistowner()+userID;

        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getProfilePicUrl();

        // Creating volley request obj
        final JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());


                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Messages chats = new Messages();
                        chats.setSender_firstName(obj.getString("firstName"));
                        chats.setSender_lastName(obj.getString("lastName"));
                        chats.setImageUrl(getPicUrl+obj.getString("profilePicture"));
                        chats.setChatID(obj.getInt("chatID"));
                        chats.setStartedDateTime(obj.getString("startedDateTime"));

                        // adding movie to movies array
                        chatList.add(chats);

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



        return view;
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
}
