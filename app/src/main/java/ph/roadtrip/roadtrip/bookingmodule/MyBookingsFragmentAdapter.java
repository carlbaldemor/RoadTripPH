package ph.roadtrip.roadtrip.bookingmodule;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MyBookingsFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    public MyBookingsFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CurrentBookingFragment();
        } else if (position == 1){
            return new PendingBookingFragment();
        }else {
            return new PenaltyBookingFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Current";
            case 1:
                return "Pending";
            case 2:
                return "Penalties";
            default:
                return null;
        }
    }

}