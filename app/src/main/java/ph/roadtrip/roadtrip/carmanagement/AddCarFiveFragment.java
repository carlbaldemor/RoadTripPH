package ph.roadtrip.roadtrip.carmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.bookingmodule.SuccessAddCarFragment;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

import static android.app.Activity.RESULT_OK;

public class AddCarFiveFragment extends Fragment {

    private static final String KEY_STATUS = "status1";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_MODEL_ID = "modelID";
    private static final String KEY_COLOR = "color";
    private static final String KEY_YEAR = "year";
    private static final String KEY_PLATE_NUMBER = "plateNumber";
    private static final String KEY_CHASSIS_NUMBER = "chassisNumber";
    private static final String KEY_LAT_RETURN = "latReturn";
    private static final String KEY_LONG_RETURN = "longReturn";
    private static final String KEY_LAT_ISSUE = "latIssue";
    private static final String KEY_LONG_ISSUE = "longIssue";
    private static final String KEY_SERVICE_TYPE = "serviceType";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_RECORD_STATUS = "status";
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_NAME = "modelName";

    private int ownerID;
    private int modelID;
    private String recordID;
    private String color;
    private String year;
    private String plateNumber;
    private String chassisNumber;
    private String latIssue;
    private String longIssue;
    private String latReturn;
    private String longReturn;
    private String serviceType;
    private String amount;
    private int model_pos;

    private ImageView apic1, bpic1;
    public static final int REQUEST_LOCATION_CODE = 99;
    private Button btnConfirm;
    private SessionHandler session;
    private String username;
    private Bitmap img1, img2;
    private boolean validPic1, validPic2;
    private String user_status;
    private TextView tvBrandName, tvModel, tvColor, tvYear,
            tvPlateNumber, tvChassisNumber, tvReturn, tvPickup, tvServiceType, tvPrice;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car_page_five, container, false);

        //Retrieve the value
        color = getArguments().getString("color");
        year = getArguments().getString("year");
        modelID = getArguments().getInt("modelID");
        plateNumber = getArguments().getString("plateNumber");
        chassisNumber = getArguments().getString("chassisNumber");
        latIssue = getArguments().getString("latIssue");
        longIssue = getArguments().getString("longIssue");
        latReturn = getArguments().getString("latReturn");
        longReturn = getArguments().getString("longReturn");
        serviceType = getArguments().getString("serviceType");
        amount = getArguments().getString("amount");


        //Get Username of user
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        username = user.getUsername();
        user_status = user.getStatus();

        CarRecord user2 = session.getOwnerID();
        ownerID = user2.getOwnerID();

        apic1 = view.findViewById(R.id.apic1);
        bpic1 = view.findViewById(R.id.bpic1);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        /*
        private TextView tvBrandName, tvModel, tvColor, tvYear,
            tvPlateNumber, tvChassisNumber, tvReturn, tvPickup, tvServiceType, tvPrice;
         */

        tvBrandName = view.findViewById(R.id.tvBrandName);
        tvModel = view.findViewById(R.id.tvModel);
        tvColor = view.findViewById(R.id.tvColor);
        tvYear = view.findViewById(R.id.tvYear);
        tvPlateNumber = view.findViewById(R.id.tvPlateNumber);
        tvChassisNumber = view.findViewById(R.id.tvChassisNumber);
        tvReturn = view.findViewById(R.id.tvReturn);
        tvPickup = view.findViewById(R.id.tvPickup);
        tvServiceType = view.findViewById(R.id.tvServiceType);
        tvPrice = view.findViewById(R.id.tvPrice);

        getBrandName();


        Geocoder geocoder;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latIssue), Double.parseDouble(longIssue), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Address> addresses2 = null;
        try {
            addresses2 = geocoder.getFromLocation(Double.parseDouble(latReturn), Double.parseDouble(longReturn), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String returnAdd = addresses2.get(0).getAddressLine(0);
        String pickupAdd = addresses.get(0).getAddressLine(0);


        tvColor.setText(color);
        tvYear.setText(year);
        tvPlateNumber.setText(plateNumber);
        tvChassisNumber.setText(chassisNumber);
        tvReturn.setText(returnAdd);
        tvPickup.setText(pickupAdd);
        tvServiceType.setText(serviceType);
        tvPrice.setText(amount);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process();
            }
        });

        return view;
    }

    public void process(){

        //Add Car Link
        UrlBean url = new UrlBean();
        String addCarLink = url.getAddCarLink();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_OWNER_ID, ownerID);
            request.put(KEY_MODEL_ID, modelID);
            request.put(KEY_COLOR, color);
            request.put(KEY_YEAR, year);
            request.put(KEY_PLATE_NUMBER, plateNumber);
            request.put(KEY_CHASSIS_NUMBER, chassisNumber);
            request.put(KEY_LAT_ISSUE, latIssue);
            request.put(KEY_LONG_ISSUE, longIssue);
            request.put(KEY_LAT_RETURN, latReturn);
            request.put(KEY_LONG_RETURN, longReturn);
            request.put(KEY_SERVICE_TYPE, serviceType);
            request.put(KEY_AMOUNT, amount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, addCarLink, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                //Inflate the fragment
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new SuccessAddCarFragment());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();


                            }else{
                                Toast.makeText(getActivity().getApplicationContext(),
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
                        Toast.makeText(getActivity().getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }

    public void getBrandName(){
        UrlBean url = new UrlBean();
        String getBrandName = url.getGetBrandName();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_MODEL_ID, modelID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getBrandName, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                tvBrandName.setText(response.getString(KEY_BRAND_NAME));
                                tvModel.setText(response.getString(KEY_MODEL_NAME));

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
