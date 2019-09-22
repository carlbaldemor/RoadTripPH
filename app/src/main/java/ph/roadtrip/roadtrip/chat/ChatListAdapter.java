package ph.roadtrip.roadtrip.chat;

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

import java.util.List;

import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.bookingmodule.BookServiceActivity;
import ph.roadtrip.roadtrip.bookingmodule.BookingRequests;
import ph.roadtrip.roadtrip.bookingmodule.ViewAcceptedBooking;
import ph.roadtrip.roadtrip.carmanagement.AppController;
import ph.roadtrip.roadtrip.classes.SessionHandler;
import ph.roadtrip.roadtrip.transactionhistory.AddReviewFragment;

public class ChatListAdapter extends BaseAdapter {
    private SessionHandler session;
    private Activity activity;
    private LayoutInflater inflater;
    private List<Messages> chatsItems;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public ChatListAdapter(Activity activity, List<Messages> chatsItems) {
        this.activity = activity;
        this.chatsItems = chatsItems;
    }

    @Override
    public int getCount() {
        return chatsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return chatsItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_chat, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.picture_list);
        TextView firstName = (TextView) convertView.findViewById(R.id.tvFirstName);
        TextView lastName = (TextView) convertView.findViewById(R.id.tvLastName);
        Button btnView = (Button) convertView.findViewById(R.id.btnView);

        // getting movie data for the row
        Messages m = chatsItems.get(position);

        // thumbnail imageS
        thumbNail.setImageUrl(m.getImageUrl(), imageLoader);

        // title
        firstName.setText(m.getSender_firstName());
        lastName.setText(m.getSender_lastName());

        final Messages items = chatsItems.get(position);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListMessagesFragment fragment = new ListMessagesFragment();
                session = new SessionHandler(activity);
                session.setChatID(items.getChatID());

                ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });



        return convertView;
    }


}
