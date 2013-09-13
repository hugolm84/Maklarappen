/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.itm.projekt.Items.NotificationItem;
import com.itm.projekt.Items.ProgressButton;
import com.itm.projekt.Items.ProgressItem;
import com.itm.projekt.R;

import java.util.ArrayList;

public class ProgressItemAdapter extends BaseArrayAdapter<ProgressItem> {

    public static class ProgressItemHolder extends NotificationItemAdapter.NotificationItemHolder {
        TextView captionText;
        ProgressButton button;
    }

    public ProgressItemAdapter(Context context, int layoutResourceId, ArrayList<ProgressItem> data) {
        super(context, layoutResourceId, data);
    }

    @Override
    public ViewHolder getViewHolder(View row) {
        ProgressItemHolder holder = new ProgressItemHolder();
        holder.title = (TextView)row.findViewById(R.id.item_title);
        holder.captionText = (TextView)row.findViewById(R.id.item_caption);
        holder.button = (ProgressButton)row.findViewById(R.id.item_progress);
        holder.notificationArea = (TextView)row.findViewById(R.id.list_item_right_shape_holder);
        holder.notificationCount = (TextView)row.findViewById(R.id.list_item_right_text_holder);
        return holder;
    }

    @Override
    public ViewHolder updateViewHolder(ViewHolder holder, ProgressItem item) {
        if(holder instanceof ProgressItemHolder) {

            ProgressItemHolder itemHolder = (ProgressItemHolder)holder;
            itemHolder.title.setText(item.getTitle());
            itemHolder.button.setProgress(item.getProgress());
            itemHolder.captionText.setText(item.getSummary());

            if(item.getNotificationCount() != 0) {
                itemHolder.notificationArea.setVisibility(0);
                itemHolder.notificationCount.setVisibility(0);
                itemHolder.notificationCount.setText(""+item.getNotificationCount());
            }
            return holder;
        }
        return holder;
    }

}
