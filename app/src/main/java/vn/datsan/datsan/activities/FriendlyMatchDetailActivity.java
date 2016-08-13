package vn.datsan.datsan.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vn.datsan.datsan.R;

public class FriendlyMatchDetailActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendly_match_detail);
        super.initToolBar();
    }
}
