package ph.roadtrip.roadtrip.bookingmodule;

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

import ph.roadtrip.roadtrip.carmanagement.CarRecord;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;

public class AcceptedBookingFragment extends Fragment {

    // Log tag
    private static final String TAG = AcceptedBookingFragment.class.getSimpleName();

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<BookingRequests> movieList = new ArrayList<BookingRequests>();
    private ListView listView;
    private CustomAcceptedListAdapter adapter;

    private ListView lv;
    private ImageView add_pic;
    private String getPicUrl;
    private String carPicture;
    private int ownerID;

    private SessionHandler session;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_accepted, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomAcceptedListAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
        setHasOptionsMenu(false);

        session = new SessionHandler(getActivity().getApplicationContext());

        CarRecord carRecord = session.getOwnerID();
        ownerID = carRecord.getOwnerID();

        UrlBean getUrl = new UrlBean();
        url = getUrl.getAcceptedBookings()+ownerID;

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
                        BookingRequests movie = new BookingRequests();
                        movie.setBrandName(obj.getString("brandName"));
                        movie.setModelName(obj.getString("modelName"));
                        movie.setImageUrl(getPicUrl+obj.getString("profilePicture"));
                        movie.setModelYear(obj.getString("modelYear"));
                        movie.setFirstName(obj.getString("firstName"));
                        movie.setLastName(obj.getString("lastName"));
                        movie.setBookingID(obj.getInt("bookingID"));
                        movie.setTotalAmount(obj.getString("totalAmount"));

                        // adding movie to movies array
                        movieList.add(movie);

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
