/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Adapters;

import android.content.Context;
import android.view.View;
import com.itm.projekt.Items.NotificationItem;

import java.util.ArrayList;

public class SectionedNotificationAdapter extends NotificationItemAdapter {

    private SectionedAdapter<NotificationItem> sectionedAdapter;

    public SectionedNotificationAdapter(Context context, int layoutResourceId, int layoutSectionHeaderId, ArrayList<NotificationItem> data) {
        super(context, layoutResourceId, data);
        sectionedAdapter = new SectionedAdapter<NotificationItem>(context, layoutResourceId, layoutSectionHeaderId, data) {
            @Override
            public ViewHolder getViewHolder(View row) {
                return getSectionHolder(row);
            }

            @Override
            public ViewHolder updateViewHolder(ViewHolder holder, NotificationItem item) {
                return updateSectionHolder(holder, item.getTitle());
            }
        };
    }

    public void addSectionItem(NotificationItem item) {
        sectionedAdapter.addSectionItem(item);
    }

}
