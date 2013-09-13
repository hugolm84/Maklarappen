/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Items;


import java.util.ArrayList;

public class ProgressItem extends BaseNotificationItem {

    private final   String summary;
    private int progress = 0;
    private ArrayList<BasicItem> itemData = new ArrayList<BasicItem>();
    private Object customObject = null;

    public ProgressItem(final String title, final String summary, final String fragmentId) {
        super(title, fragmentId);
        this.summary = summary;
    }

    public ProgressItem(final String title, final String summary, final int progress, final String fragmentId) {
        super(title, fragmentId);
        this.progress = progress;
        this.summary = summary;
    }

    public void setCustomObject(Object object) {
        this.customObject = object;
    }

    public Object getCustomObject() {
        return customObject;
    }

    public final ArrayList<BasicItem> getBasicItemData() {
        return itemData;
    }

    public void setBasicItemData(ArrayList<BasicItem> data) {
        itemData = data;
    }

    public void addItemData(BasicItem item) {
        itemData.add(item);
    }

    public void setProgress(final int progress) {
        this.progress = progress;
    }

    public final int getProgress() {
        return progress;
    }

    public final String getSummary() {
        return summary;
    }

}
