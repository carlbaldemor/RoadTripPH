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

public class SliderAdapterCar extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapterCar(Context context) {
        this.context =context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.car,
            R.drawable.map,
            R.drawable.chauffeur,
            R.drawable.coin,
            R.drawable.photo,
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
            "Input car details.",
            "Set your preferred return and pick up location.",
            "Choose service type; Self drive or Chauffeur.",
            "Set rental rate per day.",
            "Upload pictures of your car.",
            "Press Add Car.",
            "Wait for approval."
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
