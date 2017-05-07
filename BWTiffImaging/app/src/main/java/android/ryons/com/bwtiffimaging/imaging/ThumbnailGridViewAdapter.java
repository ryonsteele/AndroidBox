package android.ryons.com.bwtiffimaging.imaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.ryons.com.bwtiffimaging.R;

import java.util.ArrayList;
import java.util.List;

class ThumbnailGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<ThumbnailViewItem> mItems;

    public ThumbnailGridViewAdapter(Context context) {
        this.mContext = context;
        this.mItems = new ArrayList<>();
    }

    public void addItem(ThumbnailViewItem item) {
        this.mItems.add(item);
        this.notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mItems.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.thumbnail_grid_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // update the item view
        ThumbnailViewItem item = mItems.get(position);
        viewHolder.ivIcon.setImageBitmap(item.getThumbnail());
        return convertView;
    }
    /**
     * The view holder design pattern prevents using findViewById()
     * repeatedly in the getView() method of the adapter.
     *
     * http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView ivIcon;

        public ViewHolder(View convertView) {
            ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        }
    }
}