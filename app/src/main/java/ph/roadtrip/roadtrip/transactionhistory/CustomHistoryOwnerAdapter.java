package ph.roadtrip.roadtrip.transactionhistory;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;

public class CustomHistoryOwnerAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private int brandPos;
    private int modelPos;
    private LayoutInflater inflater;
    private List<Booking> transactionItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomHistoryOwnerAdapter(Activity activity, List<Booking> transactionItems) {
        this.activity = activity;
        this.transactionItems = transactionItems;
    }

    @Override
    public int getCount() {
        return transactionItems.size();
    }

    @Override
    public Object getItem(int location) {
        return transactionItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_history_owner, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.picture_list);
        TextView brandName = (TextView) convertView.findViewById(R.id.brandName);
        TextView modelName = (TextView) convertView.findViewById(R.id.modelName);
        TextView modelYear = (TextView) convertView.findViewById(R.id.year);
        TextView plateNumber = (TextView) convertView.findViewById(R.id.plateNumber);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        TextView totalAmount = (TextView) convertView.findViewById(R.id.totalAmount);
        Button btnView = (Button) convertView.findViewById(R.id.btnView);

        // getting movie data for the row
        Booking m = transactionItems.get(position);

        // thumbnail imageS
        thumbNail.setImageUrl(m.getRecordPicture(), imageLoader);

        // title
        brandName.setText(m.getBrandName());
        modelName.setText(m.getModelName());
        modelYear.setText(m.getModelYear());
        plateNumber.setText(m.getModelYear());
        status.setText(m.getStatus());
        totalAmount.setText(m.getTotalAmount());

        final Booking items = transactionItems.get(position);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHistoryRecordOwner fragment = new ViewHistoryRecordOwner();
                session = new SessionHandler(activity);
                //session.setItBaby(items.getRecordID(), brandPos, modelPos);

                Booking booking = session.getBookingIDHistory();
                //Toast.makeText(activity, "Record ID: "  + items.getBrandName() + carRecord.getBrandPos()  + carRecord.getModelPos(),Toast.LENGTH_LONG).show();
                session.setBookingIDHistory(items.getBookingID(), items.getOwnerID());

                ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });



        return convertView;
    }


}
