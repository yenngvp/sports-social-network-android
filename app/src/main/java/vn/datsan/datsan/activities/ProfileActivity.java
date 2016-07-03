package vn.datsan.datsan.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.years)
    TextView years;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.fc)
    TextView fc;
    @BindView(R.id.address)
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        String userId = getIntent().getStringExtra("id");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SimpleProgress.show(ProfileActivity.this);
       // UserManager.getInstance().getUser(userId, profileFetchCallBack);
    }

    CallBack.OnResultReceivedListener profileFetchCallBack = new CallBack.OnResultReceivedListener() {
        @Override
        public void onResultReceived(Object result) {
            SimpleProgress.dismiss();

            // reload view
            if (result != null) {
                reloadView((User) result);
            } else {
                Toast.makeText(ProfileActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void reloadView(User user) {
        name.setText(user.getName());
        email.setText(user.getEmail());
        fc.setText(user.getGroups());
        address.setText(user.getAddress());
    }
}
