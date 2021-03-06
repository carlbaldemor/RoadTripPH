package ph.roadtrip.roadtrip.transactionhistory;

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
import android.widget.TextView;

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


public class ListTransactionHistoryFragment extends Fragment {


    // Log tag
    private static final String TAG = ListTransactionHistoryFragment.class.getSimpleName();

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<Booking> transactionList = new ArrayList<>();
    private ListView listView;
    private CustomHistoryAdapter adapter;


    private ListView lv;
    private TextView filter;
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
        transactionList.clear();
        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomHistoryAdapter(getActivity(), transactionList);
        listView.setAdapter(adapter);


        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();

        //Url for getting JSON files
        UrlBean getUrl = new UrlBean();
        url = getUrl.getGet_history()+userID;

        //Get Picture
        UrlBean picUrl = new UrlBean();
        final String getPicUrl = picUrl.getGetPicUrl();

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
                        Booking movie = new Booking();
                        movie.setBookingID(obj.getInt("bookingID"));
                        movie.setModelName(obj.getString("modelName"));
                        movie.setBrandName(obj.getString("brandName"));
                        movie.setRecordPicture(getPicUrl+obj.getString("recordPicture"));
                        movie.setModelYear(obj.getString("modelYear"));
                        movie.setStatus(obj.getString("status"));
                        movie.setTotalAmount(obj.getString("totalAmount"));
                        movie.setRecordID(obj.getInt("recordID"));
                        movie.setPlateNumber(obj.getString("plateNumber"));
                        movie.setColor(obj.getString("color"));
                        movie.setOwnerID(obj.getInt("ownerID"));
                        movie.setDateAdded(obj.getString("dateAdded"));

                        if (obj.getString("favorite") == "null"){
                            movie.setFavorite(false);
                        } else {
                            movie.setFavorite(true);
                    }

                        // adding movie to movies array
                        transactionList.add(movie);

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
