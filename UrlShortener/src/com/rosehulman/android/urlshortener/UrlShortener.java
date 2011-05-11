package com.rosehulman.android.urlshortener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UrlShortener extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final UrlService urlService = new GooGlService();
        
        final Button shortenButton = (Button) findViewById(R.id.shorten_button);
        final Button viewsButton = (Button) findViewById(R.id.views_button);
        final EditText url = (EditText) findViewById(R.id.input);
        final TextView views = (TextView) findViewById(R.id.views);
        
        shortenButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                url.setText(urlService.getShortenedUrl(url.getText().toString()));
            }
        });
        
        viewsButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                views.setText(urlService.getNumberOfViews(url.getText().toString()) + " views");
            }
        });
    }
}