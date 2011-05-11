package com.rosehulman.android.urlshortener;


public interface UrlService {
    
    public String getShortenedUrl(String url);
    
    public int getNumberOfViews(String url);

}
