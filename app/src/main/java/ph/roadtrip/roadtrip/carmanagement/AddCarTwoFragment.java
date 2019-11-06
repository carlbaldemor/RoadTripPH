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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.LatLngBounds;
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

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.EndPoints;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class AddCarTwoFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


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

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private Button search, btnNext, search2;
    private EditText tf_location, tf_location2;
    private SessionHandler session;
    private String username;
    private String user_status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car_page_two, container, false);

        //Retrieve the value
        color = getArguments().getString("color");
        year = getArguments().getString("year");
        modelID = getArguments().getInt("modelID");
        plateNumber = getArguments().getString("plateNumber");
        chassisNumber = getArguments().getString("chassisNumber");



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }



        //Get Username of user
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        username = user.getUsername();
        user_status = user.getStatus();

        CarRecord user2 = session.getOwnerID();
        ownerID = user2.getOwnerID();



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_car);
        mapFragment.getMapAsync(this);

        search = view.findViewById(R.id.B_search);
        search2 = view.findViewById(R.id.B_search2);
        tf_location = view.findViewById(R.id.TF_LOCATION);
        tf_location2 = view.findViewById(R.id.TF_LOCATION2);
        btnNext = view.findViewById(R.id.btnNext);




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
                        }catch (IOException e){
                            e.printStackTrace();
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
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Put the value
                AddCarThreeFragment ldf = new AddCarThreeFragment ();
                Bundle args = new Bundle();
                args.putString("color", color);
                args.putString("year", year);
                args.putInt("modelID", modelID);
                args.putString("plateNumber", plateNumber);
                args.putString("chassisNumber", chassisNumber);
                args.putString("latIssue", latIssue);
                args.putString("longIssue", longIssue);
                args.putString("latReturn", latReturn);
                args.putString("longReturn", longReturn);
                ldf.setArguments(args);

                //Inflate the fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, ldf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        return view;
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

        LatLngBounds PHILIPPINES = new LatLngBounds(
                new LatLng(14.599512, 120.984222), new LatLng(14.599512, 120.984222));

        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(PHILIPPINES, 0));
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

