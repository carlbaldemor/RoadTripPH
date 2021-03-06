package ph.roadtrip.roadtrip.myfavorites;

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

import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;


public class ListMyFavoritesFragment extends Fragment {


    // Log tag
    private static final String TAG = ListMyFavoritesFragment.class.getSimpleName();

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<MyFavorites> myFavoritesList = new ArrayList<>();
    private ListView listView;
    private CustomFavoritesAdapter adapter;


    private ListView lv;
    private ImageView add_pic;
    private String getPicUrl;
    private String carPicture;
    private int userID;

    private SessionHandler session;
    //public static ParseUser mUserClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        myFavoritesList.clear();
        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomFavoritesAdapter(getActivity(), myFavoritesList);
        listView.setAdapter(adapter);

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();

        //Url for getting JSON files
        UrlBean getUrl = new UrlBean();
        url = getUrl.getListFavorites()+userID;

        //Get Picture
        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getProfilePicUrl();

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        final JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        MyFavorites movie = new MyFavorites();
                        movie.setUserID(userID);
                        movie.setOwnerID(obj.getInt("ownerID"));
                        movie.setOwner_firstName(obj.getString("firstName"));
                        movie.setOwner_lastName(obj.getString("lastName"));
                        movie.setProfilePicture(getPicUrl+obj.getString("profilePicture"));
                        movie.setDateAdded(obj.getString("dateAdded"));
                        // adding movie to movies array
                        myFavoritesList.add(movie);

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
                hidePDialog();

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
