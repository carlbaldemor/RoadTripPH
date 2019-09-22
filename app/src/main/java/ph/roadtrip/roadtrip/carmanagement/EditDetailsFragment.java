package ph.roadtrip.roadtrip.carmanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.R;

public class EditDetailsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String KEY_STATUS = "status1";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_MODEL_ID = "modelID";
    private static final String KEY_COLOR = "color";
    private static final String KEY_YEAR = "year";
    private static final String KEY_PLATE_NUMBER = "plateNumber";
    private static final String KEY_CHASSIS_NUMBER = "chassisNumber";
    private static final String KEY_LAT_ISSUE = "latIssue";
    private static final String KEY_LONG_ISSUE = "longIssue";
    private static final String KEY_LAT_RETURN = "latReturn";
    private static final String KEY_LONG_RETURN = "longReturn";
    private static final String KEY_SERVICE_TYPE = "serviceType";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_RECORD_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_YEAR = "modelYear";

    private int ownerID = 3;
    private int modelID;
    private int recordID;
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
    private String brandName, modelName, modelYear;
    private int model_pos;
    private int brandPos;
    private int modelPos;

    Spinner brandSpinner, modelSpinner, colorSpinner, yearSpinner, serviceSpinner;
    ArrayAdapter<String> dataAdapter2;
    private ImageView pic1, pic2, pic3, pic4, pic5, pic6;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private Button search, editCar, search2, deleteCar;
    private EditText tf_location, etPlateNumber, etChassisNumber, etAmount, tf_location2;
    private ProgressDialog pDialog;
    private String editCarLink, fetch_booking_data;
    private SessionHandler session;
    private String username, edit_car;
    private Bitmap img1, img2, img3, img4, img5, img6;
    private boolean validPic1, validPic2, validPic3, validPic4, validPic5, validPic6;
    private TextView tvBrand, tvModel, tvColor, tvModelYear, tvPlatenumber, tvChassisNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editdetails_car_record, container, false);
        onBackground();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_car);
        mapFragment.getMapAsync(this);

        //Textviews
        tvBrand = view.findViewById(R.id.tvBrand);
        tvModel = view.findViewById(R.id.tvModel);
        tvColor = view.findViewById(R.id.tvColor);
        tvModelYear = view.findViewById(R.id.tvModelYear);
        tvPlatenumber = view.findViewById(R.id.tvPlateNumber);
        tvChassisNumber = view.findViewById(R.id.tvChassisNumber);

        //Add Car Link
        UrlBean url = new UrlBean();
        //editcar = url.getAddCarLink();
        fetch_booking_data = url.getFetch_booking_data();
        edit_car = url.getEdit_car();

        //Get Username of user
        session = new SessionHandler(getActivity().getApplicationContext());
        CarRecord id = session.getRecordID();
        recordID = id.getRecordID();
        onBackground();

        CarRecord carRecord = session.getSpinnerPos();
        brandPos = carRecord.getBrandPos();
        modelPos = carRecord.getModelPos();

        search = view.findViewById(R.id.B_search);
        search2 = view.findViewById(R.id.B_search2);
        tf_location = view.findViewById(R.id.TF_LOCATION);
        tf_location2 = view.findViewById(R.id.TF_LOCATION2);
        editCar = view.findViewById(R.id.btnEditRecord);
        deleteCar = view.findViewById(R.id.btnDelete);
        etPlateNumber = view.findViewById(R.id.etPlateNumber);
        etChassisNumber = view.findViewById(R.id.etChassisNumber);
        etAmount = view.findViewById(R.id.etAmount);
        serviceSpinner = view.findViewById(R.id.serviceSpinner);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(view.getId() == R.id.B_search){
                    String location = tf_location.getText().toString();
                    List<Address> addressList = null;
                    MarkerOptions mo =  new MarkerOptions();

                    if(!location.equals("")){
                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                        try{
                            addressList = geocoder.getFromLocationName(location, 5);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        for(int i = 0; i<addressList.size(); i++){
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            mo.position(latLng);
                            mo.title("Location");
                            mMap.addMarker(mo);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                            //Assign the location to a variable
                            latIssue = Double.toString(myAddress.getLatitude());
                            longIssue = Double.toString(myAddress.getLongitude());
                        }
                    }
                }

            }
        });

        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(view.getId() == R.id.B_search2){
                    String location = tf_location2.getText().toString();
                    List<Address> addressList = null;
                    MarkerOptions mo =  new MarkerOptions();

                    if(!location.equals("")){
                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                        try{
                            addressList = geocoder.getFromLocationName(location, 5);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        for(int i = 0; i<addressList.size(); i++){
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            mo.position(latLng);
                            mo.title("Location");
                            mMap.addMarker(mo);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                            //Assign the location to a variable
                            latReturn = Double.toString(myAddress.getLatitude());
                            longReturn = Double.toString(myAddress.getLongitude());
                        }
                    }
                }

            }
        });

        view.findViewById(R.id.btnEditRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serviceType = serviceSpinner.getSelectedItem().toString();
                amount = etAmount.getText().toString().trim();

                process();
            }
        });

        view.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new PromptDeleteRecordFragment());
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    /**
     * Display Progress bar while registering
     */


    public void process(){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_LAT_ISSUE, latIssue);
            request.put(KEY_LONG_ISSUE, longIssue);
            request.put(KEY_LAT_RETURN, latReturn);
            request.put(KEY_LONG_RETURN, longReturn);
            request.put(KEY_SERVICE_TYPE, serviceType);
            request.put(KEY_AMOUNT, amount);
            request.put(KEY_RECORD_ID, recordID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, edit_car, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Go to Success Page
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new CarManagementFragment());
                                fragmentTransaction.commit();
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Failed to Edit Car Record", Toast.LENGTH_SHORT).show();
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

    public void onBackground(){

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_RECORD_ID, recordID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, fetch_booking_data, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                brandName = response.getString(KEY_BRAND_NAME);
                                modelName = response.getString(KEY_MODEL_NAME);
                                modelYear = response.getString(KEY_MODEL_YEAR);
                                color = response.getString(KEY_COLOR);
                                plateNumber = response.getString(KEY_PLATE_NUMBER);
                                chassisNumber = response.getString(KEY_CHASSIS_NUMBER);

                                tvBrand.setText(brandName);
                                tvModel.setText(modelName);
                                tvModelYear.setText(modelYear);
                                tvColor.setText(color);
                                tvPlatenumber.setText(plateNumber);
                                tvChassisNumber.setText(chassisNumber);

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


                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsArrayRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //PERMISSION GRANTED
                    if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    //if permission is denied
                    Toast.makeText(getActivity().getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

 /*       if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);*/
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void onClick(View view){
        if(view.getId() == R.id.B_search){
            EditText tf_location = view.findViewById(R.id.TF_LOCATION);
            String location = tf_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo =  new MarkerOptions();

            if(!location.equals("")){
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
                try{
                    addressList = geocoder.getFromLocationName(location, 5);
                }catch (IOException e){
                    e.printStackTrace();
                }

                for(int i = 0; i<addressList.size(); i++){
                    Address myAddress = addressList.get(i);
                    LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    mo.position(latLng);
                    mo.title("Toyota Vios Red Carl Baldemor");
                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(5));

        if(client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }


    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }

            else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

