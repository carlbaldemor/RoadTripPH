package ph.roadtrip.roadtrip.chatbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ph.roadtrip.roadtrip.DashboardOwnerActivity;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.transactionhistory.TransactionHistoryFragment;

public class ChatBotOwnerActivity extends DashboardOwnerActivity {
    private LinearLayout linearLayout;

    private static final String APIKEY = "44a83b83e1c05df03c0f2f0b3e0e242e";
    public static final String TAG = MyApplication.class
            .getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_support_page);

        linearLayout = findViewById(R.id.linearChat);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ChatBotFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


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
