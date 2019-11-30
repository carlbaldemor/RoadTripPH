package ph.roadtrip.roadtrip.bookingmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DecimalFormat;
import java.util.List;

import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.SessionHandler;

public class CustomPenaltyListAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private LayoutInflater inflater;
    private List<PenaltyBean> movieItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomPenaltyListAdapter(Activity activity, List<PenaltyBean> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_penalty, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        TextView brandName = (TextView) convertView.findViewById(R.id.brandName);
        TextView modelName = (TextView) convertView.findViewById(R.id.modelName);
        TextView modelYear = (TextView) convertView.findViewById(R.id.year);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        TextView dateAdded = (TextView) convertView.findViewById(R.id.dateAdded);
        Button btnView = (Button) convertView.findViewById(R.id.btnView);
        TextView totalAmount = (TextView) convertView.findViewById(R.id.totalAmount);

        // getting movie data for the row
        PenaltyBean m = movieItems.get(position);

        DecimalFormat df = new DecimalFormat("#,###.00");
        Double totals = m.getPenaltyFee();
        // title
        brandName.setText(m.getBrandName());
        modelName.setText(m.getModelName());
        modelYear.setText(m.getYear());
        status.setText(m.getPenaltyStatus());
        dateAdded.setText(m.getDateAdded());
        totalAmount.setText(String.valueOf("₱" + df.format(totals)));

        final PenaltyBean items = movieItems.get(position);

        if(m.getPenaltyStatus().equalsIgnoreCase("Paid")){
            btnView.setVisibility(View.GONE);
        }

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new SessionHandler(activity);
                session.setBookingID(items.getBookingID());

                ViewPendingFragment fragment = new ViewPendingFragment();

                Intent load = new Intent(activity, PenaltyPayPalActivity.class);
                activity.startActivity(load);
            }
        });



        return convertView;
    }


}
