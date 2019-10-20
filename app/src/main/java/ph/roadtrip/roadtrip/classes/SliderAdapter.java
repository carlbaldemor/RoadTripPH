package ph.roadtrip.roadtrip.classes;



import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ph.roadtrip.roadtrip.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context =context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.calendar,
            R.drawable.chauffeur,
            R.drawable.car,
            R.drawable.map,
            R.drawable.eyeglasses,
            R.drawable.touch,
            R.drawable.stopwatch
    };

    public String[] slide_headings = {
            "Step 1",
            "Step 2",
            "Step 3",
            "Step 4",
            "Step 5",
            "Step 6",
            "Step 7"
    };

    public String[] slide_descs = {
            "Select date, time of pick up and return.",
            "Choose service type; Self drive or Chauffeur.",
            "Choose car type.",
            "Pin your pick up location.",
            "View booking details.",
            "Press Book now.",
            "Wait for the service to be accepted."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup containter, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, containter, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading =  (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        containter.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);

    }

}
