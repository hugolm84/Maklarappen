package com.itm.projekt.MapHelper;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown=true)
public class GeoLocationItem {


    @JsonIgnore
    private Locality locality = new Locality();


    private final String subTAG = "sublocality";
    private final String locTAG = "locality";
    private final String admTAG = "administrative_area_level_1";
    private final String couTag = "County";

    @JsonIgnoreProperties(ignoreUnknown=true)
    public class Locality {

        private String subLocality;
        private String locality;
        private String county;

        public String getSubLocality() {
            return subLocality;
        }

        public void setSubLocality(String subLocality) {
            this.subLocality = subLocality;
        }

        public String getLocality() {
            return locality;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }

        public String getCounty() {
            return county;
        }

        public boolean isValid() {

            return (subLocality != null);
        }

        public void setCounty(String county) {
            this.county = county;
        }

        @Override
        public String toString() {
            return "Locality{" +
                    "subLocality='" + subLocality + '\'' +
                    ", locality='" + locality + '\'' +
                    ", county='" + county + '\'' +
                    '}';
        }
    }

    private static class AddressComponent {

        private String long_name;
        private String short_name;
        private List<String> types;
        public Geometry geometry;

        public static class Geometry {
            public Location location;
            public static class Location {
                public double lat;
                public double lng;
            }
        }

        @Override
        public String toString() {
            return "AddressComponent{" +
                    "long_name='" + long_name + '\'' +
                    ", short_name='" + short_name + '\'' +
                    ", types=" + types +
                    ", geometry=" + geometry.location.lat + " " +
                    '}';
        }

        public String getLong_name() {
            return long_name;
        }

        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

    }

    private List<AddressComponent> address_components;


    @Override
    public String toString() {
        return "GeoLocationItem{" +
                "address_components=" + address_components +
                '}';
    }

    public List<AddressComponent> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<AddressComponent> address_components) {
        this.address_components = address_components;

        for(AddressComponent comp : address_components) {

            String longName = comp.getLong_name();

            if( locality.getLocality() != null && locality.getSubLocality() != null)
                break;

            for(String type : comp.getTypes()) {
                if(type.equals(subTAG))
                    locality.setSubLocality(longName);
                if(type.equals(locTAG))
                    locality.setLocality(longName);
                if(type.equals(admTAG))
                    locality.setCounty(longName.replace(couTag, "").trim());
            }
        }
    }

    public Locality getLocality() {
        return locality;
    }

}