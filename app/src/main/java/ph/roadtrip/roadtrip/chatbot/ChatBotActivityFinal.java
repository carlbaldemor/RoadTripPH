package ph.roadtrip.roadtrip.chatbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import ph.roadtrip.roadtrip.R;

public class ChatBotActivityFinal extends AppCompatActivity {

    private WebView myWebView;
    private static final String url = "https://console.dialogflow.com/api-client/demo/embedded/be4d2c40-0be0-4f2d-9f0b-f7a9a463a944";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_final);

        myWebView = findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.loadUrl(url);

    }
}
