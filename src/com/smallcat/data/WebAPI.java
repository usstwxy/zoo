package com.smallcat.data;

import com.loopj.android.http.*;


public class WebAPI {
	private static final String BASE_URL = "http://114.215.207.88/api/";

	private static AsyncHttpClient client = new AsyncHttpClient();


	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	    client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	    client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
	    return BASE_URL + relativeUrl;
	}
}
