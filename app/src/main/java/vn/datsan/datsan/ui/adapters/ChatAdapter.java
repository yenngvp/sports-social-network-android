package vn.datsan.datsan.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;

import vn.datsan.datsan.chat.models.Message;

/**
 * Created by yennguyen on 8/28/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<Message> dataSource;

    public ChatAdapter() {
        dataSource = new ArrayList<>();
    }

    public ChatAdapter(List<Message> list) {
        if (list == null) {
            dataSource = new ArrayList<>();
        }
        dataSource = list;
    }

    public void update(List<Message> list) {
        dataSource.clear();
        dataSource.addAll(list);
        notifyDataSetChanged();

    }

    public void add(Message message) {
        dataSource.add(message);
        notifyDataSetChanged();

    }

    public List<Message> getDataSource() {
        return dataSource;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chat_message, parent, false);
        ChatViewHolder vh = new ChatViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Message message = dataSource.get(position);
        setAlignment(holder, message.isMe());

        holder.getTxtMessage().setText(message.getMessage());
        if (message.isShowSentDate()) {
            holder.getTxtSentDate().setVisibility(View.VISIBLE);
            holder.getTxtSentDate().setText(message.getSentDate());
        } else {
            holder.getTxtSentDate().setVisibility(View.GONE);
        }
        if (message.getTimestampMillis() > 0) {
            holder.getTxtInfo().setText(message.getSentTime());
        }
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMessage;
        private TextView txtInfo;
        private TextView txtSentDate;
        private LinearLayout content;
        private LinearLayout contentWithBG;
        
        public ChatViewHolder(View itemView) {
            super(itemView);

            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            content = (LinearLayout) itemView.findViewById(R.id.content);
            contentWithBG = (LinearLayout) itemView.findViewById(R.id.contentWithBackground);
            txtInfo = (TextView) itemView.findViewById(R.id.txtInfo);
            txtSentDate = (TextView) itemView.findViewById(R.id.txtSentDate);
        }

        public TextView getTxtMessage() {
            return txtMessage;
        }

        public void setTxtMessage(TextView txtMessage) {
            this.txtMessage = txtMessage;
        }

        public TextView getTxtInfo() {
            return txtInfo;
        }

        public void setTxtInfo(TextView txtInfo) {
            this.txtInfo = txtInfo;
        }

        public LinearLayout getContent() {
            return content;
        }

        public void setContent(LinearLayout content) {
            this.content = content;
        }

        public LinearLayout getContentWithBG() {
            return contentWithBG;
        }

        public void setContentWithBG(LinearLayout contentWithBG) {
            this.contentWithBG = contentWithBG;
        }

        public TextView getTxtSentDate() {
            return txtSentDate;
        }

        public void setTxtSentDate(TextView txtSentDate) {
            this.txtSentDate = txtSentDate;
        }
    }

    private void setAlignment(ChatViewHolder holder, boolean isMe) {
        if (!isMe) {
            holder.getContentWithBG().setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getContentWithBG().getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
//            layoutParams.width = (int) (0.8 * AppUtils.DISPLAY_WIDTH);
            holder.getContentWithBG().setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.getContent().setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.getTxtMessage().getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;

            holder.getTxtMessage().setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.getTxtInfo().getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.getTxtInfo().setLayoutParams(layoutParams);
        } else {
            holder.getContentWithBG().setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getContentWithBG().getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
//            layoutParams.width = (int) (0.8 * AppUtils.DISPLAY_WIDTH);
            holder.getContentWithBG().setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.getContent().getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.getContent().setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.getTxtMessage().getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.getTxtMessage().setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.getTxtInfo().getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.getTxtInfo().setLayoutParams(layoutParams);
        }
    }
}

