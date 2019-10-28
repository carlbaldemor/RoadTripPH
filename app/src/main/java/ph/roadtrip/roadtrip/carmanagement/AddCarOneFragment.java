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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.protobuf.StringValue;

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
import ph.roadtrip.roadtrip.profile.EditProfileFragment;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class AddCarOneFragment extends Fragment {

    private static final String KEY_EMPTY = "";

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
    private Button btnNext;
    private EditText etPlateNumber, etChassisNumber;
    private SessionHandler session;
    private String username;
    private String user_status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car_page_one, container, false);

        //Add Car Link
        UrlBean url = new UrlBean();

        //Get Username of user
        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();
        username = user.getUsername();
        user_status = user.getStatus();

        CarRecord user2 = session.getOwnerID();
        ownerID = user2.getOwnerID();

        btnNext = view.findViewById(R.id.btnNext);

        colorSpinner = (Spinner) view.findViewById(R.id.ColorSpinner);
        yearSpinner = (Spinner) view.findViewById(R.id.YearSpinner);
        serviceSpinner = (Spinner) view.findViewById(R.id.serviceSpinner);

        brandSpinner = (Spinner) view.findViewById(R.id.SpinnerFeedbackType);
        String size = brandSpinner.getSelectedItem().toString();

        modelSpinner = (Spinner) view.findViewById(R.id.ModelSpinner);
        String size2 = modelSpinner.getSelectedItem().toString();


        etPlateNumber = view.findViewById(R.id.etPlateNumber);
        etChassisNumber = view.findViewById(R.id.etChassisNumber);

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

        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                color = colorSpinner.getSelectedItem().toString();
                year = yearSpinner.getSelectedItem().toString();
                plateNumber = etPlateNumber.getText().toString().trim();
                chassisNumber = etChassisNumber.getText().toString().trim();

                int spinner_pos;
                String[] size_values;
                int size;

                switch (model_pos) {
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


                if (!validateInputs()) {
                    //Put the value
                    AddCarTwoFragment ldf = new AddCarTwoFragment();
                    Bundle args = new Bundle();
                    args.putString("color", color);
                    args.putString("year", year);
                    args.putInt("modelID", modelID);
                    args.putString("plateNumber", plateNumber);
                    args.putString("chassisNumber", chassisNumber);
                    ldf.setArguments(args);

                    //Inflate the fragment
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, ldf);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

    private boolean validateInputs(){
        if (plateNumber.equalsIgnoreCase(KEY_EMPTY)){
            etPlateNumber.setError("Plate Number cannot be empty!");
            etPlateNumber.requestFocus();
            return false;
        }
        if(plateNumber.length() > 8 && plateNumber.length() < 5){
            
        }

        return false;
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
}

