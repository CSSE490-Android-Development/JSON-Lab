package com.rosehulman.android.urlshortener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;


public class GooGlService implements UrlService {
    
    public static final String TAG = "UrlShortener.GooGlService";
    
    private JSONObject getData(Map<String, Object> data) {
        // Build the URL for the request we're about to make
        Uri.Builder uri = new Uri.Builder().scheme("https").authority("www.googleapis.com").path("/urlshortener/v1/url");
        
        for (String key : data.keySet()) {
            uri = uri.appendQueryParameter(key, data.get(key).toString());
        }
        
        // Create HttpClient and POST request
        HttpClient http = new DefaultHttpClient();
        HttpGet get = new HttpGet(uri.build().toString()); // Note that we're posting to HTTPS here
        
        // Log what we'll be sending
        Log.d(TAG, "Request: " + uri.build().toString());
        
        String response;
        
        // Execute the request and store the result
        try {
            response = http.execute(get, new BasicResponseHandler());
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error executing request: " + e.getMessage());
            return new JSONObject();
        } catch (IOException e) {
            Log.e(TAG, "Error executing request: " + e.getMessage());
            return new JSONObject();
        }
        
        // Log what we got back
        Log.d(TAG, "Response: " + response);
        
        // Parse JSON from response and return the parsed object
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            return new JSONObject();
        }
    }
    
    private JSONObject postJSONData(Map<String, Object> data) {
        // Create HttpClient and POST request
        HttpClient http = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://www.googleapis.com/urlshortener/v1/url"); // Note that we're posting to HTTPS here
        
        // Turn the map of our parameters into a JSONObject, then turn that into a string
        String postData = new JSONObject(data).toString();
        
        // Log what we'll be sending
        Log.d(TAG, "Request: " + postData);
        
        // Set up our POST request with data and appropriate headers
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        try {
            post.setEntity(new StringEntity(postData.toString()));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error creating http post: " + e.getMessage());
            return new JSONObject();
        }
        
        String response;
        
        // Execute the request and store the result
        try {
            response = http.execute(post, new BasicResponseHandler());
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error executing request: " + e.getMessage());
            return new JSONObject();
        } catch (IOException e) {
            Log.e(TAG, "Error executing request: " + e.getMessage());
            return new JSONObject();
        }
        
        // Log what we got back
        Log.d(TAG, "Response: " + response);
        
        // Parse JSON from response and return the parsed object
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            return new JSONObject();
        }
    }

    public String getShortenedUrl(String url) {
        Map<String, Object> postData = new HashMap<String, Object>();
        postData.put("longUrl", url);
        
        JSONObject result = postJSONData(postData);
        
        try {
            return result.getString("id");
        } catch (JSONException e) {
            Log.e(TAG, "Error extracting JSON information: " + e.getMessage());
            return "";
        }
    }

    public int getNumberOfViews(String url) {
        Map<String, Object> postData = new HashMap<String, Object>();
        postData.put("shortUrl", url);
        postData.put("projection", "FULL");
        
        JSONObject result = getData(postData);
        
        try {
            return result.getJSONObject("analytics").getJSONObject("allTime").getInt("shortUrlClicks");
        } catch (JSONException e) {
            Log.e(TAG, "Error extracting JSON information: " + e.getMessage());
            return -1;
        }
    }
}
