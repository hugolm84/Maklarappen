/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.itm.projekt.Hemnet.RealtorItem;
import com.itm.projekt.R;


public class DetailFragment extends Fragment {

    private RealtorItem.RealtorListItem item;
    private Drawable drawableId;

    public DetailFragment(RealtorItem.RealtorListItem item, Drawable drawable) {
        this.item = item;
        this.drawableId = drawable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.detail_item, null);
        ImageView image = (ImageView)root.findViewById(R.id.imageView);


        image.setImageDrawable(drawableId);

        TableLayout table = (TableLayout)root.findViewById(R.id.detail_table);
        table.addView(createRow(inflater, getString(R.string.realtor_avg_price), ""+item.getPrice()));
        table.addView(createRow(inflater, getString(R.string.realtor_avg_fee), ""+item.getFee()));
        table.addView(createRow(inflater, getString(R.string.realtor_item_size), ""+item.getSize()));
        table.addView(createRow(inflater, getString(R.string.realtor_item_m2_prize), ""+item.getPrice_m2()));
        table.addView(createRow(inflater, getString(R.string.realtor_item_age), ""+item.getAge()));
        table.addView(createRow(inflater, getString(R.string.realtor_item_address), ""+item.getAddress()));
        table.addView(createRow(inflater, getString(R.string.realtor_item_broker), ""+item.getBroker()));

        Button button = (Button)root.findViewById(R.id.button);
        button.setText(getString(R.string.realtor_item_detail));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.realtor_url)+item.getId())));
            }
        });
        return root;
    }

    private TableRow createRow(LayoutInflater inflater, String name, String value) {
        TableRow row = (TableRow)inflater.inflate(R.layout.attrib_row, null);
        ((TextView)row.findViewById(R.id.attrib_name)).setText(name);
        ((TextView)row.findViewById(R.id.attrib_value)).setText(value);
        return row;
    }

}