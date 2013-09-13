/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Items;

import java.util.ArrayList;

public class BasicItem {

    private final   String title;
    private final   String summary;

    private final   int leftIconResource;
    private final   int rightIconResource;
    private ArrayList<Object> itemData = new ArrayList<Object>();

    public BasicItem(final String title, final String summary, final int leftIconResource, final int rightIconResource) {
        this.title = title;
        this.summary = summary;
        this.leftIconResource = leftIconResource;
        this.rightIconResource = rightIconResource;
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

    public final String getTitle() {
        return title;
    }

    public final String getSummary() {
        return summary;
    }

    public final int getLeftIconResource(){
        return leftIconResource;
    }

    public final int getRightIconResource() {
        return rightIconResource;
    }
}
