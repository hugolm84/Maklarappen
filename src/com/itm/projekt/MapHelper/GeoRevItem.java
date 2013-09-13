/**
 * Hugo Lindström (C) 2013
 * huli1000
 */

package com.itm.projekt.MapHelper;

import com.google.android.gms.maps.model.LatLng;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class GeoRevItem {

    public Geometry geometry;


    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Geometry {
        public Location location;

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static class Location {
            public double lat;
            public double lng;
        }
    }

    public LatLng getLatLng() {
        return new LatLng(geometry.location.lat, geometry.location.lng);
    }

    @Override
    public String toString() {
        return geometry.location.lat +" "+ geometry.location.lng;
    }
}
