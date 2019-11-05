package ph.roadtrip.roadtrip.carmanagement;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.R;

public class ViewRecordFragment extends android.support.v4.app.Fragment {
    private SessionHandler session;
    private static final String KEY_STATUS ="status1";
    private static final String KEY_MESSAGE ="message";
    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_LONG_ISSUE = "longIssue";
    private static final String KEY_LAT_ISSUE = "latIssue";
    private static final String KEY_LONG_RETURN = "longReturn";
    private static final String KEY_LAT_RETURN = "latReturn";
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CAR_TYPE = "carType";
    private static final String KEY_MODEL_YEAR = "modelYear";
    private static final String KEY_COLOR = "color";
    private static final String KEY_SERVICE_TYPE = "serviceType";
    private static final String KEY_PROF_PIC = "profilePicture";

    private int ownerID;
    private int recordID;
    private String longIssue;
    private String latIssue;
    private String longReturn;
    private String latReturn;
    private String brandName;
    private String modelName;
    private String pickupAddress;
    private String returnAddress;
    private Double amount;
    private String carType;
    private String modelYear;
    private String color;
    private String url;
    private String serviceType;

    private TextView tvBrand, tvModel, tvColor, tvModelYear, tvCarType, tvServiceType, tvAmount, tvPickup, tvReturn;
    private Button btnEditRecord;

    public ViewRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_carman, container, false);

        //Set Views
        tvBrand = view.findViewById(R.id.tvBrand);
        tvModel = view.findViewById(R.id.tvModel);
        tvColor  = view.findViewById(R.id.tvColor);
        tvModelYear = view.findViewById(R.id.tvModelYear);
        tvCarType = view.findViewById(R.id.tvCarType);
        tvServiceType = view.findViewById(R.id.tvServiceType);
        tvAmount = view.findViewById(R.id.tvAmount);
        btnEditRecord = view.findViewById(R.id.btnEditRecord);
        tvPickup = view.findViewById(R.id.tvPickup);
        tvReturn = view.findViewById(R.id.tvReturn);

        UrlBean getUrl = new UrlBean();
        url = getUrl.getFetch_booking_data();


        getData();

        btnEditRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new EditCarRecordFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;

    }

    public void getData(){
        session = new SessionHandler(getActivity().getApplicationContext());

        CarRecord carRecord = session.getRecordID();
        recordID = carRecord.getRecordID();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_RECORD_ID, recordID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                latIssue = response.getString(KEY_LAT_ISSUE);
                                longIssue = response.getString(KEY_LONG_ISSUE);
                                latReturn = response.getString(KEY_LAT_RETURN);
                                longReturn = response.getString(KEY_LONG_RETURN);
                                brandName = response.getString(KEY_BRAND_NAME);
                                modelName = response.getString(KEY_MODEL_NAME);
                                amount = response.getDouble(KEY_AMOUNT);
                                carType = response.getString(KEY_CAR_TYPE);
                                color = response.getString(KEY_COLOR);
                                modelYear = response.getString(KEY_MODEL_YEAR);
                                serviceType = response.getString(KEY_SERVICE_TYPE);

                                Geocoder geocoder;
                                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(Double.parseDouble(latIssue), Double.parseDouble(longIssue), 1);
                                    pickupAddress = addresses.get(0).getAddressLine(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                List<Address> addresses2 = null;
                                try {
                                    addresses2 = geocoder.getFromLocation(Double.parseDouble(latReturn), Double.parseDouble(longReturn), 1);
                                    returnAddress = addresses2.get(0).getAddressLine(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                tvBrand.setText(brandName);
                                tvModel.setText(modelName);
                                tvColor.setText(color);
                                tvModelYear.setText(modelYear);
                                tvCarType.setText(carType);
                                tvServiceType.setText(serviceType);
                                tvPickup.setText(pickupAddress);
                                tvReturn.setText(returnAddress);
                                tvAmount.setText("₱" + String.valueOf(amount));

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
}
