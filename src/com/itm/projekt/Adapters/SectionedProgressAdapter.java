/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Adapters;

import android.content.Context;
import android.view.View;
import com.itm.projekt.Items.NotificationItem;
import com.itm.projekt.Items.ProgressItem;

import java.util.ArrayList;

public class SectionedProgressAdapter extends ProgressItemAdapter {

    private SectionedAdapter<ProgressItem> sectionedAdapter;

    public SectionedProgressAdapter(Context context, int layoutResourceId, int layoutSectionHeaderId, ArrayList<ProgressItem> data) {
        super(context, layoutResourceId, data);
        sectionedAdapter = new SectionedAdapter<ProgressItem>(context, layoutResourceId, layoutSectionHeaderId, data) {
            @Override
            public ViewHolder getViewHolder(View row) {
                return getSectionHolder(row);
            }

            @Override
            public ViewHolder updateViewHolder(ViewHolder holder, ProgressItem item) {
                return updateSectionHolder(holder, item.getTitle());
            }
        };
    }

    public void addSectionItem(ProgressItem item) {
        item.setProgress(100);
        sectionedAdapter.addSectionItem(item);
    }

}
