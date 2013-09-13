/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Interfaces;

import android.view.View;

/**
 * This is the ItemInterface
 * @param <T>
 */
public interface ItemInterface<T> {

    abstract class ViewHolder {}
    abstract ViewHolder getViewHolder(View row);
    abstract ViewHolder updateViewHolder(ViewHolder holder, T item);


}
