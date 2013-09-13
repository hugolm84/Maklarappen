/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.itm.projekt.Interfaces.ItemInterface;

import java.util.ArrayList;


/**
 * BaseAdapter
 * @param <T>
 */
abstract public class BaseArrayAdapter<T> extends ArrayAdapter<T> implements ItemInterface<T> {

    private final Context mContext;
    private final ArrayList<T> mData;
    private final int mLayoutResourceId;
    private final LayoutInflater mInflater;

    public BaseArrayAdapter(Context context, int layoutResourceId, ArrayList<T> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayAdapter<T> getAdapter() {
        return this;
    }

    public int getLayoutResourceId() {
        return mLayoutResourceId;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public Context getContext() {
        return mContext;
    }

    public void addItem(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        mData.remove(item);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }

    public final ArrayList<T> getData() {
        return mData;
    }

    protected void addItemNoNotify(T item) {
        mData.add(item);
    }

    public int getCount() {
        return mData.size();
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(mLayoutResourceId, parent, false);
            holder = getViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        return updateView(position, holder, convertView);
    }

    protected View convertView(ViewGroup parent) {
        return mInflater.inflate(getLayoutResourceId(), parent, false);
    }

    protected View updateView(int position, ViewHolder holder, View view) {
        updateViewHolder(holder, mData.get(position));
        return view;
    }
}
