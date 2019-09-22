package ph.roadtrip.roadtrip.myfavorites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import ph.roadtrip.roadtrip.bookingmodule.BookServiceActivity;
import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.transactionhistory.AddReviewFragment;

public class CustomFavoritesAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private int brandPos;
    private int modelPos;
    private LayoutInflater inflater;
    private List<MyFavorites> favoriteItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomFavoritesAdapter(Activity activity, List<MyFavorites> favoriteItems) {
        this.activity = activity;
        this.favoriteItems = favoriteItems;
    }

    @Override
    public int getCount() {
        return favoriteItems.size();
    }

    @Override
    public Object getItem(int location) {
        return favoriteItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_favorite, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.picture_list);
        TextView firstName = (TextView) convertView.findViewById(R.id.tvFirstName);
        TextView lastName = (TextView) convertView.findViewById(R.id.tvLastName);
        TextView dateAdded = (TextView) convertView.findViewById(R.id.tvDateAdded);
        Button btnView = (Button) convertView.findViewById(R.id.btnView);

        // getting movie data for the row
        MyFavorites m = favoriteItems.get(position);

        // thumbnail imageS
        thumbNail.setImageUrl(m.getProfilePicture(), imageLoader);

        // title
        firstName.setText(m.getOwner_firstName());
        lastName.setText(m.getOwner_lastName());
        dateAdded.setText(m.getDateAdded());


        final MyFavorites items = favoriteItems.get(position);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReviewFragment fragment = new AddReviewFragment();
                session = new SessionHandler(activity);
                //session.setItBaby(items.getRecordID(), brandPos, modelPos);
                //Booking booking = session.getBookingIDHistory();
                //Toast.makeText(activity, "Record ID: "  + items.getBrandName() + carRecord.getBrandPos()  + carRecord.getModelPos(),Toast.LENGTH_LONG).show();
                //session.setBookingIDHistory(items.getBookingID(), items.getCarowner_userID());
                session.setOwnerIDFavorites(items.getOwnerID());

                Intent load = new Intent(activity, BookServiceActivity.class);
                activity.startActivity(load);
                activity.finish();
            }
        });



        return convertView;
    }


}
