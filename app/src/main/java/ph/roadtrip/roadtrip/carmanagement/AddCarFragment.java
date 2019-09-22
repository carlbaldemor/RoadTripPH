package ph.roadtrip.roadtrip.carmanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ph.roadtrip.roadtrip.classes.EndPoints;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.R;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class AddCarFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


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

    Spinner brandSpinner, modelSpinner, colorSpinner, yearSpinner, serviceSpinner;
    ArrayAdapter<String> dataAdapter2;
    private ImageView pic1, pic2, pic3, pic4, pic5, pic6;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private Button search, addCar, search2;
    private EditText tf_location, etPlateNumber, etChassisNumber, etAmount, tf_location2;
    private ProgressDialog pDialog;
    private String addCarLink;
    private SessionHandler session;
    private String username;
    private Bitmap img1, img2, img3, img4, img5, img6;
    private boolean validPic1, validPic2, validPic3, validPic4, validPic5, validPic6;
    private String user_status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car, container, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }

        //Add Car Link
        UrlBean url = new UrlBean();
        addCarLink = url.getAddCarLink();

        //Get Username of user
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        username = user.getUsername();
        user_status = user.getStatus();

        CarRecord user2 = session.getOwnerID();
        ownerID = user2.getOwnerID();

        addCar = view.findViewById(R.id.btnAddCar);

        if (!user_status.equalsIgnoreCase("Activated")){
            addCar.setVisibility(GONE);
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_car);
        mapFragment.getMapAsync(this);

        colorSpinner = (Spinner) view.findViewById(R.id.ColorSpinner);
        yearSpinner = (Spinner) view.findViewById(R.id.YearSpinner);
        serviceSpinner = (Spinner) view.findViewById(R.id.serviceSpinner);

        brandSpinner = (Spinner) view.findViewById(R.id.SpinnerFeedbackType);
        String size = brandSpinner.getSelectedItem().toString();

        modelSpinner = (Spinner) view.findViewById(R.id.ModelSpinner);
        String size2 = modelSpinner.getSelectedItem().toString();

        search = view.findViewById(R.id.B_search);
        search2 = view.findViewById(R.id.B_search2);
        tf_location = view.findViewById(R.id.TF_LOCATION);
        tf_location2 = view.findViewById(R.id.TF_LOCATION2);
        pic1 = view.findViewById(R.id.pic1);
        pic2 = view.findViewById(R.id.pic2);
        pic3 = view.findViewById(R.id.pic3);
        pic4 = view.findViewById(R.id.pic4);
        pic5 = view.findViewById(R.id.pic5);
        pic6 = view.findViewById(R.id.pic6);
        etPlateNumber = view.findViewById(R.id.etPlateNumber);
        etChassisNumber = view.findViewById(R.id.etChassisNumber);
        etAmount = view.findViewById(R.id.etAmount);

        List<String> carBrand = Arrays.asList(getResources().getStringArray(R.array.car_brand));

        //creating and setting adapter to first spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, carBrand);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(dataAdapter1);
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                int spinner_pos = brandSpinner.getSelectedItemPosition();
                String[] size_values = getResources().getStringArray(R.array.car_brand_values);
                int size = Integer.valueOf(size_values[spinner_pos]);

                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (size) {
                    case 1:
                        setadapter(0);

                        break;
                    case 2:
                        setadapter(1);
                        break;
                    case 3:
                        setadapter(2);
                        break;
                    case 4:
                        setadapter(3);
                        break;
                    case 5:
                        setadapter(4);
                        break;
                    case 6:
                        setadapter(5);
                        break;
                    case 7:
                        setadapter(6);
                        break;
                    case 8:
                        setadapter(7);
                        break;
                    case 9:
                        setadapter(8);
                        break;
                    case 10:
                        setadapter(9);
                        break;
                    case 11:
                        setadapter(10);
                        break;
                    case 12:
                        setadapter(11);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

                            Toast.makeText(getActivity().getApplicationContext(), "Latitude: " + myAddress.getLatitude() + " Longitude: " + myAddress.getLongitude(), Toast.LENGTH_LONG).show();
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

                            Toast.makeText(getActivity().getApplicationContext(), "Latitude: " + myAddress.getLatitude() + " Longitude: " + myAddress.getLongitude(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

        //Open Gallery
        view.findViewById(R.id.pic1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        view.findViewById(R.id.pic2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 200);
            }
        });
        view.findViewById(R.id.pic3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 300);
            }
        });
        view.findViewById(R.id.pic4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 400);
            }
        });
        view.findViewById(R.id.pic5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 500);
            }
        });
        view.findViewById(R.id.pic6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 600);
            }
        });

        view.findViewById(R.id.btnAddCar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                color = colorSpinner.getSelectedItem().toString();
                year = yearSpinner.getSelectedItem().toString();
                serviceType = serviceSpinner.getSelectedItem().toString();
                plateNumber = etPlateNumber.getText().toString().trim();
                chassisNumber = etChassisNumber.getText().toString().trim();
                amount = etAmount.getText().toString().trim();

                int spinner_pos;
                String[] size_values;
                int size;

                switch (model_pos){
                    case 0:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.toyota_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 1:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.mitsubishu_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 2:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.hyundai_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 3:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.honda_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 4:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.ford_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 5:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.isuzu_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 6:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.nissan_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 7:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.suzuki_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 8:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.chevrolet_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 9:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.mazda_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 10:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.kia_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                    case 11:
                        spinner_pos = modelSpinner.getSelectedItemPosition();
                        size_values = getResources().getStringArray(R.array.tata_models_values);
                        size = Integer.valueOf(size_values[spinner_pos]);
                        modelID = size;
                        break;
                }

                process();
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

                                recordID = response.getString(KEY_RECORD_ID);


                                if (validPic6 == true){
                                    uploadBitmap(img6);
                                }
                                if (validPic5 == true){
                                    uploadBitmap(img5);
                                }
                                if (validPic4 == true){
                                    uploadBitmap(img4);
                                }
                                if (validPic3 == true){
                                    uploadBitmap(img3);
                                }
                                if (validPic2 == true){
                                    uploadBitmap(img2);
                                }
                                if (validPic1 == true){
                                    uploadBitmap(img1);
                                }
                                Toast.makeText(getActivity().getApplicationContext(),
                                       recordID, Toast.LENGTH_SHORT).show();
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


    private void setadapter(int a) {
        List<String> toyotaModels = Arrays.asList(getResources().getStringArray(R.array.toyota_models));
        List<String> mitsubishiModels = Arrays.asList(getResources().getStringArray(R.array.mitsubishi_models));
        List<String> hyundaiModels = Arrays.asList(getResources().getStringArray(R.array.hyundai_models));
        List<String> hondaModels = Arrays.asList(getResources().getStringArray(R.array.honda_models));
        List<String> fordModels = Arrays.asList(getResources().getStringArray(R.array.ford_models));
        List<String> isuzuModels = Arrays.asList(getResources().getStringArray(R.array.isuzu_models));
        List<String> nissanModels = Arrays.asList(getResources().getStringArray(R.array.nissan_models));
        List<String> suzukiModels = Arrays.asList(getResources().getStringArray(R.array.suzuki_models));
        List<String> chevroletModels = Arrays.asList(getResources().getStringArray(R.array.chevrolet_models));
        List<String> mazdaModels = Arrays.asList(getResources().getStringArray(R.array.mazda_models));
        List<String> kiaModels = Arrays.asList(getResources().getStringArray(R.array.kia_models));
        List<String> tataModels = Arrays.asList(getResources().getStringArray(R.array.tata_models));

        int spinner_pos;
        String[] size_values;
        int size;
        //Here we are checking which list to display in second spinner
        switch (a) {
            case 0://this is the case of India
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, toyotaModels);
                model_pos = 0;
                break;
            case 1://this is the case of USA
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, mitsubishiModels);
                model_pos = 1;
                break;
            case 2://this is the case of India
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, hyundaiModels);
                model_pos = 2;
                break;
            case 3://this is the case of USA
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, hondaModels);
                model_pos = 3;
                break;
            case 4://this is the case of India
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, fordModels);
                model_pos = 4;
                break;
            case 5://this is the case of USA
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, isuzuModels);
                model_pos = 5;
                break;
            case 6://this is the case of India
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, nissanModels);
                model_pos = 6;
                break;
            case 7://this is the case of USA
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, suzukiModels);
                model_pos = 7;
                break;
            case 8://this is the case of India
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, chevroletModels);
                model_pos = 8;
                break;
            case 9://this is the case of USA
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, mazdaModels);
                model_pos = 9;
                break;
            case 10://this is the case of USA
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, kiaModels);
                model_pos = 10;
                break;
            case 11://this is the case of USA
                dataAdapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_ui, tataModels);
                model_pos = 11;
                break;
        }
        //Here we are setting the second adapter to our second spinner
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(dataAdapter2);
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

        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
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
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                pic1.setImageBitmap(bitmap);

                //Check this if user placed a picture
                validPic1 = true;
                img1 = bitmap;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                pic2.setImageBitmap(bitmap2);
                //Check this if user placed a picture
                validPic2 = true;
                img2 = bitmap2;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 300 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap3 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                pic3.setImageBitmap(bitmap3);
                //Check this if user placed a picture
                validPic3 = true;
                img3 = bitmap3;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 400 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap4 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                pic4.setImageBitmap(bitmap4);
                //Check this if user placed a picture
                validPic4 = true;
                img4 = bitmap4;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 500 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap5 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                pic5.setImageBitmap(bitmap5);
                //Check this if user placed a picture
                validPic5 = true;
                img5 = bitmap5;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 600 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap6 = MediaStore.Images.Media.getBitmap(this.getActivity().getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                pic6.setImageBitmap(bitmap6);
                //Check this if user placed a picture
                validPic6 = true;
                img6 = bitmap6;

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = recordID;

        //our custom volley request
        VolleyMultipartRequestCarMan volleyMultipartRequestCarMan = new VolleyMultipartRequestCarMan(Request.Method.POST, EndPoints.UPLOAD_CARPICS_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getActivity().getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(volleyMultipartRequestCarMan);
    }
}

