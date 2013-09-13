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
                for(GeoLocationItem item : items) {
                    Log.d(TAG, "Found " + item.toString());
                }

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
            Set<GeoLocationItem> itemInvalidSet = new HashSet<GeoLocationItem>();

            synchronized(this) {
                for(GeoLocationItem item : items) {

                    GeoLocationItem.Locality loc = item.getLocality();
                    if( loc.getSubLocality() == null ){
                        if(loc.getCounty() != null && loc.getLocality() != null) {
                            itemInvalidSet.add(item);
                        }
                    }
                    else {
                        itemSet.add(item);
                    }
                }
            }

            if(itemSet.isEmpty()) {
                Log.d(TAG, "No valid locals!");
            }

            if( itemInvalidSet.isEmpty()) {
                Log.d(TAG, "No invalid locals!");
            }

            for(GeoLocationItem item : itemSet) {
                Log.d(TAG, "Valid: " + item.getLocality().toString());
            }

            for(GeoLocationItem item : itemInvalidSet) {
                Log.d(TAG, "Invalid: " + item.getLocality().toString());
            }

            if(itemSet.isEmpty() && itemInvalidSet.isEmpty()) {
                if(geoLocationListener != null) {
                    geoLocationListener.onGeoLocationError("Geolocations was empty!");
                }
                return;
            }

            if(geoLocationListener != null) {

                GeoLocationItem.Locality loc;

                if(!itemSet.isEmpty())
                    loc = itemSet.iterator().next().getLocality();
                else
                    loc = itemInvalidSet.iterator().next().getLocality();

                final String subLocation = (loc.getSubLocality() == null ? loc.getLocality() : loc.getSubLocality());
                final String location = (loc.getLocality() == null ? loc.getCounty() : loc.getLocality());

                Log.d(TAG, "Sending GeoLocationItem " + location + " " + subLocation);
                geoLocationListener.onGeoLocationReady(location, subLocation);
            }
        }
    }


}