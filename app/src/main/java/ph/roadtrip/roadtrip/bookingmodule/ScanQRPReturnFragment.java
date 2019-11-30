package ph.roadtrip.roadtrip.bookingmodule;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.joda.time.Hours;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.updateData;
import ph.roadtrip.roadtrip.R;
import android.util.Log;

public class ScanQRPReturnFragment extends Fragment {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_BOOKING_ID  = "bookingID";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_TOTAL_PENALTY = "totalPenalty";

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    Button next;
    private SessionHandler session;
    private DrawerLayout drawer;
    private Intent load;
    private String bookingID;
    private String endDate;
    private int hoursLate;
    private Double totalPenalty;
    private String getEndDateLink, insertPenaltyURL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scan_bar_code, container, false);

        surfaceView = view.findViewById(R.id.camerapreview);
        textView = view.findViewById(R.id.textView);

        UrlBean url = new UrlBean();
        getEndDateLink = url.getGetenddate();
        insertPenaltyURL = url.getInsertpenaltyURL();

        barcodeDetector = new BarcodeDetector.Builder(getActivity().getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(getActivity().getApplicationContext(), barcodeDetector).setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try{
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if(qrCodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            insert();
                            cameraSource.stop();
                        }
                    });
                }
            }
        });

        return view;
    }

    public void insert(){

        String link = textView.getText().toString();

        String[] temp;
        String delimiter = " ";

        temp = link.split(delimiter);
        String userID = temp[0];
        String latitude = temp[1];
        String longitude = temp[2];
        bookingID = temp[3];

        getEndDateTime();

        UrlBean getUrl = new UrlBean();
        String url = getUrl.getScan_return_qr() + userID + "&currentLat=" + latitude + "&currentLong=" + longitude + "&bookingID=" + bookingID;
        new updateData().execute(url);

        //Email Receipt to renter
        String mail = getUrl.getMail_receipt() + bookingID;
        new updateData().execute(mail);

        //Email Receipt to owner
        String mail2 = getUrl.getMail_receipt_owner() + bookingID;
        new updateData().execute(mail2);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new SuccessQRFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    public void insertPenalty(){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_BOOKING_ID, bookingID);
            request.put(KEY_TOTAL_PENALTY, totalPenalty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, insertPenaltyURL, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                Log.i("********", "Success Inserting Penalty");
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

    public void getEndDateTime(){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_BOOKING_ID, bookingID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getEndDateLink, request, new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                endDate = response.getString(KEY_END_DATE);

                                //Convert DateTime
                                final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date current = new Date();
                                final String currentDateFinal = formatter.format(current);

                                DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                DateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                Date date = null;
                                Date date2 = null;

                                try {
                                    date = inFormat.parse(endDate);
                                    date2 = inFormat.parse(currentDateFinal);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (date != null && date2 != null) {
                                        //hoursLate = Math.toIntExact(getDifferenceHours(date2, date));

                                        DateTime dateTime = new DateTime(date);
                                        DateTime dateTime2 = new DateTime(date2);
                                        hoursLate = Hours.hoursBetween(dateTime, dateTime2).getHours() % 24;

                                        if(hoursLate < 1){
                                            hoursLate = 0;
                                        } else {
                                            totalPenalty = (double)hoursLate*200;
                                            insertPenalty();
                                        }
                                    }


                                Log.i("********", "End Date: " + String.valueOf(date));
                                Log.i("********", "Current Date: " + String.valueOf(date2));
                                Log.i("********", "Hours Late: " + String.valueOf(hoursLate));

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
