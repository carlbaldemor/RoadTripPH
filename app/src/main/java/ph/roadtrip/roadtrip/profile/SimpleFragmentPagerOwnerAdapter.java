package ph.roadtrip.roadtrip.profile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ph.roadtrip.roadtrip.fileupload.AttachmentsFragment;

public class SimpleFragmentPagerOwnerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerOwnerAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EditInfoOwnerFragment();
        } else {
            return new AttachmentsFragment();
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
                return "Edit Info";
            case 1:
                return "Get Verified";
            default:
                return null;
        }
    }

}