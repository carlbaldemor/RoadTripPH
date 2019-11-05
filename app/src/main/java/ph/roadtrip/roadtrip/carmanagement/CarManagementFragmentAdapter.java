package ph.roadtrip.roadtrip.carmanagement;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CarManagementFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    public CarManagementFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ListCarsFragment();
        } else {
            //return new AddCarFragment();
        }
        return null;
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
                return "My Cars";
            case 1:
                return "Add Car";
            default:
                return null;
        }
    }

}