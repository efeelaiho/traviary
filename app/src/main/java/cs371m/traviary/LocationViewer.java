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
public class LocationViewer extends Fragment {

    // Progress Dialog
    private ProgressDialog pDialog;

    private WebView webview;
    private String locationBeingViewed = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        pDialog = ProgressDialog.show(getContext(), "", "Please wait while the Wikipedia page for " + locationBeingViewed + " loads...", true);

        View v = inflater.inflate(R.layout.location_viewer, container, false);

        webview = (WebView) v.findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
        webview.loadUrl("https://en.wikipedia.org/wiki/" + locationBeingViewed);

        return v;
    }

    public void setLocationBeingViewed(String l) {
        locationBeingViewed = l;
    }

}
