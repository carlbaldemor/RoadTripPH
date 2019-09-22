package ph.roadtrip.roadtrip.transactionhistory;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class TransactionFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    public TransactionFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ListTransactionHistoryFragment();
        } else if (position == 1) {
            return new ListReviewFragment();
        }else {
            return new ListCarToReviewFragment();
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
                return "History";
            case 1:
                return "To Review";
            case 2:
                return "Cars To Review";
            default:
                return null;
        }
    }
}