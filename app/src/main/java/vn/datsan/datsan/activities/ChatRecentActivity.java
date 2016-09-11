package vn.datsan.datsan.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.serverdata.chat.ChatService;
import vn.datsan.datsan.ui.adapters.DividerItemDecoration;
import vn.datsan.datsan.ui.adapters.FlexListAdapter;
import vn.datsan.datsan.ui.adapters.RecyclerTouchListener;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.AppConstants;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.AppUtils;
import vn.datsan.datsan.utils.ChatComparator;

/**
 * Created by yennguyen on 8/17/16.
 */
public class ChatRecentActivity extends SimpleActivity {

    private static final String TAG = ChatRecentActivity.class.getSimpleName();
    private static final String EVENT_LISTENER = "EVENT_LISTENER";

    FlexListAdapter adapter;
    List<Chat> recentChats;
    ValueEventListener userChatEventListener;
    Map<String, ValueEventListener> chatEventListeners = new HashMap<>();

    SimpleProgress progress;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.delete_chat_button) Button deleteChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_recent);
        ButterKnife.bind(this);

        super.initToolBar();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlexListAdapter(ChatRecentActivity.this) {

            @Override
            public void setImage(Context context, ImageView imageView, String url) {

            }
        };
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (recentChats == null || recentChats.isEmpty()) {
                    return;
                }

                // Starting chat
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                FlexListAdapter.FlexItem chatItem = adapter.getDataSource().get(position);
                Chat searchChat = new Chat();
                searchChat.setId(chatItem.getId());
                intent.putExtra("chat", recentChats.get(recentChats.indexOf(searchChat)));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

                final FlexListAdapter.FlexItem chatItem = adapter.getDataSource().get(position);
                AppLog.d(TAG, "onLongClick:chatId:" + chatItem.getId());

                // Show menu pop-up
                PopupMenu popupMenu = new PopupMenu(view.getContext(), deleteChatButton);

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_context_recent_chat, popupMenu.getMenu());

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ChatService.getInstance().deleteChat(chatItem.getId());
                        adapter.getDataSource().remove(chatItem);
                        adapter.notifyDataSetChanged();
                        return false;
                    }
                });
            }
        }));

        FloatingActionButton createChatBtn = (FloatingActionButton) findViewById(R.id.create);
        createChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create chat
                Intent intent = new Intent(view.getContext(), CreateChatActivity.class);
                startActivity(intent);
            }
        });

        recentChats = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatService.getInstance().removeDatabaseRefListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserManager.getInstance().getCurrentUser() == null
                && FirebaseAuth.getInstance().getCurrentUser() != null) {

            final SimpleProgress progress = new SimpleProgress(this, null);
            progress.show();

            UserManager.getInstance().getCurrentUserInfo(new CallBack.OnResultReceivedListener() {
                @Override
                public void onResultReceived(Object result) {
                    UserManager.getInstance().setCurrentUser((User) result);
                    // Load chat history
                    loadRecentChat();

                    progress.dismiss();
                }
            });
        } else {
            // Load chat history
            loadRecentChat();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove all listeners
        if (userChatEventListener != null) {
            UserManager.getInstance().getCurrentUserChatDatabaseRef().removeEventListener(userChatEventListener);
            userChatEventListener = null;
            AppLog.d(EVENT_LISTENER, "Removed childEventListener: " + UserManager.getInstance().getCurrentUser().getName());
        }

        if (chatEventListeners.size() > 0) {
            for (Map.Entry<String, ValueEventListener> entry : chatEventListeners.entrySet()) {
                String chatId = entry.getKey();
                ValueEventListener listener = entry.getValue();
                ChatService.getInstance().getChatDatabaseRef(chatId).removeEventListener(listener);

                AppLog.d(EVENT_LISTENER, "Removed valueEventListener: " + chatId);
            }
        }
        chatEventListeners.keySet().removeAll(chatEventListeners.keySet());
        AppLog.d(EVENT_LISTENER, "chatEventListeners size: " + chatEventListeners.size());
    }

    private void loadRecentChat() {
        progress = new SimpleProgress(this, getString(R.string.loading_recent_chat));
        progress.show();

        if (userChatEventListener == null) {
            userChatEventListener = createUserChatsEventListener();
            UserManager.getInstance().getCurrentUserChatDatabaseRef()
                    .limitToLast(AppConstants.CHAT_HISTORY_PAGINATION_SIZE_DEFAULT)
                    .addValueEventListener(userChatEventListener);
        }
    }

    private ValueEventListener createUserChatsEventListener() {
        AppLog.d(EVENT_LISTENER, "Create ValueEventListener");

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AppLog.d(EVENT_LISTENER, ":ValueEventListener:onDataChange");
                if (dataSnapshot.getValue() == null) {
                    return; // there is no recent chat
                }

                recentChats.removeAll(recentChats);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Chat chat = data.getValue(Chat.class);
                    if (chat == null) {
                        return;
                    }
                    chat.setId(data.getKey());
                    recentChats.add(chat);
                    getLatestChatStatus(chat);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void getLatestChatStatus(Chat chat) {

        // Get the latest status of the chat,
        // Current user may offline sometimes so he may don't have the latest chat
        // Tell him there are how many messages unread
        if (!chatEventListeners.containsKey(chat.getId())) {
            ValueEventListener listener = createChatValueEventListener();
            ChatService.getInstance().getChatDatabaseRef(chat.getId())
                    .addValueEventListener(listener);
            chatEventListeners.put(chat.getId(), listener);
        }
    }

    private ValueEventListener createChatValueEventListener() {

        AppLog.d(EVENT_LISTENER, "Create ValueEventListener");

        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progress.dismiss();

                AppLog.d(EVENT_LISTENER, ":ValueEventListener:onDataChange");

                Chat latestChat = dataSnapshot.getValue(Chat.class);
                if (latestChat == null) {
                    AppLog.e(EVENT_LISTENER, "Cannot load the status of the chat");
                    return; // Something not correct
                }
                latestChat.setId(dataSnapshot.getKey());

                int i = recentChats.indexOf(latestChat);
                Chat myChat = recentChats.get(i);
                latestChat.setUnreadMessageCount(latestChat.getMessageCount() - myChat.getMessageCount());
                if (latestChat.getUnreadMessageCount() < 0) {
                    latestChat.setUnreadMessageCount(0); // Concurrent update went wrong?
                }

                FlexListAdapter.FlexItem item = createItemFromChat(latestChat);
                adapter.getDataSource().remove(item);
                adapter.addOrReplaceAndResort(item);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AppLog.e(EVENT_LISTENER, "Cannot load recent chat: onCancelled:" + databaseError);
            }
        };
    }

    private FlexListAdapter.FlexItem createItemFromChat(Chat chat) {

        final boolean oneToOneChat;
        final String title = chat.getDynamicChatTitle();
        if (title != null && title.indexOf(AppConstants.GROUP_NAME_SEPARATOR) > 0) {
            oneToOneChat = false; // The chat has more than 2 members
        } else {
            oneToOneChat = true;
        }
        String content = null;
        if (chat.getLastMessage() != null) {
            content = chat.getLastMessage().getMessage();

            // Not show the last sender name if there is an one-to-one chat
            if (!oneToOneChat && chat.getLastMessage().getUserName() != null) {
                String senderName = chat.getLastMessage().getUserName();
                if (senderName.length() > 18) {
                    senderName = senderName.substring(0, 15) + "...";
                }
                content = senderName + ": " + content;
            }
        }
        String timestamp = AppUtils.getDateTimeAsString(chat.getLastModifiedTimestampMillis(),
                AppUtils.DATETIME_ddMMyy_FORMATTER);
        String badge = chat.getUnreadMessageCount() > 0 ? String.valueOf(chat.getUnreadMessageCount()) : null;

        FlexListAdapter.FlexItem item = adapter.createItemWithBadge(chat.getId(), null, title, content, timestamp, badge);
        item.setSortingWeight(chat.getLastModifiedTimestampMillis());
        return item;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Back to Home
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
