/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Hemnet;

import android.support.v4.app.Fragment;
import android.util.Log;
import com.itm.projekt.Network.JSON.JSONResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Realtors extends JSONResponse {

    private static final String TAG = Realtors.class.getName();

    public static interface RealtorsListener {
        void onRealtorsReady(RealtorAreaAverage areaAverage, List<RealtorItem> realtors);
        void onRealtorsError(String error);
    }

    private RealtorsListener realtorsListener;
    private JSONRequestListener jsonRequestListener;

    public Realtors(Fragment parent) {
        if(parent instanceof RealtorsListener)
            realtorsListener = (RealtorsListener)parent;

        jsonRequestListener = new JSONRequestListener() {

            @Override
            public void onJSONReady(String response) {
                Log.d(TAG, "JSON String response ready!");
                new RealtorParser().map(response);
            }

            @Override
            public void onJSONError(NetworkError error) {
                if(realtorsListener != null) {
                    realtorsListener.onRealtorsError(error.getErrorMsg());
                }
            }
        };
        setListener(jsonRequestListener);
    }


    private class RealtorParser {

        public void map(String result) {

            List<RealtorItem> items = new ArrayList<RealtorItem>();
            RealtorAreaAverage areaAverage = new RealtorAreaAverage();

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode averageRoot = mapper.readTree(result).path("result").path("area_avg");
                JsonNode resultsRoot = mapper.readTree(result).path("result").path("results");
                areaAverage = mapper.readValue(averageRoot, new TypeReference<RealtorAreaAverage>() {});
                items = mapper.readValue(resultsRoot, new TypeReference<List<RealtorItem>>() {});

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                if( items.isEmpty()) {
                    if(realtorsListener != null ) {
                        realtorsListener.onRealtorsError("Error in realtors!");
                        return;
                    }
                }

                if(realtorsListener != null ) {
                    realtorsListener.onRealtorsReady(areaAverage, items);
                }
            }

        }
    }
}
