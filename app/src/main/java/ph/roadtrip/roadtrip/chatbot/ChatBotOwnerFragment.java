package ph.roadtrip.roadtrip.chatbot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ph.roadtrip.roadtrip.R;

public class ChatBotOwnerFragment extends Fragment {

    private WebView myWebView;
    private static final String url = "https://console.dialogflow.com/api-client/demo/embedded/be4d2c40-0be0-4f2d-9f0b-f7a9a463a944";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myWebView = view.findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.loadUrl(url);
    }

}

