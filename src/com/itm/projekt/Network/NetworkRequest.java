/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Network;

import android.os.AsyncTask;
import android.util.Log;
import java.io.*;
import java.net.*;


public abstract class NetworkRequest extends AsyncTask<String, Void, Object> {

    private static final String TAG = NetworkRequest.class.getName();

    private HttpURLConnection connection;

    protected class NetworkError {
        private String errorMsg;

        public NetworkError(String e) {
            errorMsg = e;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

    public String encode(String in) {
        try {
            in = URLEncoder.encode(in, "UTF-8");
        } catch( UnsupportedEncodingException e) {
            Log.d(TAG, e.getMessage());
        } finally {
            return in;
        }
    }

    protected void setConnection(HttpURLConnection conn) {
        connection = conn;
    }

    protected void disconnect() {
        if( connection != null )
            connection.disconnect();
    }

    protected InputStream requestResponse() throws IOException {

        Log.d(TAG, "Requesting response for " + connection.getURL().toString());

        if( connection == null ) {
            Log.d(TAG, "ERROR! Connections was null!");
            return null;
        }

        connection.connect();

        InputStream stream = connection.getInputStream();
        int status = connection.getResponseCode();

        if (status / 100 != 2) {
            Log.d(TAG, "Oh my! Not a 2xx response!");
            return null;
        }

        if( stream  == null ) {
            Log.d(TAG, "WARNING! IOStream is null!");
            return null;
        }

        return stream;
    }

    @Override
    protected Object doInBackground(String... urls) {

        try {

            URL url = new URL(urls[0]);
            connection = (HttpURLConnection)url.openConnection();
            connection.setUseCaches(true);
            connection.addRequestProperty("Cache-Control", "max-stale=" + connection.getExpiration());
            InputStream response = requestResponse();//getCachedBody(url);
            return response;

        } catch( IOException e ) {
            Log.d(TAG, "ERROR! " + e.getMessage());
            return null;

        } finally {

            Log.d(TAG, "FINALLY DONE, DISCONNECTING");
            connection.disconnect();
        }
    }

    @Override
    abstract protected void onPreExecute();

    @Override
    abstract protected void onPostExecute(Object result);
}
