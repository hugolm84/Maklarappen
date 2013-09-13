/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Listeners;

import android.view.View;

public class ViewHolderListener implements View.OnClickListener {


    private int itemId = -1;
    private ItemListener itemListener;
    public static interface ItemListener {
        public void onItemClicked(int pos);
    }

    public ViewHolderListener(int itemId,ItemListener listener) {
        this.itemId = itemId;
        this.itemListener = listener;
    }

    @Override
    public void onClick(View v) {
       if(itemListener != null)
           itemListener.onItemClicked(itemId);
    }
}
