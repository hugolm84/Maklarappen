/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.itm.projekt.Adapters.ExpandableListItemAdapter;
import com.itm.projekt.Hemnet.RealtorAreaAverage;
import com.itm.projekt.Hemnet.RealtorItem;
import com.itm.projekt.Items.BasicItem;
import com.itm.projekt.Items.ProgressButton;
import com.itm.projekt.Items.ProgressItem;
import com.itm.projekt.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LatestResultFragment extends Fragment {

    private static final String TAG = LatestResultFragment.class.getSimpleName();

    private TableLayout header;
    private TableRow row_header, row1, row2, row3, row4;
    private ExpandableListView expandableListView;
    private RealtorAreaAverage avgItem;
    private String currentArea;
    private LatestExpandableAdapter adapter;

    public class LatestExpandableAdapter extends ExpandableListItemAdapter<ProgressItem, BasicItem> {

        public LatestExpandableAdapter(Context context) {
            super(context, new ArrayList<ProgressItem>(), new  HashMap<ProgressItem, List<BasicItem>>());
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {


            ProgressItem item = getGroup(groupPosition);

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater)getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.realtor_list_item, null);

            }

            ProgressButton btn = (ProgressButton)convertView.findViewById(R.id.item_progress);

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.item_title);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(item.getTitle());

            TextView lblListCaption = (TextView) convertView
                    .findViewById(R.id.item_caption);
            lblListCaption.setText(item.getSummary());

            TextView lblListCaptionBelow = (TextView) convertView
                    .findViewById(R.id.item_caption_below);

            DecimalFormat df = new DecimalFormat("#.00");
            lblListCaptionBelow.setText(
                    getString(R.string.realtor_item_age) + ": " +
                    df.format(((RealtorItem.RealtorBaseItem) item.getCustomObject()).getAge())
            );

            if(btn != null ) {
                btn.setProgress(item.getProgress());
            }

            TextView countView = (TextView)convertView.findViewById(R.id.list_item_right_text_holder);
            countView.setText(""+item.getNotificationCount());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final BasicItem item = getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.basic_list_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.basic_list_item_title);
            TextView txtListChildSummary = (TextView) convertView.findViewById(R.id.basic_list_item_summary);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.basic_list_left_icon);
            convertView.findViewById(R.id.basic_list_right_icon).setVisibility(View.INVISIBLE);

            txtListChild.setText(item.getTitle());
            txtListChildSummary.setText((item.getSummary().isEmpty() ? "No address" : item.getSummary()));
            txtListChild.setVisibility(View.VISIBLE);
            imageView.setPadding(25, 5, 5, 5);


            int drawableId = R.drawable.ic_house;
            if(item.getTitle().equals(getResources().getString(R.string.realty_type_house))) drawableId = R.drawable.ic_house;
            if(item.getTitle().equals(getResources().getString(R.string.realty_type_cottage))) drawableId = R.drawable.ic_cottage;
            if(item.getTitle().equals(getResources().getString(R.string.realty_type_pairhouse))) drawableId = R.drawable.ic_cottage;
            if(item.getTitle().equals(getResources().getString(R.string.realty_type_appartment))) drawableId = R.drawable.ic_apartment;

            imageView.setImageResource(drawableId);
            return convertView;
        }
    }

    private void updateHeader() {

        if(avgItem == null || currentArea == null) {
            return;
        }

        if(row_header == null)
            return;



        DecimalFormat df = new DecimalFormat("#.00");

        if(isAdded()) {
        ((TextView)row_header.findViewById(R.id.header_area)).setText(
                getString(R.string.realtor_avg_area)
                        + currentArea
        );

        ((TextView)row1.findViewById(R.id.column_1)).setText(
                getString(R.string.realtor_avg_price)
        );

        ((TextView)row1.findViewById(R.id.column_2)).setText(": " + df.format(avgItem.price) );

        ((TextView)row2.findViewById(R.id.column_1)).setText(
                getString(R.string.realtor_avg_fee)
        );

        ((TextView)row2.findViewById(R.id.column_2)).setText(": " + df.format(avgItem.fee) );

        ((TextView)row3.findViewById(R.id.column_1)).setText(
                getString(R.string.realtor_avg_age)
        );

        ((TextView)row3.findViewById(R.id.column_2)).setText(": " + df.format(avgItem.age) );

        ((TextView)row4.findViewById(R.id.column_1)).setText(
                getString(R.string.realtor_avg_change)
        );

        ((TextView)row4.findViewById(R.id.column_2)).setText(": " + df.format(
                (avgItem.price_change_up-avgItem.price_change_down))
        );

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.realtor_header_item, container, false);
        header = (TableLayout)inflatedView.findViewById(R.id.header_table);
        expandableListView = (ExpandableListView)inflatedView.findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(getAdapter());

        row_header = (TableRow)header.findViewById(R.id.row_header);
        row1 = (TableRow)header.findViewById(R.id.row1);
        row2 = (TableRow)header.findViewById(R.id.row2);
        row3 = (TableRow)header.findViewById(R.id.row3);
        row4 = (TableRow)header.findViewById(R.id.row4);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                BasicItem item = getAdapter().getChild(groupPosition, childPosition);
                RealtorItem.RealtorListItem realtorItem = (RealtorItem.RealtorListItem)item.getItemData().get(0);
                FragmentManager m = getActivity().getSupportFragmentManager();
                ImageView imageView = (ImageView)v.findViewById(R.id.basic_list_left_icon);
                DetailFragment fragment = new DetailFragment(realtorItem, imageView.getDrawable());
                m.beginTransaction()
                 .add(R.id.content_frame, fragment)
                 .addToBackStack(realtorItem.getClass().getSimpleName())
                 .show(fragment)
                 .commit();


                return true;
            }
        });
        updateHeader();

        return inflatedView;

    }

    public void updateAdapter(final String area, RealtorAreaAverage areaAverage, List<RealtorItem> items) {

        LatestExpandableAdapter listAdapter = getAdapter();

        Log.d(TAG, "Updateing adapter, have " + listAdapter.getGroupCount() );

        if(!listAdapter.isEmpty())
            listAdapter.clear();


        Log.d(TAG, "Updated adapter, have " + listAdapter.getGroupCount() );

        DecimalFormat df = new DecimalFormat("#.00");

        this.currentArea = area;
        this.avgItem = areaAverage;

        for(RealtorItem realtor : items) {

            ProgressItem item  = new ProgressItem(realtor.getName(), df.format(realtor.getPercentage())+"% andel", "Fragment"+1);
            item.setCustomObject(realtor.getAverage());
            item.setNotificationCount(realtor.getItems().size());
            item.setProgress((int) realtor.getPercentage());
            listAdapter.addHeader(item);

            ArrayList<BasicItem> itemData = new ArrayList<BasicItem>();

            for(RealtorItem.RealtorListItem child : realtor.getItems()) {

                BasicItem i = new BasicItem(child.getType(), child.getAddress(), -1, -1);
                i.addItemData(child);
                itemData.add(i);

            }

            listAdapter.addChilds(item, itemData);
        }

        updateHeader();
    }

    public LatestExpandableAdapter getAdapter() {
        if(adapter == null) {
            adapter = new LatestExpandableAdapter(getActivity());
        }
        return adapter;
    }

    @Override
    public String toString() {
        return "Senaste resultat";
    }

}
