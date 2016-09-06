package vn.datsan.datsan.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getName();

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.years)
    TextView years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (UserManager.getInstance().getCurrentUser() == null) {
            final SimpleProgress progress = new SimpleProgress(ProfileActivity.this, null);
            progress.show();

            UserManager.getInstance().getCurrentUserInfo(new CallBack.OnResultReceivedListener() {
                @Override
                public void onResultReceived(Object result) {
                    progress.dismiss();

                    // reload view
                    if (result != null) {
                        reloadView((User) result);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void reloadView(User user) {
        name.setText(user.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
