package ph.roadtrip.roadtrip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ph.roadtrip.roadtrip.bookingmodule.ViewBookingOfferActivity;
import ph.roadtrip.roadtrip.carmanagement.ListCarsFragment;
import ph.roadtrip.roadtrip.classes.CustomInfoWindowAdapter;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private String url;
    private Context context;
    String recLat;
    String recLong;
    String recLat2;
    String recLong2;
    String brandName;
    String modelName;
    String amount;
    String carType;
    String address;
    String address2;
    String status;
    String recordPicture;
    String sdate;
    String edate;
    int recordID;
    private Button viewBooking;
    private String startDate, endDate, getCarType, serviceType, lblStart, lblEnd;

    private static final String KEY_RECORD_ID = "recordID";
    // Log tag
    private static final String TAG = ListCarsFragment.class.getSimpleName();
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_book_service);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_maps, null, false);
        drawer.addView(contentView, 0);

        viewBooking = (Button)findViewById(R.id.btnViewBooking);

        // Get the transferred data from source activity.
        Intent intent = getIntent();
        startDate = getIntent().getExtras().getString("KEY_START_DATE");
        endDate = getIntent().getExtras().getString("KEY_END_DATE");
        getCarType = getIntent().getExtras().getString("KEY_CAR_TYPE");
        serviceType = getIntent().getExtras().getString("KEY_SERVICE_TYPE");
        sdate = getIntent().getExtras().getString("KEY_START");
        edate = getIntent().getExtras().getString("KEY_END");
        lblStart = getIntent().getExtras().getString("KEY_LBL_START");
        lblEnd = getIntent().getExtras().getString("KEY_LBL_END");

        UrlBean getUrl = new UrlBean();
        url = getUrl.getListCarsMaps()+startDate+"&endDate="+endDate+"&serviceType="+serviceType+"&carType="+getCarType;



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //PERMISSION GRANTED
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    //if permission is denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
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

        final CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MapsActivity.this,brandName,modelName,address);
        mMap.setInfoWindowAdapter(adapter);

        // Creating volley request obj
        final JsonArrayRequest movieReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {


                        MarkerOptions mo =  new MarkerOptions();
                        JSONObject obj = response.getJSONObject(i);
                        recLat = obj.getString("latIssue");
                        recLong = obj.getString("longIssue");
                        recLat = obj.getString("latReturn");
                        recLong = obj.getString("longReturn");
                        brandName = obj.getString("brandName");
                        modelName = obj.getString("modelName");
                        amount = obj.getString("amount");
                        carType = obj.getString("carType");
                        status = obj.getString("status");
                        recordPicture = obj.getString("recordPicture");
                        recordID = obj.getInt("recordID");

                        LatLng latLng = new LatLng(Double.parseDouble(recLat), Double.parseDouble(recLong));

                        Geocoder geocoder;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(recLat), Double.parseDouble(recLong), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        List<Address> addresses2 = null;
                        try {
                            addresses2 = geocoder.getFromLocation(Double.parseDouble(recLat), Double.parseDouble(recLong), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        address2 = addresses2.get(0).getAddressLine(0);

                        mo.position(latLng);
                        mo.title(brandName + " " + modelName + " " + amount);

                        address = addresses.get(0).getAddressLine(0);
                        Marker marker = null;
                        if (carType.equals("Sedan")){
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(latLng).title(brandName + " " + modelName + " (P" +  amount  + ")")
                                    .snippet("Car Type: " + carType+"\n"+"\n"+"Pickup: " + address +"\n"+ "Return: " + address2)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_sedan))));
                            marker.setTag(recordID);
                        } else if (carType.equals("Hatchback")){
                            marker = mMap.addMarker(new MarkerOptions()
                                    .position(latLng).title(String.valueOf(recordID) +  " " + brandName + " " + modelName + " (P" + amount + ")")
                                    .snippet("Car Type: " + carType+"\n"+"\n"+"Pickup: " + address +"\n"+ "Return: " + address2)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_hatchback))));
                            marker.setTag(recordID);
                        } else if (carType.equals("MPV")){
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng).title(brandName + " " + modelName + " (P" + amount + ")")
                                    .snippet("Car Type: " + carType+"\n"+"\n"+"Pickup: " + address +"\n"+ "Return: " + address2)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_mpv))));

                        } else if (carType.equals("SUV")){
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng).title(String.valueOf(recordID ) +  " " + brandName + " " + modelName + " (P" + amount + ")")
                                    .snippet("Car Type: " + carType+"\n"+"\n"+"Pickup: " + address +"\n"+ "Return: " + address2)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_suv))));

                        } else if (carType.equals("Pickup")){
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng).title(brandName + " " + modelName + " (P" + amount + ")")
                                    .snippet("Car Type: " + carType+"\n"+"\n"+"Pickup: " + address +"\n"+ "Return: " + address2)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_pickup))));
                        }

                        mMap.setInfoWindowAdapter(adapter);
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent i = new Intent(MapsActivity.this, ViewBookingOfferActivity.class);
                                //String record = marker.getTitle();
                                String record = String.valueOf(marker.getTag());
                                i.putExtra("record", record);
                                i.putExtra("KEY_START", sdate);
                                i.putExtra("KEY_END", edate);
                                i.putExtra("KEY_RECORD_ID", recordID);
                                i.putExtra("KEY_START_DATE", startDate);
                                i.putExtra("KEY_END_DATE", endDate);
                                i.putExtra("KEY_SERVICE_TYPE", serviceType);
                                i.putExtra("KEY_ADDRESS", address);
                                i.putExtra("KEY_LBL_START", lblStart);
                                i.putExtra("KEY_LBL_END", lblEnd);
                                startActivity(i);
                            }
                        });

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                LinearLayout info = new LinearLayout(MapsActivity.this);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(MapsActivity.this);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(MapsActivity.this);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Intent i = new Intent(MapsActivity.this, ViewBookingOfferActivity.class);
                                        i.putExtra("KEY_START", sdate);
                                        i.putExtra("KEY_END", edate);
                                        i.putExtra("KEY_RECORD_ID", recordID);
                                        i.putExtra("KEY_START_DATE", startDate);
                                        i.putExtra("KEY_END_DATE", endDate);
                                        i.putExtra("KEY_SERVICE_TYPE", serviceType);
                                        i.putExtra("KEY_ADDRESS", address);
                                        startActivity(i);
                                    }
                                });*/

                                return info;
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(movieReq);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    public static String DoFormat(double myNumber)
    {
        return String.format("{0:0.00}", myNumber).replace(".00","");
    }


    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public void onClick(View v) throws JSONException {
        if(v.getId() == R.id.B_search){
            EditText tf_location = (EditText)findViewById(R.id.TF_LOCATION);
            String location = tf_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo =  new MarkerOptions();

            if(!location.equals("")){
                Geocoder geocoder = new Geocoder(this);
                try{
                    addressList = geocoder.getFromLocationName(location, 5);
                }catch (IOException e){
                    e.printStackTrace();
                }

                for(int i = 0; i<addressList.size(); i++){
                    Address myAddress = addressList.get(i);
                    LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    mo.position(latLng);
                    mo.title("Searched Location");
                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(this)
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }


    }

    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }

            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
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
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

}
