package vn.datsan.datsan.ui.appviews;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.datsan.datsan.BR;
import vn.datsan.datsan.R;
import vn.datsan.datsan.utils.AppLog;

import java.util.List;

/**
 * Created by xuanpham on 7/22/16.
 */
public class SubjectInfoAdapter extends RecyclerView.Adapter<SubjectInfoAdapter.ViewHolder> {
    private List<DataItem> dataSet;

    public SubjectInfoAdapter(List<DataItem> listData) {
        dataSet = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_info_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataItem item = dataSet.get(position);
        holder.getBinding().setVariable(BR.dataItem, item);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void updateDataSet(List<DataItem> dataItems) {
        dataSet.clear();
        dataSet.addAll(dataItems);
        notifyDataSetChanged();
    }

    public DataItem createDataItem(String subject, String info, DataType type) {
        return new DataItem(subject, info, type);
    }

    public class DataItem {
        private String subject;
        private String info;
        private DataType type;

        public DataItem(String subject, String info, DataType type) {
            this.subject = subject;
            this.info = info;
            this.type = type;
        }

        public DataItem(String subject, String info) {
            this.subject = subject;
            this.info = info;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public DataType getType() {
            return type;
        }

        public void setType(DataType type) {
            this.type = type;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding viewDataBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            viewDataBinding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding() {
            return viewDataBinding;
        }
    }

    public enum DataType {
        TEXT,
        DATE_TIME
    }
}
