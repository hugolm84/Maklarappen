/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.MapHelper;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.itm.projekt.Network.JSON.JSONResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.*;


/**
 * This class is created because GeoCoding isn't available in the Emulators
 */
public class GeoLocation extends JSONResponse {

    private final static String TAG = GeoLocation.class.getName();
    private JSONResponse.JSONRequestListener jsonRequestListener;
    private GeoLocationListener geoLocationListener;

    /**
     * Listener interface to tell when the locations is ready
     */
    public static interface GeoLocationListener {
        void onGeoLocationReady(String locality, String subLocality);
        void onGeoLocationError(String errorMsg);
    }

    public GeoLocation(Fragment parent) {
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
    private class GeoParser extends AsyncTask<String, Void, List<GeoLocationItem>> {

        private List<GeoLocationItem> map(String result) {

            List<GeoLocationItem> items = new ArrayList<GeoLocationItem>();

            try {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(result).path("results");
                items = mapper.readValue(root, new TypeReference<List<GeoLocationItem>>() {});

                Log.d(TAG, "Found " + items.size() + " items!");

            } catch (IOException e) {

                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

            } finally {

                return items;
            }

        }

        @Override
        protected List<GeoLocationItem> doInBackground(String... params) {
            Log.d(TAG, "Starting backgroundMapping!");
            return map(params[0]);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(List<GeoLocationItem> items) {

            Set<GeoLocationItem> itemSet = new HashSet<GeoLocationItem>();
            synchronized(this) {
                for(GeoLocationItem item : items) {
                    if( item.getLocality().isValid() ) {
                        item.toString();
                        itemSet.add(item);
                    }
                }
            }

            for(GeoLocationItem item : itemSet) {
                Log.d(TAG, item.getLocality().toString());
            }

            if(itemSet.isEmpty()) {
                if(geoLocationListener != null) {
                    geoLocationListener.onGeoLocationError("Geolocations was empty!");
                }
                return;
            }

            GeoLocationItem.Locality loc = itemSet.iterator().next().getLocality();
            if(geoLocationListener != null) {

                final String location = (loc.getLocality() == null ? loc.getCounty() : loc.getLocality());
                Log.d(TAG, "Sending GeoLocationItem " + location + " " + loc.getSubLocality());
                geoLocationListener.onGeoLocationReady(location, loc.getSubLocality());
            }
        }
    }


}