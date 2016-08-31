package vn.datsan.datsan.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.List;
import java.util.Random;

import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.ui.appviews.ContactsCompletionView;

public class CreateChatActivity extends SimpleActivity implements TokenCompleteTextView.TokenListener<User> {
    ContactsCompletionView completionView;
    User[] people;
    ArrayAdapter<User> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        this.initToolBar();

        people = new User[]{
                new User("Marshall Weir", "marshall@example.com"),
                new User("Margaret Smith", "margaret@example.com"),
                new User("Max Jordan", "max@example.com"),
                new User("Meg Peterson", "meg@example.com"),
                new User("Amanda Johnson", "amanda@example.com"),
                new User("Terry Anderson", "terry@example.com"),
                new User("Siniša Damianos Pilirani Karoline Slootmaekers",
                        "siniša_damianos_pilirani_karoline_slootmaekers@example.com")
        };

        adapter = new FilteredArrayAdapter<User>(this, R.layout.user_layout, people) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.user_layout, parent, false);
                }

                User p = getItem(position);
                ((TextView)convertView.findViewById(R.id.name)).setText(p.getName());
                ((TextView)convertView.findViewById(R.id.email)).setText(p.getEmail());

                return convertView;
            }

            @Override
            protected boolean keepObject(User User, String mask) {
                mask = mask.toLowerCase();
                return User.getName().toLowerCase().startsWith(mask) || User.getEmail().toLowerCase().startsWith(mask);
            }
        };

        completionView = (ContactsCompletionView)findViewById(R.id.searchView);
        completionView.setAdapter(adapter);
        completionView.setTokenListener(this);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);


        if (savedInstanceState == null) {
            completionView.setPrefix("To: ");
            completionView.addObject(people[0]);
            completionView.addObject(people[1]);
        }

        Button removeButton = (Button)findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> people = completionView.getObjects();
                if (people.size() > 0) {
                    completionView.removeObject(people.get(people.size() - 1));
                }
            }
        });

        Button addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                completionView.addObject(people[rand.nextInt(people.length)]);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void updateTokenConfirmation() {
        StringBuilder sb = new StringBuilder("Current tokens:\n");
        for (Object token: completionView.getObjects()) {
            sb.append(token.toString());
            sb.append("\n");
        }

        ((TextView)findViewById(R.id.tokens)).setText(sb);
    }


    @Override
    public void onTokenAdded(User token) {
        ((TextView)findViewById(R.id.lastEvent)).setText("Added: " + token);
        updateTokenConfirmation();
    }

    @Override
    public void onTokenRemoved(User token) {
        ((TextView)findViewById(R.id.lastEvent)).setText("Removed: " + token);
        updateTokenConfirmation();
    }
}

