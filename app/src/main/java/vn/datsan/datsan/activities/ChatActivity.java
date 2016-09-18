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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.ServerTime;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.models.chat.Message;
import vn.datsan.datsan.models.chat.MessageType;
import vn.datsan.datsan.models.chat.TypingSignal;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.ServerTimeService;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.serverdata.chat.ChatService;
import vn.datsan.datsan.serverdata.chat.MessageService;
import vn.datsan.datsan.ui.adapters.ChatAdapter;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.AppConstants;
import vn.datsan.datsan.utils.AppLog;


public class ChatActivity extends SimpleActivity {

    private static final String TAG = ChatActivity.class.getName();

    @BindView(R.id.messageEdit) EditText messageEdt;
    @BindView(R.id.chatSendButton) Button sendBtn;
    @BindView(R.id.whoTypingTxt) TextView whoTypingTxt;

    @BindView(R.id.chat_container) RelativeLayout chat_container;

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    private ChatService chatService = ChatService.getInstance();
    private MessageService messageService = MessageService.getInstance();
    private ChildEventListener incomingMessageEventListener;
    private ValueEventListener chatValueEventListener;
    private volatile Chat chat;

    private String startAt;
    private String endAt;
    private Message myLatestLocalMessage;

    private volatile TypingSignal myTypingSignal;
    private volatile List<TypingSignal> incomingTypingSignals;

    final User currentUser = UserManager.getInstance().getCurrentUser();

    // Timer handler for handling typing timeout
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            // Check for current user typing timed out, ie. user focusing text input field but stopped typing for awhile
            if (isTimedOut(myTypingSignal.getTimestamp(), AppConstants.TYPING_SIGNAL_TIMEOUT_MILLIS)) {
                // Stop my typing
                MessageService.getInstance().stopTyping(chat.getId(), myTypingSignal);
                AppLog.d("TYPING_SIGNAL", "Stopped my typing due to timed out!");
            }

            // Check any incoming signal timed out
            if (incomingTypingSignals.size() > 0) {
                for (TypingSignal signal : incomingTypingSignals) {
                    if (signal.isTyping()) {
                        if (isTimedOut(signal.getTimestamp(), AppConstants.TYPING_SIGNAL_TIMEOUT_MILLIS)) {
                            // Force timed out the user
                            signal.setTyping(false);
                        }
                    }
                }

                // Update UI
                updateTypingIndicator();
            }

            // Delay some millis for the next check
            timerHandler.postDelayed(this, AppConstants.TYPING_SIGNAL_TIMEOUT_MILLIS);
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

        // Typing signals
        myTypingSignal = new TypingSignal(currentUser.getId(), currentUser.getName());
        incomingTypingSignals = new ArrayList<>();

        // Listen on message input
        messageEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int len = charSequence.length();

                if (len == 0) {
                    // Stop typing when user clear the input
                    MessageService.getInstance().stopTyping(chatId, myTypingSignal);
                } else {
                    // Continuing typing

                    // Check if passed the keep-alive interval
                    if (!myTypingSignal.isTyping()
                            || isTimedOut(myTypingSignal.getTimestamp(), AppConstants.TYPING_SIGNAL_KEEP_ALIVE_MILLIS)) {
                        MessageService.getInstance().startTyping(chatId, myTypingSignal);

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
            MessageService.getInstance().stopTyping(chat.getId(), myTypingSignal);
        }

        if (chatValueEventListener != null) {
            chatService.getChatDatabaseRef(chat.getId()).removeEventListener(chatValueEventListener);
            chatValueEventListener = null;
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

        // Listen on chat update
        listenOnChatUpdate();

        // Start timer handler
        timerHandler.postDelayed(timerRunnable, AppConstants.TYPING_SIGNAL_TIMEOUT_MILLIS);
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
        // Back to Recent Chat
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
                    AppLog.d(TAG, ":onChildChanged:");
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    AppLog.d(TAG, ":onChildRemoved:");
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    AppLog.d(TAG, ":onChildMoved:");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    AppLog.e(TAG, ":onCancelled:");
                }
            };
            messageService.getMessageDatabaseRef(chat.getId())
                    .orderByChild("timestamp")
                    .limitToLast(AppConstants.CHAT_HISTORY_PAGINATION_SIZE_DEFAULT)
                    .addChildEventListener(incomingMessageEventListener);
        }
    }

    private void listenOnIncomingTypingSignals() {
        MessageService.getInstance().listenOnTypingSignal(chat.getId(), new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {

                TypingSignal signal = (TypingSignal) result;
                if (signal == null) {
                    return;
                }
                // Should not care about others' timestamp due to we are not replaying on the server timestamp
                // it's actually other devices' timestamp
                signal.setTimestamp(DateTime.now().getMillis());

                if (!signal.equals(myTypingSignal)) {
                    // Others typing (either incoming new or update)
                    if (incomingTypingSignals.contains(signal)) {
                        incomingTypingSignals.set(incomingTypingSignals.indexOf(signal), signal);
                    } else {
                        incomingTypingSignals.add(signal);
                    }

                    // Update UI
                    updateTypingIndicator();
                }
            }
        });
    }

    private void listenOnChatUpdate() {
        if (chatValueEventListener == null) {
            chatValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chat = dataSnapshot.getValue(Chat.class);
                    chat.setId(dataSnapshot.getKey());

                    // Current user has seen the chat, update the user with this
                    UserManager.getInstance().getCurrentUserChatDatabaseRef(chat.getId()).setValue(chat);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            chatService.getChatDatabaseRef(chat.getId()).addValueEventListener(chatValueEventListener);
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

    private void updateTypingIndicator() {

        // See who is typing
        List<TypingSignal> othersTyping = new ArrayList<>();
        for (TypingSignal signal : incomingTypingSignals) {
            if (signal.isTyping()) {
                othersTyping.add(signal);
            }
        }
        AppLog.d("TYPING_SIGNAL", "There are " + othersTyping.size() + " typing ...");

        if (othersTyping.size() == 0) {
            whoTypingTxt.setText("");
            whoTypingTxt.setVisibility(View.INVISIBLE);
        } else {
            String message;
            if (othersTyping.size() == 1) {
                TypingSignal signal = othersTyping.get(0);
                message = MessageFormat.format(getString(R.string.one_is_typing), signal.getUserName());
            } else if (othersTyping.size() == 2) {
                TypingSignal signal1 = othersTyping.get(0);
                TypingSignal signal2 = othersTyping.get(1);
                message = MessageFormat.format(getString(R.string.two_are_typing),
                        signal1.getUserName(), signal2.getUserName());
            } else {
                TypingSignal signal1 = othersTyping.get(0);
                int others = othersTyping.size() - 1;
                message = MessageFormat.format(getString(R.string.one_and_others_are_typing),
                        signal1.getUserName(), others);
            }
            whoTypingTxt.setText(message);
            whoTypingTxt.setVisibility(View.VISIBLE);
        }
    }

    private boolean isTimedOut(long timestamp, long millisToTimedOut) {
        long millisNow = DateTime.now().getMillis();
        return (millisNow - timestamp > millisToTimedOut);
    }

}
