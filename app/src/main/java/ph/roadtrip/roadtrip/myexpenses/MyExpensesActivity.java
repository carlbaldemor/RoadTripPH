package ph.roadtrip.roadtrip.myexpenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.classes.SessionHandler;

public class MyExpensesActivity extends DashboardActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);


    }
}
