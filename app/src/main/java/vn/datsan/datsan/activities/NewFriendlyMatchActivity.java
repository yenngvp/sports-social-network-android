package vn.datsan.datsan.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.FriendlyMatch;
import vn.datsan.datsan.serverdata.MatchService;
import vn.datsan.datsan.serverdata.UserService;
import vn.datsan.datsan.ui.customwidgets.Alert.SimpleAlert;
import vn.datsan.datsan.utils.AppLog;

public class NewFriendlyMatchActivity extends SimpleActivity {

    @BindView(R.id.start_time_tv)
    TextView startTimeTv;
    @BindView(R.id.endtime_tv)
    TextView endTimeTv;
    @BindView(R.id.date_tv)
    TextView date;
    @BindView(R.id.topic_name_edt)
    TextView topicName;
    @BindView(R.id.field_name_edt)
    TextView fieldName;

    private DateTime startDate;// = DateTime.now(DateTimeZone.forOffsetHours(7));
    private DateTime endDate;// = DateTime.now(DateTimeZone.forOffsetHours(7));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friendly_match);
        ButterKnife.bind(this);
        super.initToolBar();
        DateTimeZone.setDefault(DateTimeZone.forOffsetHours(7));
        startDate = DateTime.now().plusHours(1);
        endDate = DateTime.now().plusHours(2);

        final ImageButton setStartTimeBtn = (ImageButton) findViewById(R.id.change_start_time_btn);
        ImageButton setEndTimeBtn = (ImageButton) findViewById(R.id.change_end_time_btn);
        ImageButton setDateBtn = (ImageButton) findViewById(R.id.change_date_btn);
        startTimeTv.setText(startDate.getHourOfDay() + ":" + String.format("%02d", startDate.getMinuteOfHour()));
        endTimeTv.setText(endDate.getHourOfDay() + ":" + String.format("%02d",endDate.getMinuteOfHour()));

        setStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppLog.log(AppLog.LogType.LOG_ERROR, "tag", startDate.toString() + "\n " + startDate.getHourOfDay());
                Date date = new Date();
                TimePickerDialog dialog = new TimePickerDialog(NewFriendlyMatchActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                                startTimeTv.setText(hours + ":" + minutes);
                                startDate.withHourOfDay(hours);
                                startDate.withMinuteOfHour(minutes);

                                AppLog.log(AppLog.LogType.LOG_ERROR, "tag", startDate.toString());
                            }
                        }, startDate.getHourOfDay(), startDate.getMinuteOfHour(), true);
                dialog.setTitle("Chọn thời gian bắt đầu");
                dialog.show();
            }
        });

        setEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                TimePickerDialog dialog = new TimePickerDialog(NewFriendlyMatchActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                                endTimeTv.setText(hours + ":" + minutes);

                                endDate.withHourOfDay(hours);
                                endDate.withMinuteOfHour(minutes);

                                AppLog.log(AppLog.LogType.LOG_ERROR, "tag", endDate.toString());
                            }
                        }, endDate.getHourOfDay(), endDate.getMinuteOfHour(), true);
                dialog.setTitle("Chọn thời gian kết thúc");
                dialog.show();

        }
        });

        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(NewFriendlyMatchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        if (month < startDate.getMonthOfYear()
                                || (month == startDate.getMonthOfYear()) && day < startDate.getDayOfMonth()) {
                            SimpleAlert.showAlert(NewFriendlyMatchActivity.this, "Dữ liệu nhập không hợp lệ",
                                    "Không thể chọn ngày trong quá khứ !", "Đóng");
                        } else {
                            startDate.withMonthOfYear(month);
                            endDate.withMonthOfYear(month);
                            date.setText(day + "/" + month);
                        }
                    }
                }, startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth());

                dialog.setTitle("Chọn ngày thi đấu");
                dialog.show();
            }
        });
    }

    @OnClick(R.id.register)
    public void onCreateMatchButtonClicked() {
        if(!checkValidation())
            return;
        FriendlyMatch friendlyMatch = new FriendlyMatch();
        friendlyMatch.setTitle(topicName.getText().toString());
        friendlyMatch.setCreatorId(UserService.getInstance().getUserInfo().getId());
        friendlyMatch.setCreatorName(UserService.getInstance().getUserInfo().getName());
        friendlyMatch.setStartTime(startDate.getMillis());
        friendlyMatch.setEndTime(endDate.getMillis());
        friendlyMatch.setFields(fieldName.getText().toString());
        MatchService.getInstance().addMatch(friendlyMatch);
    }

    private boolean checkValidation() {
        if (startDate.isBeforeNow())
            AppLog.log(AppLog.LogType.LOG_ERROR, "Tag", "in past");
        if (endDate.getMinuteOfDay() - startDate.getMinuteOfDay() < 10 || startDate.isBeforeNow()) {
            SimpleAlert.showAlert(NewFriendlyMatchActivity.this, "Dữ liệu nhập không hợp lệ",
                    "Vui lòng kiểm tra lại thời gian nhập !", "Đóng");
            return false;
        }
        if (topicName.getText().length() < 10) {
            SimpleAlert.showAlert(NewFriendlyMatchActivity.this, "Dữ liệu nhập không hợp lệ",
                    "Tiêu đề quá ngắn !", "Đóng");
            return false;
        }
        return true;
    }
}
