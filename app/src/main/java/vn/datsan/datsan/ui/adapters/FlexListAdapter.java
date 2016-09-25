package vn.datsan.datsan.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.adapters.ImageViewBindingAdapter;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.ChatFlexItemComparator;

/**
 * Created by xuanpham on 7/28/16.
 */
public abstract class FlexListAdapter extends RecyclerView.Adapter<FlexListAdapter.FlexViewHolder>{

    private Context context;
    private List<ViewItem> dataSource;

    public FlexListAdapter(Context context) {
        this.context = context;
        dataSource = new ArrayList<>();
    }

    public FlexListAdapter(Context context, List<ViewItem> list) {
        if (list == null) {
            dataSource = new ArrayList<>();
        }
        this.context = context;
        dataSource = list;
    }

    public void update(List<? extends ViewItem> list) {
        dataSource.clear();
        dataSource.addAll(list);
        notifyDataSetChanged();
    }

    public List<? extends ViewItem> getDataSource() {
        return dataSource;
    }

    public void add(ViewItem item) {
        dataSource.add(item);
    }

    public void remove(ViewItem item) {
        dataSource.remove(item);
    }

    public void addOrReplaceAndResort(ViewItem item) {
        if (dataSource.contains(item)) {
            dataSource.set(dataSource.indexOf(item), item);
        } else {
            dataSource.add(item);
        }
        Collections.sort(dataSource, new ChatFlexItemComparator());
    }

    public ViewItem get(int i) {
        return dataSource.get(i);
    }

    @Override
    public FlexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_flex_item, parent, false);
        FlexViewHolder vh = new FlexViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FlexViewHolder holder, int position) {
        ViewItem item = (ViewItem) dataSource.get(position);
        holder.getTitle().setText(item.getRowTitle());
        holder.getContent().setText(item.getRowContent());
        holder.getNote().setText(item.getRowNote());
        if (item.getRowBadge() == null) {
            holder.getBadge().setVisibility(View.INVISIBLE);
        } else {
            holder.getBadge().setVisibility(View.VISIBLE);
            holder.getBadge().setText(item.getRowBadge());
            // Highlight unread content
            holder.getContent().setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }

         setImage(context, holder.getThumb(), item.getImageUrl());
    }

    public abstract void setImage(Context context, ImageView imageView, String imageUrl);

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class FlexViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumb;
        private TextView title;
        private TextView content;
        private TextView note;
        private TextView badge;
        public FlexViewHolder(View viewItem) {
            super(viewItem);

            thumb = (ImageView) viewItem.findViewById(R.id.row_img);
            title = (TextView) viewItem.findViewById(R.id.row_main_title);
            content = (TextView) viewItem.findViewById(R.id.row_sub_content);
            note = (TextView) viewItem.findViewById(R.id.row_extra_note);
            badge = (TextView) viewItem.findViewById(R.id.row_extra_badge);
        }

        public ImageView getThumb() {
            return thumb;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getContent() {
            return content;
        }

        public TextView getNote() {
            return note;
        }

        public TextView getBadge() {
            return badge;
        }
    }

    public interface ViewItem {

        String getItemId();
        String getImageUrl();
        String getRowTitle();
        String getRowContent();
        String getRowNote();
        String getRowBadge();
        long getSortingValue();
        boolean equals(Object obj);
    }
}
