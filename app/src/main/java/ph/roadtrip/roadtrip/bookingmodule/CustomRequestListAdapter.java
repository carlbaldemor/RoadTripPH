package ph.roadtrip.roadtrip.bookingmodule;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;

public class CustomRequestListAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private LayoutInflater inflater;
    private List<BookingRequests> movieItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomRequestListAdapter(Activity activity, List<BookingRequests> movieItems) {
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
            convertView = inflater.inflate(R.layout.list_requests_layout, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.picture_list);
        TextView brandName = (TextView) convertView.findViewById(R.id.brandName);
        TextView modelName = (TextView) convertView.findViewById(R.id.modelName);
        TextView modelYear = (TextView) convertView.findViewById(R.id.year);
        TextView firstName = (TextView) convertView.findViewById(R.id.firstName);
        TextView lastName = (TextView) convertView.findViewById(R.id.lastName);
        TextView color = (TextView) convertView.findViewById(R.id.color);
        Button btnView = (Button) convertView.findViewById(R.id.btnView);
        TextView totalAmount = (TextView) convertView.findViewById(R.id.totalAmount);

        // getting movie data for the row
        BookingRequests m = movieItems.get(position);

        // thumbnail imageS
        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);


        DecimalFormat df = new DecimalFormat("#,###.00");
        Double totals = Double.parseDouble(m.getTotalAmount());
        // title
        brandName.setText(m.getBrandName());
        modelName.setText(m.getModelName());
        modelYear.setText(m.getModelYear());
        firstName.setText(m.getFirstName());
        lastName.setText(m.getLastName());
        color.setText(m.getColor());
        totalAmount.setText(String.valueOf("₱" + df.format(totals)));

        final BookingRequests items = movieItems.get(position);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new SessionHandler(activity);
                session.setBookingID(items.getBookingID());

                ViewRequestFragment fragment = new ViewRequestFragment();

                ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });



        return convertView;
    }


}
