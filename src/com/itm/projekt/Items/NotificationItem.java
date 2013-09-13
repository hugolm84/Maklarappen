/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Items;

public class NotificationItem extends BaseNotificationItem {

    private final   int leftIconResource;

    public NotificationItem(final String title, final int leftIconResource, final String fragmentId) {
        super(title, fragmentId);
        this.leftIconResource = leftIconResource;
    }

    public final int getLeftIconResource(){
        return leftIconResource;
    }

}
