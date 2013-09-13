/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Hemnet;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.*;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.*;
import com.itm.projekt.Database.SqliteHelper;
import com.itm.projekt.Fragments.LatestResultFragment;
import com.itm.projekt.MainActivity;
import com.itm.projekt.MapHelper.GeoLocation;
import com.itm.projekt.MapHelper.GeoReversed;
import com.itm.projekt.R;

import java.util.*;


public class MapFragment extends Fragment implements
        GeoLocation.GeoLocationListener,
        GeoReversed.GeoLocationListener,
        Realtors.RealtorsListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private MapView mMapView;
    private GoogleMap map;
    private Bundle mBundle;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker currentMarker;
    private SharedPreferences prefs;
    private SqliteHelper database;

    // Stockholm is the default location.
    static final LatLng STOCKHOLM = new LatLng(59.338092,18.069656);

    private final static String TAG = MapFragment.class.getName();
    private final float defaultZoomLevel = 10;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getResources().getString(R.string.locationEnabled))) {
            boolean locationEnabled = prefs.getBoolean(getResources().getString(R.string.locationEnabled), true);
            map.setMyLocationEnabled(locationEnabled);
        }
    }

    private void savePlaceToPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.savedPrefs), 0);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> latlng = new HashSet<String>();
        latlng.add("Lng"+currentMarker.getPosition().longitude);
        latlng.add("Lat"+currentMarker.getPosition().latitude);
        Log.d(TAG, "Adding " + currentMarker.getTitle() + " and latlng: " + latlng.toString());
        editor.putStringSet(currentMarker.getTitle(), latlng);
        editor.commit();

    }
    private class SavePlacesDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_save_place)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            savePlaceToPreferences();
                            Toast.makeText(getActivity(), getResources().getString(R.string.dialog_saved_place), Toast.LENGTH_SHORT).show();
                            currentMarker.hideInfoWindow();

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            currentMarker.hideInfoWindow();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.mapfragment, container, false);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO handle this situation
        }

        mMapView = (MapView) inflatedView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded(inflatedView);

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        database = new SqliteHelper(getActivity());
        getActivity().getSharedPreferences(getResources().getString(R.string.savedPrefs), 0).registerOnSharedPreferenceChangeListener(this);
    }

    private void setUpMapIfNeeded(View inflatedView) {
        if (map == null) {
            map = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
            if (map != null) {
                setUpMap();
            }
        }
    }

    public void updatePostion(LatLng latLng, final String location, final String subLocation) {
        updateMarker(latLng);
        panToPosition(latLng);
        createRealtorQuery(location, subLocation);
    }

    public void updatePosition(LatLng latLng) {
        if(latLng == currentMarker.getPosition())
            return;
        updateMarker(latLng);
        panToPosition(latLng);
        googleGeoQuery(latLng);
    }

    private void setUpMap() {

        boolean locationEnabled = prefs.getBoolean(getResources().getString(R.string.locationEnabled), true);

        if(prefs == null){
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            prefs.registerOnSharedPreferenceChangeListener(this);
        }

        map.setMyLocationEnabled(locationEnabled);
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerDragListener(this);

        setCurrentLocation();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //updateCurrentPosition();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "onStatusChanged " + provider + " " + status + " " + extras.toString());
            }

            @Override
            public void onProviderEnabled(String provider) {

                Log.d(TAG, "onStatusChanged " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "onStatusChanged " + provider);
            }
        };

        try {

            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } catch( IllegalArgumentException e) {
            Log.d(TAG, "No providers available");
            e.printStackTrace();
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                SavePlacesDialogFragment savePlacesDialogFragment = new SavePlacesDialogFragment();
                savePlacesDialogFragment.show(getActivity().getSupportFragmentManager(), "SavedPlaces");
            }
        });

    }

    protected void setCurrentLocality(String locality) {
        Log.d(TAG, " ==== Got locality " + locality + " at latlng " + getCurrentMarkerPosition().toString());
        Toast.makeText(getActivity().getApplicationContext(), "Letar efter mäklare i " + locality, Toast.LENGTH_LONG).show();
        currentMarker.setTitle(locality);
    }


    public void updateMarker(LatLng point) {

        if( currentMarker != null ) {
            currentMarker.remove();
        }

        currentMarker = map.addMarker( new MarkerOptions()
                .position(point)
                .title(getResources().getString(R.string.currentPosition))
                .draggable(true)
        );


    }

    private LatLng getCurrentMarkerPosition() {
        if( currentMarker != null)
            return currentMarker.getPosition();
        return new LatLng(0,0);
    }

    private LatLng getCurrentPosition() {
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(lastLocation == null)
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastLocation == null)
            return STOCKHOLM;

        double lat = lastLocation.getLatitude();
        double lng = lastLocation.getLongitude();
        return new LatLng(lat, lng);
    }

    private void panToPosition(LatLng point) {
        float zoom;
        if(map == null)
            zoom = defaultZoomLevel;
        else
            zoom = map.getCameraPosition().zoom;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, zoom));
        map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
    }

    private void panToPosition(LatLng point,float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, zoom+5));
        map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
    }

    private void googleGeoQuery(LatLng point) {
        String url =  getString(R.string.geoloc)
                +"sensor=true&latlng="+point.latitude+","+point.longitude;
        Log.d(TAG, "Quering " + url);
        new GeoLocation(this).execute(url);
    }

    private void realtorsQuery(String location, String subLocation, String type) {

        if( location == null || subLocation == null ){
            Log.d(TAG, " == ERROR! All locations for realtorquery was null!");
            return;
        }

        Realtors realtors = new Realtors(this);
        String query = getResources().getString(R.string.realtorquery)
                + realtors.encode(location) + "/" + realtors.encode(subLocation) + "/" + (type == null || type.isEmpty() ? "a" : type);

        Log.d(TAG, "Performing query on " + query);

        realtors.execute(query);
    }

    private void setCurrentLocation() {
        LatLng lastLatLng = getCurrentPosition();
        updateMarker(lastLatLng);
        panToPosition(lastLatLng, 10);
        googleGeoQuery(lastLatLng);
    }

    private ArrayList<String> getRealtorTypes() {
        ArrayList<String> types = new ArrayList<String>();
        if(prefs.getBoolean("pref_key_search_houses", false))       types.add("v");
        if(prefs.getBoolean("pref_key_search_apartments", false))   types.add("b");
        if(prefs.getBoolean("pref_key_search_cottages", false))     types.add("f");
        return types;
    }

    @Override
    public void onGeoLocationReady(String locality, String subLocality) {
        setCurrentLocality(subLocality);
        Log.d(TAG, " ======== GOT ALL LOCATIONS! " + locality + "/" + subLocality);

        if(database != null) {
            database.createRecord(locality, subLocality, currentMarker.getPosition());
        }

        createRealtorQuery(locality, subLocality);
    }

    private void createRealtorQuery(final String locality, final String subLocality) {
        boolean allTypes = prefs.getBoolean("pref_key_search_all", false);

        if(!allTypes) {

            ArrayList<String> types = getRealtorTypes();
            StringBuilder queryString = new StringBuilder();

            for(int i = 0; i < types.size(); i++) {
                queryString.append(types.get(i));
                if(i != types.size()-1)
                    queryString.append(",");
            }
            Log.d(TAG, "Searching for custom types " + queryString.toString());
            realtorsQuery(locality, subLocality, queryString.toString());
        }
        else {
            realtorsQuery(locality, subLocality, "a");
        }
    }
    @Override
    public void onGeoLocationReady(LatLng latLng) {
        updatePosition(latLng);
    }

    @Override
    public void onGeoLocationError(String errorMsg) {
        Log.d(TAG, " ======== GOT GEO ERROR! " + errorMsg);
    }


    @Override
    public void onMapClick(LatLng point) {
        //currentLocationText.setText(point.toString());
        map.animateCamera(CameraUpdateFactory.newLatLng(point));
    }

    @Override
    public void onMapLongClick(LatLng point) {
        updateMarker(point);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //currentLocationText.setText("Marker " + marker.getId() + " Drag@" + marker.getPosition());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        panToPosition(marker.getPosition());
        googleGeoQuery(marker.getPosition());
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //currentLocationText.setText("Marker " + marker.getId() + " DragStart");

    }

    @Override
    public void onStop() {
        super.onStop();
        if(map != null ) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if(prefs != null)
            prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        if(map != null){
            locationManager.removeUpdates(locationListener);
        }
        if(prefs != null)
            prefs.unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public String toString() {
        return "Karta";
    }

    @Override
    public void onRealtorsReady(RealtorAreaAverage areaAverage, List<RealtorItem> realtors) {

        final String fragmentId = getString(R.string.latestResultFragment);
        final int maxItems = Integer.parseInt(prefs.getString("pref_search_max_results", "15"));

        Fragment fragment = ((MainActivity)getActivity()).addFragment(fragmentId);

        if(fragment != null) {
            ((LatestResultFragment)fragment).updateAdapter(
                    currentMarker.getTitle(),
                    areaAverage,
                    (realtors.size() > maxItems ? realtors.subList(0, maxItems) : realtors)
            );
            ((MainActivity)getActivity()).updateNotificationItem(fragmentId, maxItems);
            Toast.makeText(getActivity().getApplicationContext(), "La till " + maxItems +" resultat.", Toast.LENGTH_LONG).show();

        } else
            Toast.makeText(getActivity().getApplicationContext(), "Error! Kunde inte skapa resultat lista!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRealtorsError(String error) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * @todo: This would use reverse geo
     * @param query
     */
    public void mapQuery(final String query) {

        if(database != null) {
            Cursor c = database.query(query);
            if (c != null ) {
                if  (c.moveToFirst()) {
                    String location = c.getString(c.getColumnIndex("_location"));
                    String sublocation = c.getString(c.getColumnIndex("_sublocation"));
                    double lat = c.getDouble(c.getColumnIndex("_lat"));
                    double lng = c.getDouble(c.getColumnIndex("_lng"));
                    Log.d(TAG, " ==== Got location " + location + " " + sublocation + "\n and lat:" + lat + "\n and lng:" + lng);
                    updatePostion(new LatLng(lat, lng), location, sublocation);
                    return;
                }
            } else Log.d(TAG, " === NO SQLITE RECORDS!");
        }

        String url =  getString(R.string.geoRev)
                +"sensor=false&address="+query;
        Log.d(TAG, " === Doing query on " + url);
        new GeoReversed(this).execute(url);
    }

}
