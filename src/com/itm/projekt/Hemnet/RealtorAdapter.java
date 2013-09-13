/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Hemnet;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class RealtorAdapter extends ArrayAdapter<RealtorItem> {
    public RealtorAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public RealtorAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public RealtorAdapter(Context context, int textViewResourceId, RealtorItem[] objects) {
        super(context, textViewResourceId, objects);
    }

    public RealtorAdapter(Context context, int resource, int textViewResourceId, RealtorItem[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public RealtorAdapter(Context context, int textViewResourceId, List<RealtorItem> objects) {
        super(context, textViewResourceId, objects);
    }

    public RealtorAdapter(Context context, int resource, int textViewResourceId, List<RealtorItem> objects) {
        super(context, resource, textViewResourceId, objects);
    }

   /* Context context;
    ArrayList<RealtorItem> data;

    int layoutResourceId;

    static class RealtorItemHolder
    {
        TextView txtBase;
        TextView txtThumb;
    }

    public RealtorAdapter(Context context, int layoutResourceId, ArrayList<RealtorItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RealtorItemHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RealtorItemHolder();
            holder.txtThumb = (TextView)row.findViewById(R.id.list_thumb_text);
            holder.txtBase = (TextView)row.findViewById(R.id.list_base_text);
            row.setTag(holder);
        }
        else
        {
            holder = (RealtorItemHolder)row.getTag();
        }

        RealtorItem realtor = data.get(position);
        holder.txtBase.setText(realtor.getName());
        holder.txtThumb.setText(new DecimalFormat("#.#").format(realtor.getPercentage()).toString() + "%");
        return row;
    }
             */

}
