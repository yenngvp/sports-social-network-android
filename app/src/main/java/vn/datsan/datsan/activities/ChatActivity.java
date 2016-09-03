package vn.datsan.datsan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.models.chat.MessageType;
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
    @BindView(R.id.chatSendButton) Button sendBtn;

    @BindView(R.id.chat_container) RelativeLayout chat_container;

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    private ChatService chatService = ChatService.getInstance();
    private MessageService messageService = MessageService.getInstance();
    private ChildEventListener incomingMessageEventListener;
    private Chat chat;

    private String startAt;
    private String endAt;
    private Message myLatestLocalMessage;

    private volatile TypingSignal myTypingSignal;
    private volatile List<TypingSignal> incomingTypingSignals = new ArrayList<>();

    // Timer handler for handling typing timeout
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            // Check for current user typing timeout, ie. user focusing text input field but stopped typing for awhile
            long now = DateTime.now().getMillis();

            if (myTypingSignal != null
                    && (now - myTypingSignal.getTimestamp() > Constants.TYPING_SIGNAL_TIMEOUT_MILLIS)) {
                // Stop my typing
                MessageService.getInstance().stopTypingSignal(chat.getId(), myTypingSignal);
                myTypingSignal = null;
                AppLog.d("TYPING_SIGNAL", "Stopped my typing due to timedout!");
            }

            // Check any incoming signal timed out
            for (TypingSignal signal : incomingTypingSignals) {
                if (now - signal.getTimestamp() > Constants.TYPING_SIGNAL_TIMEOUT_MILLIS) {
                    MessageService.getInstance().stopTypingSignal(chat.getId(), signal);
                }
            }

            // Update UI
            AppLog.d("TYPING_SIGNAL", "After timeout There are " + incomingTypingSignals.size() + " typing ...");

            // Delay some millis for the next check
            timerHandler.postDelayed(this, Constants.TYPING_SIGNAL_TIMEOUT_MILLIS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        super.initToolBar();

        chatAdapter = new ChatAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        chat = getIntent().getParcelableExtra("chat");
        getSupportActionBar().setTitle(chat.getDynamicChatTitle());

        final String chatId = chat.getId();

        // Listen on message input
        messageEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                User currentUser = UserManager.getInstance().getCurrentUser();
                int len = charSequence.length();

                // Stopped typing when user clear the input
                if (len == 0 && myTypingSignal != null) {
                    MessageService.getInstance().stopTypingSignal(chatId, myTypingSignal);
                    myTypingSignal = null;
                } else {
                    // Check if passing the keep-alive interval
                    boolean passedKeepAliveInterval = false;
                    if (myTypingSignal != null) {
                        long now = DateTime.now().getMillis();
                        if (now - myTypingSignal.getTimestamp() > Constants.TYPING_SIGNAL_KEEP_ALIVE_MILLIS) {
                            passedKeepAliveInterval = true;
                        }
                    }

                    // Start typing or keep alive typing signal
                    if (myTypingSignal == null || passedKeepAliveInterval) {
                        if (myTypingSignal == null) {
                            myTypingSignal = new TypingSignal(currentUser.getId(), currentUser.getName());
                        }
                        myTypingSignal.setTimestamp(DateTime.now().getMillis());
                        MessageService.getInstance().startTypingSignal(chatId, myTypingSignal);

                        AppLog.d("TYPING_SIGNAL", myTypingSignal.toString());
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove timer handler
        timerHandler.removeCallbacks(timerRunnable);

        if (incomingMessageEventListener != null) {
            messageService.getMessageDatabaseRef(chat.getId()).removeEventListener(incomingMessageEventListener);
            incomingMessageEventListener = null;
        }

        if (myTypingSignal != null) {
            MessageService.getInstance().stopTypingSignal(chat.getId(), myTypingSignal);
            myTypingSignal = null;
        }

        MessageService.getInstance().removeTypingSignalListeners(chat.getId());

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Listen on new incoming messages
        listenOnIncomingMessage();

        // Listen on message incoming typing signals
        listenOnIncomingTypingSignals();

        // Start timer handler
        timerHandler.postDelayed(timerRunnable, Constants.TYPING_SIGNAL_TIMEOUT_MILLIS);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ChatRecentActivity.class));
        finish();
    }

    private void listenOnIncomingMessage() {

        if (incomingMessageEventListener == null) {
            // Listen to any incoming message to the chat group
            incomingMessageEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setId(dataSnapshot.getKey());
                    displayMessage(message, true); // display others incoming message
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
                    .orderByKey()
                    .limitToLast(Constants.CHAT_HISTORY_PAGINATION_SIZE_DEFAULT)
                    .addChildEventListener(incomingMessageEventListener);
        }
    }

    private void listenOnIncomingTypingSignals() {
        MessageService.getInstance().listenOnTypingSignal(chat.getId(), new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {

                TypingSignal signal = (TypingSignal) result;
                User currentUser = UserManager.getInstance().getCurrentUser();

                if (signal != null && signal.getUserId() != currentUser.getId()) {

                    if (!signal.isStopped() && incomingTypingSignals.size() == 0) {
                        incomingTypingSignals.add(signal);
                    } else {
                        for (TypingSignal s : incomingTypingSignals) {
                            if (signal.getUserId().equals(s.getUserId())) {
                                incomingTypingSignals.remove(s); // stopped
                                if (!signal.isStopped()) {
                                    // Update signal
                                    incomingTypingSignals.add(signal);
                                }
                                break;
                            }
                        }
                    }
                    // Update UI
                    AppLog.d("TYPING_SIGNAL", "There are " + incomingTypingSignals.size() + " typing ...");
                }
            }
        });
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
        Message message = new Message(MessageType.TEXT_MESSAGE);
        message.setMessage(messageText);
        message.setUserId(currentUser.getId());
        message.setChat(chat);
        message.setUserName(currentUser.getName());

        // Send to Firebase
        myLatestLocalMessage = messageService.send(message);

        // Reset input field
        messageEdt.setText("");

        // Display message
        displayMessage(myLatestLocalMessage, false);
    }

    private synchronized void displayMessage(Message message, boolean isServerMessage) {
        if (message.isMe() && isServerMessage && chatAdapter.getDataSource().contains(message)) {
            // I just received a new sending message of myself, I don't want to display duplicate it
            int i = chatAdapter.getDataSource().indexOf(message);
            Message localMessageWithServerValue = chatAdapter.getDataSource().get(i);
            localMessageWithServerValue.setTimestamp(message.getTimestampMillis());
            chatAdapter.getDataSource().set(i, localMessageWithServerValue);
            chatAdapter.notifyDataSetChanged();
        } else {
            chatAdapter.add(message);
            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
        }
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
