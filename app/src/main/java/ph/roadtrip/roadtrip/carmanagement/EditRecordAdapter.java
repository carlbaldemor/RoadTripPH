package ph.roadtrip.roadtrip.carmanagement;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ph.roadtrip.roadtrip.fileupload.CarAttachmentsFragment;

public class EditRecordAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context mContext;

    public EditRecordAdapter(FragmentManager fm) {
        super(fm);
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EditDetailsFragment();
        } else {
            return new CarAttachmentsFragment();
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
                return "Edit Details";
            case 1:
                return "Upload Attachments";
            default:
                return null;
        }
    }

}