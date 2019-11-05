package ph.roadtrip.roadtrip.bookingmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ph.roadtrip.roadtrip.BaseActivity;
import ph.roadtrip.roadtrip.classes.MySingleton;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.User;
import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.transactionhistory.Booking;

import static android.view.View.GONE;


public class ViewBookingOfferActivity extends BaseActivity {

    private static final String KEY_RECORD_ID = "recordID";
    private static final String KEY_STATUS = "status1";
    private static final String KEY_BRAND_NAME = "brandName";
    private static final String KEY_MODEL_NAME = "modelName";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CAR_TYPE = "carType";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_MODEL_YEAR = "modelYear";
    private static final String KEY_COLOR = "color";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PROF_PIC = "profilePicture";
    private static final String KEY_OWNER_ID = "ownerID";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_LONG_ISSUE = "longIssue";
    private static final String KEY_LAT_ISSUE = "latIssue";
    private static final String KEY_LONG_RETURN = "longReturn";
    private static final String KEY_LAT_RETURN = "latReturn";
    private static final String KEY_TOTAL_AMOUNT = "totalAmount";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_END_DATE = "endDate";
    private static final String KEY_TOTAL_RATING = "totalRating";
    private static final String KEY_OWNER_USER_ID = "owner_userID";
    private static final String KEY_OWNER_USER_ID_2 = "userID";
    private static final String KEY_CAR_ID = "carID";
    private static final String KEY_SPECIAL_NOTE = "specialNote";

    private int userID;
    private String longIssue;
    private String latIssue;
    private String longReturn;
    private String latReturn;
    private String brandName;
    private String modelName;
    private Double amount;
    private String carType;
    private String modelYear;
    private String color;
    private String firstName;
    private String lastName;
    private String fullName;
    private String profPic;
    private String sdate, edate;
    private Double totalAmount;
    private int ownerID;
    private int owner_userID;
    private long diff;
    private double total, total2;
    private int carID;

    private TextView tvBrand, tvPickup, tvStartDate, tvEndDate, tvService, tvName, tvRating;
    private TextView lblTotalDays, lblAmount, lblStart, lblEnd, lblTotalAmount, lblTotalAmount2, lblPickup, lblReturn;
    private EditText etSpecialNote;
    private ImageView profilePicture;
    private LinearLayout btmSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Button btnBookNow, btnConfirm;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private int recordID;
    private String startDate, endDate, address, address2, serviceType, specialNote, lStart, lEnd;
    private String fetch_booking_data, request_booking;
    private String first;
    private SessionHandler session;
    private String user_status;
    private int isVerified;

