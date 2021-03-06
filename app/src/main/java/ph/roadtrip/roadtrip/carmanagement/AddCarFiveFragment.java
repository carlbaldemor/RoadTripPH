package ph.roadtrip.roadtrip.carmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.bookingmodule.SuccessAddCarFragment;
import ph.roadtrip.roadtrip.classes.CarPictures;
import ph.roadtrip.roadtrip.classes.EndPoints;
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
    private static final String KEY_CAR_ID = "carID";

    private int ownerID;
    private int modelID;
    private String carID;
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

    public static final int REQUEST_LOCATION_CODE = 99;
    private ProgressDialog pDialog;
    private Button btnConfirm;
    private SessionHandler session;
    private String username;
    private Bitmap img1, img2;
    private boolean validPic1, validPic2;
    private String user_status;
    private String pickupAdd, returnAdd;
    private TextView tvBrandName, tvModel, tvColor, tvYear,
            tvPlateNumber, tvChassisNumber, tvReturn, tvPickup, tvServiceType, tvPrice;
    private String str1 = null, str2 = null, str3 = null, str4 = null, str5 = null, str6 = null, imgOR = null, imgCR = null, imgSIR = null;
    private boolean str11, str22, str33, str44, str55, str66, imgORR, imgCRR, imgSIRR;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car_page_five, container, false);
        session = new SessionHandler(getActivity().getApplicationContext());
        CarPictures carPictures = new CarPictures();


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
        CarPictures car1 = session.getCarPic1();
        if (car1 != null){
            str1 = car1.getCarPic1();
            str11 = true;
        }
        CarPictures car2 = session.getCarPic2();
        if (car2 != null){
            str2 = car2.getCarPic2();
            str22 = true;
        }
        CarPictures car3 = session.getCarPic3();
        if (car3 != null){
            str3 = car3.getCarPic3();
            str33 = true;
        }
        CarPictures car4 = session.getCarPic4();
        if (car4 != null){
            str4 = car4.getCarPic4();
            str44 = true;
        }
        CarPictures car5 = session.getCarPic5();
        if (car5 != null){
            str5 = car5.getCarPic5();
            str55 = true;
        }
        CarPictures car6 = session.getCarPic6();
        if (car6 != null){
            str6 = car6.getCarPic6();
            str66 = true;
        }
        CarPictures carOR = session.getCarOR();
        if (carOR != null){
            imgOR = carOR.getCarOR();
            imgORR = true;
        }
        CarPictures carCR = session.getCarCR();
        if (carCR != null){
            imgCR = carCR.getCarCR();
            imgCRR = true;
        }
        CarPictures carSIR = session.getCarSIR();
        if (carSIR != null){
            imgSIR = carSIR.getCarSIR();
            imgSIRR = true;
        }

        //Get Username of user

        User user = session.getUserDetails();
        username = user.getUsername();
        user_status = user.getStatus();

        CarRecord user2 = session.getOwnerID();
        ownerID = user2.getOwnerID();

        btnConfirm = view.findViewById(R.id.btnConfirm);

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

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        getBrandName();

        Geocoder geocoder;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latIssue), Double.parseDouble(longIssue), 1);
            pickupAdd = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Address> addresses2 = null;
        try {
            addresses2 = geocoder.getFromLocation(Double.parseDouble(latReturn), Double.parseDouble(longReturn), 1);
            returnAdd = addresses2.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DecimalFormat df = new DecimalFormat("#,###.00");
        Double amt = Double.parseDouble(amount);
        tvColor.setText(color);
        tvYear.setText(year);
        tvPlateNumber.setText(plateNumber);
        tvChassisNumber.setText(chassisNumber);
        tvReturn.setText(returnAdd);
        tvPickup.setText(pickupAdd);
        tvServiceType.setText(serviceType);
        tvPrice.setText(String.valueOf("₱" + df.format(amt)));
        hidePDialog();
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

                                recordID = response.getString(KEY_RECORD_ID);
                                carID = response.getString(KEY_CAR_ID);

                                if (str11){
                                    uploadBitmap(StringToBitMap(str1));
                                }
                                if (str22){
                                    uploadBitmap(StringToBitMap(str2));
                                }
                                if (str33){
                                    uploadBitmap(StringToBitMap(str3));
                                }
                                if (str44){
                                    uploadBitmap(StringToBitMap(str4));
                                }
                                if (str55){
                                    uploadBitmap(StringToBitMap(str5));
                                }
                                if (str66){
                                    uploadBitmap(StringToBitMap(str6));
                                }
                                if (imgORR){
                                    uploadBitmapOR(StringToBitMap(imgOR));
                                }
                                if (imgCRR){
                                    uploadBitmapCR(StringToBitMap(imgCR));
                                }
                                if(imgSIRR){
                                    uploadBitMapSIR(StringToBitMap(imgSIR));
                                }

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

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
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

    private void uploadBitmapOR(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = carID;

        //our custom volley request
        VolleyMultipartRequestCarMan volleyMultipartRequestCarMan = new VolleyMultipartRequestCarMan(Request.Method.POST, EndPoints.UPLOAD_CAR_ATTACH_URL,
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

    private void uploadBitmapCR(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = carID;

        //our custom volley request
        VolleyMultipartRequestCarMan volleyMultipartRequestCarMan = new VolleyMultipartRequestCarMan(Request.Method.POST, EndPoints.UPLOAD_CAR_ATTACH_CR_URL,
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

    private void uploadBitMapSIR(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = carID;

        //our custom volley request
        VolleyMultipartRequestCarMan volleyMultipartRequestCarMan = new VolleyMultipartRequestCarMan(Request.Method.POST, EndPoints.UPLOAD_CAR_ATTACH_SIR_URL,
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

