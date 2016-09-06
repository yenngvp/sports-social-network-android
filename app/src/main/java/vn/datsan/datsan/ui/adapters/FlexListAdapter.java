package vn.datsan.datsan.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.adapters.ImageViewBindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 7/28/16.
 */
public abstract class FlexListAdapter extends RecyclerView.Adapter<FlexListAdapter.FlexViewHolder>{

    private List<FlexItem> dataSource;
    private Context context;

    public FlexListAdapter(Context context) {
        this.context = context;
        dataSource = new ArrayList<>();
}

    public FlexListAdapter(Context context, List<FlexItem> list) {
        if (list == null)
            dataSource = new ArrayList<>();
        this.context = context;
        dataSource = list;
    }

    public void update(List<FlexItem> list) {
        dataSource.clear();
        dataSource.addAll(list);
        notifyDataSetChanged();
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
        FlexItem item = dataSource.get(position);
        holder.getTitle().setText(item.getRowTitle());
        holder.getContent().setText(item.getRowContent());
        holder.getNote().setText(item.getRowNote());
        holder.getNote().setText(item.getRowNote());
        holder.getBadge().setText(item.getRowBadge());

        String imageUrl = dataSource.get(position).getImageUrl();
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            AppLog.log(AppLog.LogType.LOG_ERROR, "flex", dataSource.get(position).getImageUrl());
//            Picasso.with(context)
//                    .load(dataSource.get(position).getImageUrl())
//                .resize(100, 100)
//                .centerCrop().into(holder.getThumb());
//        }
        setImage(context, holder.getThumb(), imageUrl);
    }

    public abstract void setImage(Context context, ImageView imageView, String url);

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
        public FlexViewHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.row_img);
            title = (TextView) itemView.findViewById(R.id.row_main_title);
            content = (TextView) itemView.findViewById(R.id.row_sub_content);
            note = (TextView) itemView.findViewById(R.id.row_extra_note);
            badge = (TextView) itemView.findViewById(R.id.row_extra_badge);
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

    public class FlexItem {
        private String imageUrl;
        private String rowTitle;
        private String rowContent;
        private String rowNote;
        private String rowBadge;

        public FlexItem(String imageUrl, String rowTitle, String rowContent, String rowNote) {
            this.imageUrl = imageUrl;
            this.rowTitle = rowTitle;
            this.rowContent = rowContent;
            this.rowNote = rowNote;
        }

        public FlexItem(String imageUrl, String rowTitle, String rowContent, String rowNote, String rowBadge) {
            this.imageUrl = imageUrl;
            this.rowTitle = rowTitle;
            this.rowContent = rowContent;
            this.rowNote = rowNote;
            this.rowBadge = rowBadge;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getRowTitle() {
            return rowTitle;
        }

        public void setRowTitle(String rowTitle) {
            this.rowTitle = rowTitle;
        }

        public String getRowContent() {
            return rowContent;
        }

        public void setRowContent(String rowContent) {
            this.rowContent = rowContent;
        }

        public String getRowNote() {
            return rowNote;
        }

        public void setRowNote(String rowNote) {
            this.rowNote = rowNote;
        }

        public String getRowBadge() {
            return rowBadge;
        }

        public void setRowBadge(String rowBadge) {
            this.rowBadge = rowBadge;
        }
    }

    public FlexItem createItem(String imageUrl, String rowTitle, String rowContent, String rowNote) {
        return new FlexItem(imageUrl, rowTitle, rowContent, rowNote);
    }

    public FlexItem createItemWithBadge(String imageUrl, String rowTitle, String rowContent, String rowNote, String badge) {
        return new FlexItem(imageUrl, rowTitle, rowContent, rowNote, badge);
    }
}