    private int[] myImageList = new int[]{R.drawable.harley2, R.drawable.benz2,
            R.drawable.vecto,R.drawable.webshots
            ,R.drawable.bikess,R.drawable.img1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_book_service);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_view_booking_offer, null, false);
        drawer.addView(contentView, 0);


        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        userID = user.getUserID();
        isVerified = user.getIsVerified();
        user_status = user.getStatus();

        btnConfirm = findViewById(R.id.btnConfirm);

        //Check user status and is email verified
        if (!user_status.equalsIgnoreCase("Active")) {
            if (isVerified == 0) {
                btnConfirm.setVisibility(GONE);
            }
        }


        UrlBean url = new UrlBean();
        fetch_booking_data = url.getFetch_booking_data();
        request_booking = url.getRequest_booking();

        //Initialize Text Views
        tvBrand = findViewById(R.id.tvBrand);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvPickup = findViewById(R.id.tvPickup);
        tvName = findViewById(R.id.tvName);
        profilePicture = findViewById(R.id.profilePicture);
        btnBookNow = findViewById(R.id.btnBookNow);
        lblTotalDays = findViewById(R.id.lblTotalDays);
        lblAmount = findViewById(R.id.lblAmount);
        lblStart = findViewById(R.id.lblStart);
        lblEnd = findViewById(R.id.lblEnd);
        lblTotalAmount = findViewById(R.id.lblTotalAmount);
        lblPickup = findViewById(R.id.lblPickup);
        lblReturn = findViewById(R.id.lblReturn);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvService = findViewById(R.id.tvServiceType);
        tvRating = findViewById(R.id.tvRating);
        etSpecialNote = findViewById(R.id.etSpecialNote);

        final View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        // Get the transferred data from source activity.
        Intent intent = getIntent();
        String record = intent.getExtras().getString("record", "");
        //recordID = intent.getExtras().getInt("KEY_RECORD_ID", 0);
        startDate = getIntent().getExtras().getString("KEY_START_DATE");
        endDate = getIntent().getExtras().getString("KEY_END_DATE");
        serviceType = getIntent().getExtras().getString("KEY_SERVICE_TYPE");
        //address = getIntent().getExtras().getString("KEY_ADDRESS");
        sdate = getIntent().getExtras().getString("KEY_START");
        edate = getIntent().getExtras().getString("KEY_END");
        lStart = getIntent().getExtras().getString("KEY_LBL_START");
        lEnd = getIntent().getExtras().getString("KEY_LBL_END");

        String arr[] = record.split(" ", 2);
        String first = arr[0];
        recordID = Integer.parseInt(first);

        Toast.makeText(getApplicationContext(), record, Toast.LENGTH_SHORT).show();
        //Array for slide
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();


        init();
        getData();

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ListReviewBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setCarReview(recordID, carID);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ListReviewCarBookingFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBooking();
            }
        });

    }



    public void getData(){
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            if (recordID == 0){
                Toast.makeText(getApplicationContext(), "Record ID is 0", Toast.LENGTH_SHORT).show();
            } else {
                request.put(KEY_RECORD_ID, recordID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, fetch_booking_data, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Check if user got logged in successfully

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
                        firstName = response.getString(KEY_FIRST_NAME);
                        lastName = response.getString(KEY_LAST_NAME);
                        profPic = response.getString(KEY_PROF_PIC);
                        ownerID = response.getInt(KEY_OWNER_ID);
                        owner_userID = response.getInt(KEY_OWNER_USER_ID);
                        carID = response.getInt(KEY_CAR_ID);


                        session.setOwnerUserID(owner_userID, firstName, lastName, profPic);
                        fullName = firstName + " " + lastName;

                        //Get Total number of days
                        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String inputString1 = sdate;
                        String inputString2 = edate;
                        try {
                            Date date1 = myFormat.parse(inputString1);
                            Date date2 = myFormat.parse(inputString2);
                            long diff = date2.getTime() - date1.getTime();
                            total = Double.valueOf(diff);
                            total2 = new Double(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Set up address
                        Geocoder geocoder;
                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(latIssue), Double.parseDouble(longIssue), 1);
                            address = addresses.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        List<Address> addresses2 = null;
                        try {
                            addresses2 = geocoder.getFromLocation(Double.parseDouble(latReturn), Double.parseDouble(longReturn), 1);
                            address2 = addresses2.get(0).getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DecimalFormat df = new DecimalFormat("#.00");
                        //Set Text
                        tvBrand.setText(color + " " + brandName + " " + modelName + " " + modelYear);
                        tvPickup.setText(address);
                        tvStartDate.setText(lStart);
                        tvEndDate.setText(lEnd);
                        tvService.setText(serviceType);
                        tvName.setText(fullName);

                        //Set Text Bot Sheet
                        lblStart.setText(lStart);
                        lblEnd.setText(lEnd);
                        lblTotalDays.setText(String.valueOf(total2));
                        lblAmount.setText(String.valueOf("₱" + df.format(amount)));
                        totalAmount = total2*amount;
                        lblTotalAmount.setText(String.valueOf("₱" + df.format(totalAmount)));
                        lblPickup.setText(address);
                        lblReturn.setText(address2);

                        //Set Profile Picutre
                        UrlBean url = new UrlBean();
                        String myUrl = url.getProfilePicUrl()+profPic;

                        Glide.with(getApplicationContext())
                                .load(myUrl)
                                .into(profilePicture);

                        getAverageRating();



                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                //Display error message whenever an error occurs
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    public void confirmBooking(){
        specialNote = etSpecialNote.getText().toString();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_RECORD_ID, recordID);
            request.put(KEY_USER_ID, userID);
            request.put(KEY_OWNER_ID, ownerID);
            request.put(KEY_TOTAL_AMOUNT, totalAmount);
            request.put(KEY_START_DATE, startDate);
            request.put(KEY_END_DATE, endDate);
            request.put(KEY_SPECIAL_NOTE, specialNote);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, request_booking, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {

                                //Go to Success Page
                                Intent i = new Intent(ViewBookingOfferActivity.this, SuccessBookingRequestActivity.class);
                                startActivity(i);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(),
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }


    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        getData();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(ViewBookingOfferActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    public void getAverageRating(){
        UrlBean url = new UrlBean();
        String getUserData3 = url.getGetAverageRatingProfile();
        session = new SessionHandler(getApplicationContext());
        //User user = session.getUserDetails();
        //int userID = user.getUserID();

        JSONObject request = new JSONObject();
        try {
            request.put(KEY_OWNER_USER_ID_2, owner_userID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, getUserData3, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                Double rate = response.getDouble(KEY_TOTAL_RATING);

                                DecimalFormat df = new DecimalFormat("#.#");
                                df.format(rate);

                                tvRating.setText(String.valueOf(rate));

                            } else{
                                Toast.makeText(getApplicationContext(),
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
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);

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
