package com.example.browser;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    private String url = "";

    private WebView webHolder;
    private EditText webUrlStr;
    private Button webUrlSearch;
    private Button webUrlCancel;

    //button click listener
    private ButtonClickedListener buttonClickedListener;

    private WebUrlStrWatcher webUrlStrWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        webHolder = (WebView) findViewById(R.id.web_holder);
        webUrlStr = (EditText) findViewById(R.id.web_url_str);
        webUrlSearch = (Button) findViewById(R.id.web_url_search);
        webUrlCancel = (Button) findViewById(R.id.web_url_cancel);

        buttonClickedListener = new ButtonClickedListener();

        webUrlStrWatcher = new WebUrlStrWatcher();
        webHolder.getSettings().setDefaultTextEncodingName("UTF-8");
        webHolder.getSettings().setJavaScriptEnabled(true);
        webHolder.setWebViewClient(new MyWebViewClient());
        webHolder.loadUrl("http://www.baidu.com");

        webUrlSearch.setOnClickListener(buttonClickedListener);
        webUrlStr.addTextChangedListener(webUrlStrWatcher);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError (WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if(error.getErrorCode() == ERROR_HOST_LOOKUP) {
                url = url.substring(7);
                url = "http://www.baidu.com/baidu?word=" + url;
                webHolder.loadUrl(url);
            } else if(error.getErrorCode() == ERROR_UNSUPPORTED_AUTH_SCHEME) {

            }
        }
    }
    private class ButtonClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.web_url_search) {
                webHolder.loadUrl(url);
            } else if(view.getId() == R.id.web_url_cancel) {
                url = "";
                webHolder.loadUrl(url);
            }
        }
    }

    public class WebUrlStrWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }
        public void afterTextChanged(Editable editable) {

        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            url = charSequence.toString();
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = "http://" + url;
            }
            if (URLUtil.isNetworkUrl(url)&&URLUtil.isValidUrl(url)) {
                setStatusOfSearch(1);
            } else {
                setStatusOfSearch(2);
            }
        }
    }

    public void setStatusOfSearch(int status) {
        if (status == 1) {
            //search status
            webUrlSearch.setVisibility(View.VISIBLE);
            webUrlCancel.setVisibility(View.GONE);
        } else if(status == 2) {
            //cancel status
            webUrlSearch.setVisibility(View.GONE);
            webUrlCancel.setVisibility(View.VISIBLE);
        } else {
            //fresh status
        }
    }
}
