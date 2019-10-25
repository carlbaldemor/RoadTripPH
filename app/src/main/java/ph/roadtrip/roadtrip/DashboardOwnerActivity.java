package ph.roadtrip.roadtrip;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


public class DashboardOwnerActivity extends BaseOwnerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_dashboard_owner, null, false);
        drawer.addView(contentView, 0);

        //HOME DASHBOARD Start
        HomeOwnerFragment fragmentDemo = (HomeOwnerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        //above part is to determine which fragment is in your frame_container
        setFragment(new HomeOwnerFragment());
        //setFragment(fragmentDemo);
        //HOME DASHBOARD END


    }


    // This could be moved into an abstract BaseActivity
    // class for being re-used by several instances
    protected void setFragment(HomeOwnerFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeOwnerFragment()).commit();

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
