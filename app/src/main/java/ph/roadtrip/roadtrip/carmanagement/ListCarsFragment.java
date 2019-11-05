package ph.roadtrip.roadtrip.carmanagement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.myfavorites.ListMyFavoritesFragment;

import android.util.Log;
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


public class ListCarsFragment extends Fragment {

    // Log tag
    private static final String TAG = ListCarsFragment.class.getSimpleName();

    // Movies json url
    private String url;
    private ProgressDialog pDialog;
    private List<CarRecord> movieList = new ArrayList<CarRecord>();
    private ListView listView;
    private CustomListAdapter adapter;


    private ListView lv;
    private ImageView add_pic;
    private String getPicUrl;
    private String carPicture;
    private int ownerID;

    private SessionHandler session;
    //public static ParseUser mUserClicked;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_car_management, container, false);

        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomListAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);

        session = new SessionHandler(getActivity().getApplicationContext());

        CarRecord carRecord = session.getOwnerID();

        ownerID = carRecord.getOwnerID();

        UrlBean getUrl = new UrlBean();
        url = getUrl.getListCarsMyCars()+ownerID;

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
                        CarRecord movie = new CarRecord();
                        movie.setBrandName(obj.getString("brandName"));
                        movie.setModelName(obj.getString("modelName"));
                        movie.setImageUrl(getPicUrl+obj.getString("recordPicture"));
                        movie.setModelYear(obj.getString("modelYear"));
                        movie.setStatus(obj.getString("status"));
                        movie.setCarType(obj.getString("carType"));
                        movie.setPlateNumber(obj.getString("plateNumber"));
                        movie.setRecordID(obj.getInt("recordID"));
                        movie.setCarID(obj.getInt("carID"));
                        movie.setDateAdded(obj.getString("dateAdded"));
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

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddCarOneFragment()).commit();
            }
        });

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
