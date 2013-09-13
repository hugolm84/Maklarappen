/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.itm.projekt.Adapters.NotificationItemAdapter;
import com.itm.projekt.Items.NotificationItem;
import com.itm.projekt.R;

import java.util.ArrayList;
public class HistoryFragment extends ListFragment {

    private static final String TAG = HistoryFragment.class.getSimpleName();
    private ArrayList<NotificationItem> items = new ArrayList<NotificationItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        for(int i = 0; i < 10; i++) {
            NotificationItem item  = new NotificationItem("History"+i, R.drawable.location_place, "Fragment"+1);
            item.setNotificationCount(i+2);
            items.add(item);
        }

        setListAdapter(new NotificationItemAdapter(getActivity(), R.layout.drawer_list_menu_item, items));

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        return root;

    }

    @Override
    public String toString() {
        return "Historik";
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i(TAG, "Item clicked: " + id + " view: " + v.getTag());

    }
}
