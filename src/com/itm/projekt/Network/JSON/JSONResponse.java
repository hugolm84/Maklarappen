/**
 * Hugo Lindström (C) 2013
 * huli1000
 */

package com.itm.projekt.Network.JSON;

import android.util.Log;
import com.itm.projekt.Network.NetworkRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class JSONResponse extends NetworkRequest {

    private static final String TAG = JSONResponse.class.getName();

    private JSONRequestListener listener;

    public static interface JSONRequestListener {
        void onJSONReady(String response);
        void onJSONError(NetworkError error);
    }

    protected void setListener(JSONRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

    }

    protected Object convertToJSON(InputStream stream) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch( IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * Converting the InputStream to JSON must be handled in the background thread!
     * We must override it
     * */
    @Override
    protected Object doInBackground(String... urls) {

        try {

            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            setConnection(connection);
            InputStream response = connection.getInputStream();
            return convertToJSON(response);

        } catch( IOException e ) {
            Log.d(TAG, "ERROR! " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object result) {

        Log.d(TAG, "Got results!");

        if( result == null ) {
            Log.d(TAG, "Result was null! or not JSON");
            if( listener != null ) {
                listener.onJSONError(new NetworkError("Failed to get JSONObject!"));
            }
            return;
        }

        if( listener != null ) {
            Log.d(TAG, "Sending JSONResponse!");
            listener.onJSONReady(result.toString());
            return;
        }

    }
}
