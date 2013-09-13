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
import com.itm.projekt.R;

import java.util.ArrayList;

public class NotificationItemAdapter extends BaseArrayAdapter<NotificationItem> {

    public static class NotificationItemHolder extends ViewHolder {
        TextView title;
        TextView notificationCount;
        TextView notificationArea;
        ImageView icon;

    }

    public NotificationItemAdapter(Context context, int layoutResourceId, ArrayList<NotificationItem> data) {
        super(context, layoutResourceId, data);
    }

    @Override
    public ViewHolder getViewHolder(View row) {
        NotificationItemHolder holder = new NotificationItemHolder();
        holder.title = (TextView)row.findViewById(R.id.list_menu_item_title);
        holder.icon = (ImageView)row.findViewById(R.id.list_menu_left_icon);
        holder.notificationArea = (TextView)row.findViewById(R.id.list_menu_right_item_shape_holder);
        holder.notificationCount = (TextView)row.findViewById(R.id.list_menu_right_item_text_holder);
        return holder;
    }

    @Override
    public ViewHolder updateViewHolder(ViewHolder holder, NotificationItem item) {
        if(holder instanceof NotificationItemHolder) {

            NotificationItemHolder drawerMenuItemHolder = (NotificationItemHolder)holder;
            drawerMenuItemHolder.title.setText(item.getTitle());

            if(item.getLeftIconResource() > 0) {
                drawerMenuItemHolder.icon.setVisibility(0);
                drawerMenuItemHolder.icon.setImageResource(item.getLeftIconResource());
            }

            if(item.getNotificationCount() != 0) {
                drawerMenuItemHolder.notificationArea.setVisibility(0);
                drawerMenuItemHolder.notificationCount.setVisibility(0);
                drawerMenuItemHolder.notificationCount.setText(""+item.getNotificationCount());
            }
            return holder;
        }
        return holder;
    }

}
