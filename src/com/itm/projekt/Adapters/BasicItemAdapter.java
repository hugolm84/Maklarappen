/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.itm.projekt.Items.BasicItem;
import com.itm.projekt.R;

import java.util.ArrayList;

public class BasicItemAdapter extends BaseArrayAdapter<BasicItem> {

    ItemListener itemListener;

    public static interface ItemListener {
        void onLeftIconClicked(int pos);
        void onRightIconClicked(int pos);
    }

    private class BasicItemListener {
        int pos;

        BasicItemListener(int pos) {
            this.pos = pos;
        }

        View.OnClickListener leftViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemListener != null) {
                    itemListener.onLeftIconClicked(pos);
                }
            }
        };

        View.OnClickListener rightViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemListener != null)
                    itemListener.onRightIconClicked(pos);
            }
        };

    }

    public void setItemListener(ItemListener listener) {
        itemListener = listener;
    }

    static class BasicItemHolder extends ViewHolder {
        TextView title;
        TextView caption;
        ImageView left_icon;
        ImageView right_icon;
    }

    public BasicItemAdapter(Context context, int layoutResourceId, ArrayList<BasicItem> data) {
        super(context, layoutResourceId, data);
    }

    @Override
    public ViewHolder getViewHolder(View row) {
        BasicItemHolder holder = new BasicItemHolder();
        holder.title = (TextView)row.findViewById(R.id.basic_list_item_title);
        holder.caption = (TextView)row.findViewById(R.id.basic_list_item_summary);
        holder.left_icon = (ImageView)row.findViewById(R.id.basic_list_left_icon);
        holder.right_icon = (ImageView)row.findViewById(R.id.basic_list_right_icon);
        return holder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {
            convertView = getInflater().inflate(getLayoutResourceId(), parent, false);
            holder = getViewHolder(convertView);
            ((BasicItemHolder)holder).left_icon.setOnClickListener(new BasicItemListener(position).leftViewListener);
            ((BasicItemHolder)holder).right_icon.setOnClickListener(new BasicItemListener(position).rightViewListener);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        return updateView(position, holder, convertView);
    }

    @Override
    public ViewHolder updateViewHolder(ViewHolder holder, BasicItem item) {

        if(holder instanceof BasicItemHolder) {

            BasicItemHolder itemHolder = (BasicItemHolder)holder;
            int leftIcon = item.getLeftIconResource();
            int rightIcon = item.getRightIconResource();

            itemHolder.title.setText(item.getTitle());

            if(!item.getSummary().isEmpty()) {
                itemHolder.caption.setText(item.getSummary());
                itemHolder.caption.setVisibility(View.VISIBLE);
            }
            else {
                itemHolder.caption.setVisibility(View.INVISIBLE);
            }

            if(leftIcon > 0) {
                itemHolder.left_icon.setVisibility(0);
                itemHolder.left_icon.setImageResource(leftIcon);
            }
            if(rightIcon > 0) {
                itemHolder.right_icon.setVisibility(0);
                itemHolder.right_icon.setImageResource(rightIcon);
            }

            return holder;
        }
        return holder;
    }

}
