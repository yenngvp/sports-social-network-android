package vn.datsan.datsan.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.models.chat.Chat;
import vn.datsan.datsan.search.AppSearch;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.serverdata.chat.ChatService;
import vn.datsan.datsan.ui.appviews.ContactsCompletionView;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;

public class CreateChatActivity extends SimpleActivity implements TokenCompleteTextView.TokenListener<User>, TextWatcher {

    private static final String TAG = CreateChatActivity.class.getSimpleName();

    ContactsCompletionView completionView;
    List<User> users;
    ArrayAdapter<User> adapter;

    @BindView(R.id.txt_max_users_exceed) TextView tvMaxUserExceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        ButterKnife.bind(this);

        this.initToolBar();
        getSupportActionBar().setTitle(getString(R.string.new_chat));

        users = new ArrayList<>();

        adapter = new FilteredArrayAdapter<User>(this, R.layout.user_layout, users) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.user_layout, parent, false);
                }

                User p = getItem(position);
                ((TextView)convertView.findViewById(R.id.user_name)).setText(p.getName());
//                ((ImageView)convertView.findViewById(R.id.user_avatar)).;

                return convertView;
            }

            @Override
            protected boolean keepObject(User user, String mask) {
                mask = mask.toLowerCase();
                return user.getId().toLowerCase().startsWith(mask);
            }
        };

        completionView = (ContactsCompletionView)findViewById(R.id.searchView);
        completionView.setAdapter(adapter);
        completionView.setTokenListener(this);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        completionView.addTextChangedListener(this);
        completionView.allowDuplicates(false);
        completionView.setKeepScreenOn(true);
        // Enter or Done keyboard button click listener, start new chat
        completionView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (completionView.getObjects() == null || completionView.getObjects().size() == 0) {
                        return false;
                    }

                    // Get selected users list
                    final List<User> usersInChat = new ArrayList<>();
                    User me = UserManager.getInstance().getCurrentUser();
                    if (!usersInChat.contains(me)) {
                        usersInChat.add(me);
                    }

                    for (User user : completionView.getObjects()) {
                        usersInChat.add(user);
                    }

                    final Context context = v.getContext();
                    SimpleProgress.show(context, null);
                    // Search existed chat group from the users list
                    ChatService.getInstance().searchChatFromUsers(usersInChat, new CallBack.OnResultReceivedListener() {
                        @Override
                        public void onResultReceived(Object result) {
                            Chat chat = (Chat) result;
                            if (chat == null) {
                                // Create chat
                                chat = ChatService.getInstance().createChatGroup(usersInChat);
                            }

                            // Enter chat
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("chat", chat);
                            startActivity(intent);

                            SimpleProgress.dismiss();
                        }
                    });
                }
                return false;
            }
        });

        completionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.showSoftInput(arg1, InputMethodManager.SHOW_FORCED);
            }

        });

        if (savedInstanceState == null) {
            completionView.setPrefix("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onTokenAdded(User token) {
        if (completionView.getObjects().size() > Constants.MAX_MEMBERS_IN_A_CHAT) {
            completionView.removeObject(token);
            tvMaxUserExceed.setText(String.format(getString(R.string.max_user_exceed), Constants.MAX_MEMBERS_IN_A_CHAT));
        }
    }

    @Override
    public void onTokenRemoved(User token) {
        if (completionView.getObjects().size() < Constants.MAX_MEMBERS_IN_A_CHAT) {
            tvMaxUserExceed.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        String keyword = getKeywordForSearch(charSequence, i, i1, i2);
        if (TextUtils.isEmpty(keyword)) {
            return;
        }

        AppSearch.searchUser(keyword, null, null, null, new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {

                SearchResult searchResult = (SearchResult) result;
                if (searchResult == null || !searchResult.isSucceeded()) {
                    AppLog.d(TAG, "Not found any user");
                    return;
                }

                // Get search result type fields
                AppLog.d(TAG, "Search found TOTAL: " + searchResult.getTotal());
                User me = UserManager.getInstance().getCurrentUser();
                List<SearchResult.Hit<Map, Void>> hits = searchResult.getHits(Map.class);
                List<User> newUsers = new ArrayList<>();
                for (SearchResult.Hit<Map, Void> hit : hits) {
                    Map source = (Map) hit.source;
                    String id = (String)source.get(JestResult.ES_METADATA_ID);
                    if (!me.getId().equals(id)) {
                        // Not adding myself to the suggestion list
                        String name = (String) source.get("name");
                        String email = (String) source.get("email");

                        User user = new User(id, name, email);
                        newUsers.add(user);
                    }
                }

                users.removeAll(users);
                users.addAll(newUsers);
                adapter.addAll(users);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private String getKeywordForSearch(CharSequence charSequence, int i, int i1, int i2) {
        String keyword = charSequence.toString();
        AppLog.d(TAG, "ORIGIN: " +  keyword);

        if (TextUtils.isEmpty(keyword)) {
            return keyword;
        }

        keyword = keyword.replaceAll("[,,]", "");
        keyword = keyword.trim();

        AppLog.d(TAG, "TRIMMED: " + keyword);

        return keyword;
    }

}

