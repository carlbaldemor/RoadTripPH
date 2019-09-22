package ph.roadtrip.roadtrip.chatbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import ph.roadtrip.roadtrip.DashboardActivity;
import ph.roadtrip.roadtrip.MyApplication;
import ph.roadtrip.roadtrip.R;
import ph.roadtrip.roadtrip.transactionhistory.TransactionHistoryFragment;

public class ChatBotActivity extends DashboardActivity {

    private LinearLayout linearLayout;
    private WebView myWebView;
    private static final String url = "https://console.dialogflow.com/api-client/demo/embedded/be4d2c40-0be0-4f2d-9f0b-f7a9a463a944";

    public ChatBotActivity(){

    }

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
                Intent load = new Intent(getApplicationContext(), ChatBotActivityFinal.class);
                startActivity(load);
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
