package vn.datsan.datsan.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.TimeZone;

import vn.datsan.datsan.R;

public class NewFriendlyMatchActivity extends SimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friendly_match);
        super.initToolBar();

        final ImageButton setStartTimeBtn = (ImageButton) findViewById(R.id.change_start_time_btn);
        ImageButton setEndTimeBtn = (ImageButton) findViewById(R.id.change_end_time_btn);
        ImageButton setDateBtn = (ImageButton) findViewById(R.id.change_date_btn);

        setStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalTime localTime = new LocalTime();
                TimePickerDialog dialog = new TimePickerDialog(NewFriendlyMatchActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                            }
                        },localTime.getHourOfDay(),localTime.getMinuteOfHour(), true);
                dialog.setTitle("Chọn thời gian bắt đầu");
                dialog.show();
            }
        });

        setEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTime dateTime = DateTime.now(DateTimeZone.getDefault());

                LocalTime localTime = new LocalTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("7")));
                TimePickerDialog dialog = new TimePickerDialog(NewFriendlyMatchActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                            }
                        },dateTime.getHourOfDay(),localTime.getMinuteOfHour(), true);
                dialog.setTitle("Chọn thời gian kết thúc");
                dialog.show();
        }
        });

        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate localDate = new LocalDate();
                DatePickerDialog dialog = new DatePickerDialog(NewFriendlyMatchActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    }
                }, localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());

                dialog.setTitle("Chọn ngày thi đấu");
                dialog.show();
            }
        });
    }
}
