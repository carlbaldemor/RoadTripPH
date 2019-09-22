package ph.roadtrip.roadtrip.transactionhistory;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class TransactionFragmentOwnerPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    public TransactionFragmentOwnerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ListTransactionHistoryOwnerFragment();
        }else {
            return new ListReviewOwnerFragment();
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
                return "History";
            case 1:
                return "To Review";
            default:
                return null;
        }
    }
}