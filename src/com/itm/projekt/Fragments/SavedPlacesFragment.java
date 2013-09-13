/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.itm.projekt.Adapters.BasicItemAdapter;
import com.itm.projekt.Hemnet.MapFragment;
import com.itm.projekt.Items.BasicItem;
import com.itm.projekt.MainActivity;
import com.itm.projekt.R;

import java.util.ArrayList;
import java.util.Map;

public class SavedPlacesFragment extends ListFragment  {

    BasicItemAdapter adapter;
    private ArrayList<BasicItem> items = new ArrayList<BasicItem>();
    private ArrayList<String> removedKeys = new ArrayList<String>();

    private static final String TAG = SavedPlacesFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.d(TAG, "OnCreate");

    }

    @Override
    public void onViewStateRestored(Bundle savedInstance) {
        super.onViewStateRestored(savedInstance);
        Log.d(TAG, "OnViewStateRestored");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
    }


    private void updateListItems(SharedPreferences prefs) {

        if(!items.isEmpty())
            items.clear();

        Map<String, ?> prefItems = prefs.getAll();
        for(String s : prefItems.keySet()){
            String latLng = prefItems.get(s).toString().replace("[", "").replace("]", "");

            Log.d(TAG, "Updating list with " + prefItems.get(s).toString() + " : " + latLng);
            items.add(new BasicItem(s, latLng, R.drawable.location_place, R.drawable.content_discard));
        }

        adapter.addAll(items);
        adapter.notifyDataSetChanged();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        adapter = new BasicItemAdapter(getActivity(), R.layout.basic_list_item, new ArrayList<BasicItem>());

        SharedPreferences prefs = getActivity().getSharedPreferences("savedplaces", 0);
        updateListItems(prefs);

        adapter.setItemListener(new BasicItemAdapter.ItemListener() {
            @Override
            public void onLeftIconClicked(int pos) {
            }

            @Override
            public void onRightIconClicked(int pos) {
                removedKeys.add(adapter.getItem(pos).getTitle());
                adapter.removeItem(pos);
            }
        });

        setListAdapter(adapter);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        return root;
    }

    @Override
    public String toString() {
        return "Sparade platser";
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView latLng = (TextView)v.findViewById(R.id.basic_list_item_summary);

        Fragment fragment = ((MainActivity)getActivity()).addFragment(getResources().getString(R.string.mapFragment));
        if(fragment != null) {
            float lat = 0, lng = 0;
            for(String entry : latLng.getText().toString().split(",")) {
                if(entry.contains("Lng")) lng = Float.parseFloat(entry.replace("Lng", ""));
                if(entry.contains("Lat")) lat = Float.parseFloat(entry.replace("Lat", ""));
            }
            Log.d(TAG, "Swapping fragments!");
            ((MapFragment)fragment).updatePosition(new LatLng(lat, lng));
            ((MainActivity)getActivity()).swapFragments(this, fragment);

        }
    }

    public void updateListFromPreferences(SharedPreferences prefs) {

        if(getListAdapter() == null)
            return;

        updateListItems(prefs);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.savedPrefs), 0);
        SharedPreferences.Editor editor = prefs.edit();
        for(String key : removedKeys) {
            editor.remove(key);
        }
        editor.commit();
    }

}
