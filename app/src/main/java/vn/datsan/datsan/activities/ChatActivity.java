package vn.datsan.datsan.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.serverdata.chat.ChatService;
import vn.datsan.datsan.serverdata.chat.MessageService;
import vn.datsan.datsan.ui.adapters.ChatAdapter;
import vn.datsan.datsan.utils.AppLog;


public class ChatActivity extends SimpleActivity {

    private static final String TAG = ChatActivity.class.getName();

    @BindView(R.id.messageEdit)
    EditText messageEdt;
    @BindView(R.id.messagesContainer)
    ListView messagesContainer;
    @BindView(R.id.chatSendButton)
    Button sendBtn;

    @BindView(R.id.meLbl)
    TextView meLabel;
    @BindView(R.id.friendLabel)
    TextView companionLabel;
    @BindView(R.id.container)
    RelativeLayout container;

    private ChatAdapter adapter;
    private ArrayList<Message> chatHistory;

    private ChatService chatService = ChatService.getInstance();
    private MessageService messageService = MessageService.getInstance();
    private ChildEventListener messageChildEventListener;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        super.initToolBar();

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<Message>());
        messagesContainer.setAdapter(adapter);
        companionLabel.setText("My Buddy");

        chat = getIntent().getParcelableExtra("chat");
    }

    @Override
    protected void onPause() {
        super.onPause();

        messageService.getMessageDatabaseRef(chat.getId()).removeEventListener(messageChildEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMessageHistory(chat.getId());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listen to any message update for the chat group (chat group)
        messageChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (!message.isMe()) {
                    displayMessage(message);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AppLog.e(TAG, "We don't expect to have child message Changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                AppLog.e(TAG, "Remove message feature not supported yet!");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                AppLog.e(TAG, "We don't expect to have child message Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        messageService.getMessageDatabaseRef(chat.getId()).addChildEventListener(messageChildEventListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMessageHistory(String chatId) {
        if (StringUtils.isBlank(chatId)) {
            return;
        }
        AppLog.d(TAG, "loadMessageHistory for chat: " + chatId);
        // Load chat history from firebase chatService
        messageService.getMessageDatabaseRef(chatId).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return; // No data
                }

                List<Message> messageHistory = new ArrayList<Message>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    messageHistory.add(message);
                }

                // Get messages history and add to display in revert order,
                // because we want to display latest message at the bottom of the chat list, not on the top
                Collections.reverse(messageHistory); // Want to have the latest chats on top
                for (Message message : messageHistory) {
                    displayMessage(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Send message, save to firebase
     */
    @OnClick(R.id.chatSendButton)
    public void sendMessage() {
        String messageText = messageEdt.getText().toString();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        // Construct message
        User currentUser = UserManager.getInstance().getCurrentUser();
        Message message = new Message();
        message.setMessage(messageText);
        message.setUserId(currentUser.getId());
        message.setChat(chat);
        message.setUserName(currentUser.getName());
        message.setTimestamp(DateTime.now());

        // Save to Firebase
        messageService.save(message);

        // Reset input field
        messageEdt.setText("");

        // Display message
        displayMessage(message);
    }

    public void displayMessage(Message message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

}
