package com.itm.projekt.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hugo
 * Date: 8/24/13
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class ExpandableListItemAdapter<T, T2> extends BaseExpandableListAdapter {

    private List<T> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<T, List<T2>> _listDataChild;

    public ExpandableListItemAdapter(Context context, List<T> listDataHeader,
                                 HashMap<T, List<T2>> listChildData) {
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    public void addChilds(T item, ArrayList<T2> childs) {
        _listDataChild.put(item, childs);
        notifyDataSetChanged();
    }

    public void addHeader(T item) {
        _listDataHeader.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        _listDataChild.clear();
        _listDataHeader.clear();
        notifyDataSetChanged();
    }

    @Override
    public T2 getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    abstract public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent);
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public T getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    abstract public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent);


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
