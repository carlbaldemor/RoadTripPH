package ph.roadtrip.roadtrip.bookingmodule;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.classes.UrlBean;
import ph.roadtrip.roadtrip.classes.updateData;
import ph.roadtrip.roadtrip.R;

public class ScanQRPReturnFragment extends Fragment {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    Button next;
    private SessionHandler session;
    private DrawerLayout drawer;
    private Intent load;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scan_bar_code, container, false);

        surfaceView = view.findViewById(R.id.camerapreview);
        textView = view.findViewById(R.id.textView);

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
        String bookingID = temp[3];

        Toast.makeText(getActivity(), userID + " " + latitude + " " + longitude + " " + bookingID, Toast.LENGTH_SHORT).show();
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

}
