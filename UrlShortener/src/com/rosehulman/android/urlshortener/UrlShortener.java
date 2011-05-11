package com.rosehulman.android.urlshortener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class UrlShortener extends Activity {
    
    UrlService mUrls;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mUrls = new GooGlService();
    }
}