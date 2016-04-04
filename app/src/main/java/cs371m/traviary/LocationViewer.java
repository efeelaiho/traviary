package cs371m.traviary;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Hunter on 3/31/16.
 */
public class LocationViewer extends ActionBarActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    private WebView webview;
    private String locationBeingViewed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_viewer);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
                locationBeingViewed = null;
            else
                locationBeingViewed = extras.getString("name");
        }
        else {
            locationBeingViewed = (String) savedInstanceState.getSerializable("name");
        }

        pDialog = ProgressDialog.show(LocationViewer.this, "", "Please wait while the Wikipedia page for " + locationBeingViewed + " loads...", true);
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
        webview.loadUrl("https://en.wikipedia.org/wiki/" + locationBeingViewed);

    }

}
