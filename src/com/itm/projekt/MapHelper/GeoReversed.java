/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.MapHelper;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.itm.projekt.Network.JSON.JSONResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class is created because GeoCoding isn't available in the Emulators
 */
public class GeoReversed extends JSONResponse {

    private final static String TAG = GeoLocation.class.getName();
    private JSONResponse.JSONRequestListener jsonRequestListener;
    private GeoLocationListener geoLocationListener;

    /**
     * Listener interface to tell when the locations is ready
     */
    public static interface GeoLocationListener {
        void onGeoLocationReady(LatLng latLng);
        void onGeoLocationError(String errorMsg);
    }

    public GeoReversed(Fragment parent) {
        if(parent instanceof GeoLocationListener)
            geoLocationListener = (GeoLocationListener)parent;

        jsonRequestListener = new JSONRequestListener() {

            @Override
            public void onJSONReady(String response) {
                new GeoParser().execute(response); //.parse();
            }

            @Override
            public void onJSONError(NetworkError error) {
                if(geoLocationListener != null) {
                    geoLocationListener.onGeoLocationError(error.getErrorMsg());
                }
            }
        };
        setListener(jsonRequestListener);
    }

    public void setListner(GeoLocationListener listner) {
        geoLocationListener = listner;
    }

    /**
     * This is a GeoLocation Mapper. It depends on Jackson. Perhaps not needed
     * ObjectMapper is very resource heavy.
     */
    private class GeoParser extends AsyncTask<String, Void, List<GeoRevItem>> {

        private List<GeoRevItem> map(String result) {

            List<GeoRevItem> items = new ArrayList<GeoRevItem>();

            try {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(result).path("results");
                items = mapper.readValue(root, new TypeReference<List<GeoRevItem>>() {});

                Log.d(TAG, "Found " + items.size() + " items!");

            } catch (IOException e) {

                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

            } finally {

                return items;
            }

        }

        @Override
        protected List<GeoRevItem> doInBackground(String... params) {
            Log.d(TAG, "Starting backgroundMapping!");
            return map(params[0]);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(List<GeoRevItem> items) {

            Set<LatLng> itemSet = new HashSet<LatLng>();
            synchronized(this) {
                for(GeoRevItem item : items) {
                    itemSet.add(item.getLatLng());
                }
            }

            if(itemSet.isEmpty()) {
                if(geoLocationListener != null) {
                    geoLocationListener.onGeoLocationError("Geolocations was empty!");
                }
                return;
            }

            LatLng loc = itemSet.iterator().next();
            if(geoLocationListener != null) {
                geoLocationListener.onGeoLocationReady(loc);
            }
        }
    }
}