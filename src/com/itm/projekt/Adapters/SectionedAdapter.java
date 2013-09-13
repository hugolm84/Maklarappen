/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.itm.projekt.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * SectionedAdapter
 * @param <T>
 * This class creates an Adapter that is presented in a ListView:
 *      Section
 *          - Item
 *          - Item
 *      Section 2
 *          - Item ...
 */
public abstract class SectionedAdapter<T> extends BaseArrayAdapter<T> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SECTION = 1;

    private final int mLayoutSectionHeaderId;

    private TreeSet<Integer> mSectionSet = new TreeSet<Integer>();

    protected static class SectionHolderView extends ViewHolder {
        TextView title;
    }

    public SectionedAdapter(Context context, int layoutResourceId, int layoutSectionHeaderId, ArrayList<T> data) {
        super(context, layoutResourceId, data);
        mLayoutSectionHeaderId = layoutSectionHeaderId;
    }

    public void addSectionItem(final T item) {
        addItemNoNotify(item);
        mSectionSet.add(getCount() - 1);
        notifyDataSetChanged();
    }

    protected ViewHolder getSectionHolder(View row) {
        SectionHolderView holder = new SectionHolderView();
        holder.title = (TextView)row.findViewById(R.id.list_section_header);
        return holder;
    }

    protected ViewHolder updateSectionHolder(ViewHolder holder, final String title) {
        if(holder instanceof SectionHolderView)
            ((SectionHolderView)holder).title.setText(title);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return mSectionSet.contains(position) ? TYPE_SECTION : TYPE_ITEM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);

        if (convertView == null) {
            switch (type) {
                case TYPE_ITEM:
                    convertView = convertView(parent);
                    holder = getViewHolder(convertView);
                    convertView.setTag(holder);
                    break;
                case TYPE_SECTION:
                    convertView = getInflater().inflate(mLayoutSectionHeaderId, parent, false);
                    holder = getSectionHolder(convertView);
                    break;
            }
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        return updateView(position, holder, convertView);
    }

}