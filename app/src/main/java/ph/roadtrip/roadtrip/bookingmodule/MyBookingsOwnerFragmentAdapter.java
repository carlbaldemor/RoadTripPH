package ph.roadtrip.roadtrip.bookingmodule;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MyBookingsOwnerFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    public MyBookingsOwnerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AcceptedBookingFragment();
        } else {
            return new RequestsBookingFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Accepted";
            case 1:
                return "Requests";
            default:
                return null;
        }
    }

}