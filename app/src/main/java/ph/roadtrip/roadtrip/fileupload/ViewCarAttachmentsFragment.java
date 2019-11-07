package ph.roadtrip.roadtrip.fileupload;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import ph.roadtrip.roadtrip.carmanagement.CarRecord;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

import static com.android.volley.VolleyLog.TAG;

public class ViewCarAttachmentsFragment extends android.support.v4.app.Fragment {

    private TextView date1, date2;
    private ImageView apic1, delete1;
    private ImageView bpic1, delete2;
    private int userID;

    // Movies json url
    private String url;
    private int recordID;
    private ProgressDialog pDialog;
    private List<CarAttachments> reviewlist = new ArrayList<>();
    private ListView listView;
    private CustomListCarAttachmentAdapter adapter;
    private SessionHandler session;

    public ViewCarAttachmentsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile_attachments, container, false);
        reviewlist.clear();

        //ListView
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CustomListCarAttachmentAdapter(getActivity(), reviewlist);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        //Get UserID
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();

        CarRecord id = session.getRecordID();
        recordID = id.getRecordID();

        UrlBean urlBean = new UrlBean();
        String url = urlBean.getViewcardocuments()+recordID;
        final String getPic = urlBean.getCarAttachmentPicUrl();

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
                        CarAttachments movie = new CarAttachments();
                        movie.setAttachment(getPic+obj.getString("attachment"));
                        movie.setCarAttachID(obj.getInt("carAttachID"));
                        movie.setDateAdded(obj.getString("dateAdded"));
                        movie.setCarID(obj.getInt("carID"));
                        movie.setAttachmentType(obj.getString("attachmentType"));
                        // adding movie to movies array
                        reviewlist.add(movie);

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
