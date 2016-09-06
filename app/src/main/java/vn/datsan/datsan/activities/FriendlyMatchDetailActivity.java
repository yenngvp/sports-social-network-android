package vn.datsan.datsan.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.joda.time.DateTime;
import butterknife.BindView;
import butterknife.ButterKnife;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.FriendlyMatch;
import vn.datsan.datsan.utils.AppUtils;

public class FriendlyMatchDetailActivity extends SimpleActivity {

    @BindView(R.id.fc_avatar)
    ImageView fcAvatar;
    @BindView(R.id.fc_name)
    TextView fcName;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.time_range)
    TextView timeTv;
    @BindView(R.id.field_name)
    TextView fieldName;
    @BindView(R.id.call_btn)
    Button callBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendly_match_detail);
        super.initToolBar();

        ButterKnife.bind(this);

        FriendlyMatch data = getIntent().getParcelableExtra("data");
        populateData(data);
    }

    private void populateData(FriendlyMatch data) {
        DateTime startTime = new DateTime(data.getStartTime());
        DateTime endTime = new DateTime(data.getEndTime());
        String dayWeek = AppUtils.getWeekDayAsText(startTime);
        String dayMonth = AppUtils.getMonthDayAsText(startTime);
        // TODO: Shouldn't do it manually. Use Joda Duration/Period to calculate the time period
        String timeRange = "Thời gian " + startTime.getHourOfDay() + "h:" + startTime.getMinuteOfHour() + " - " +
                endTime.getHourOfDay() + "h:" + endTime.getMinuteOfHour();
        String timeString = dayWeek + "," + dayMonth + "\n" + timeRange;
        titleTv.setText(data.getTitle());
        fcName.setText(data.getHomeGroupName() + " ");
        fieldName.setText(data.getFields());
        timeTv.setText(timeString);
    }
}
