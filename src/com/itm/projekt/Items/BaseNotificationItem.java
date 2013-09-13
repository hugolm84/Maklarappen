/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Items;

import java.util.ArrayList;

public class BaseNotificationItem {

    private final   String title;
    private final   String fragmentId;
    private int notificationCount;

    private ArrayList<Object> itemData = new ArrayList<Object>();

    BaseNotificationItem(final String title, final String fragmentId) {
        this.title = title;
        this.fragmentId = fragmentId;
    }

    public void setNotificationCount(int count) {
        notificationCount = count;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public final ArrayList<Object> getItemData() {
        return itemData;
    }

    public void setItemData(ArrayList<Object> data) {
        itemData = data;
    }

    public void addItemData(Object item) {
        itemData.add(item);
    }

    public final String getFragmentId() {
        return fragmentId;
    }

    public final String getTitle() {
        return title;
    }
}
