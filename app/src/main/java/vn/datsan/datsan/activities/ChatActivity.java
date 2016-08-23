package vn.datsan.datsan.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.models.chat.TypingSignal;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.serverdata.chat.ChatService;
import vn.datsan.datsan.serverdata.chat.MessageService;
import vn.datsan.datsan.ui.adapters.ChatAdapter;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;


public class ChatActivity extends SimpleActivity {

    private static final String TAG = ChatActivity.class.getName();

    @BindView(R.id.messageEdit) EditText messageEdt;
    @BindView(R.id.messagesContainer) ListView messagesContainer;
    @BindView(R.id.chatSendButton) Button sendBtn;

    @BindView(R.id.meLbl) TextView meLabel;
    @BindView(R.id.friendLabel) TextView companionLabel;
    @BindView(R.id.container) RelativeLayout container;

    private ChatAdapter adapter;

    private ChatService chatService = ChatService.getInstance();
    private MessageService messageService = MessageService.getInstance();
    private ChildEventListener incomingMessageEventListener;
    private Chat chat;

    private volatile List<Message> messageHistory;

    private String startAt;
    private String endAt;
    private Message latestMessage;

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

        messageHistory = new ArrayList<>();

        final String chatId = chat.getId();
        // Listen on message input
        messageEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AppLog.d("beforeTextChanged: " + charSequence.length());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                AppLog.d("onTextChanged: " + charSequence.length());

                User currentUser = UserManager.getInstance().getCurrentUser();
                TypingSignal signal = new TypingSignal();
                signal.setUserTyping(currentUser.getName());
                messageService.sendTypingSignal(chatId, signal);
                AppLog.d(currentUser.getName() + " typing a message ...");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                AppLog.d("afterTextChanged: ");
            }
        });

        // Listen on message typing signal
        MessageService.getInstance().listenOnTypingSignal(chat.getId(), new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {

                TypingSignal typingSignal = (TypingSignal) result;
                if (typingSignal != null) {
                    AppLog.d(typingSignal.toString());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (incomingMessageEventListener != null) {
            messageService.getMessageDatabaseRef(chat.getId()).removeEventListener(incomingMessageEventListener);
            incomingMessageEventListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load message for the chat in following procedure:
        // 1. Load message history from local database first
        // 2. Get the latest local message
        // 3. Load messages from the server that newer than the local
        // 4. Listen on any incoming message to the group (message child event)

        loadLocalMessageHistory();
        loadServerMessageHistory();

        // Listen on new incoming messages
        listenOnIncomingMessage();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    private void loadLocalMessageHistory() {
        String chatId = chat.getId();


    }

    private void loadServerMessageHistory() {

        String chatId = chat.getId();

        AppLog.d(TAG, "loadServerMessageHistory for chat: " + chatId);

        // Load chat history from firebase chatService
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return; // No data
                }

                List<Message> messageServerHistory = new ArrayList<Message>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    messageServerHistory.add(message);
                }

                // Reset view and old messages
                messageHistory.removeAll(messageHistory);
                // Update new server messages
                messageHistory.addAll(messageServerHistory);
                // Display messages
                adapter.add(messageHistory);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (endAt == null) {
            // First page load
            // Load latest number of CHAT_HISTORY_PAGINATION_SIZE_DEFAULT messages from the server
            messageService.getMessageDatabaseRef(chatId)
                    .orderByKey()
                    .limitToLast(Constants.CHAT_HISTORY_PAGINATION_SIZE_DEFAULT)
                    .addListenerForSingleValueEvent(valueEventListener);
        } else {
            // Response to scroll event
            // Load latest number of CHAT_HISTORY_PAGINATION_SIZE_DEFAULT messages from the server
            // but specify startAt and endAt key index
            messageService.getMessageDatabaseRef(chatId)
                    .orderByKey()
                    .endAt(startAt)
                    .limitToLast(Constants.CHAT_HISTORY_PAGINATION_SIZE_DEFAULT)
                    .addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void listenOnIncomingMessage() {

        if (incomingMessageEventListener == null) {
            // Listen to any incoming message to the chat group
            incomingMessageEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.isMe()) {
                        // we know that we just receive a message of current user, so it means user successfully sent message to the server
                        // show signal to the user to indicate "Sent" status
                        AppLog.d("Received my message");
                    } else {
                        displayMessage(message); // display others incoming message
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
            messageService.getMessageDatabaseRef(chat.getId())
                    .addChildEventListener(incomingMessageEventListener);
        }
    }

    /**
     * Send message, send to firebase
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

        latestMessage = message;

        // Save to the local database


        // Send to Firebase
        messageService.send(message);

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

    private void showSendingIndicator() {

    }

    private void dismissSendingIndicator() {

    }

    private void showSentIndicator() {

    }

    private void showReadIndicator() {

    }

    private void showTypingIndicator() {

    }
}
