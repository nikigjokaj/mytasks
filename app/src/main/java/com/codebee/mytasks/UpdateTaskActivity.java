package com.codebee.mytasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateTaskActivity extends AppCompatActivity {

    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String time = "", date = "";
    private int hour, min, y, m, d;
    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        myTask = (MyTask) getIntent().getSerializableExtra("task");
        ((EditText)findViewById(R.id.update_task_label_text)).setText(myTask.getLabel());
        ((EditText)findViewById(R.id.update_task_desc_text)).setText(myTask.getDescription());
        ((TextView)findViewById(R.id.update_task_date_text)).setText(myTask.getDate());
        ((TextView)findViewById(R.id.update_task_time_text)).setText(myTask.getTime());
        time = myTask.getTime();
        date = myTask.getDate();

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;

                Calendar calendar = Calendar.getInstance();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(y, m, d);
                if (calendar1.getTime().compareTo(calendar.getTime()) > 0) {
                    time = hour + ":" + min;
                    ((TextView) findViewById(R.id.update_task_time_text)).setText(time);
                } else {
                    if (isAfterDelay(hour, min)) {
                        time = hour + ":" + min;
                        ((TextView) findViewById(R.id.update_task_time_text)).setText(time);
                    } else {
                        Toast.makeText(getApplicationContext(), "You can only create tasks scheduled after 30 minutes from now.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month;
                d = dayOfMonth;
                if (isAfterToday(d, m, y)) {
                    date = d + "/" + (m + 1) + "/" + y;
                    ((TextView) findViewById(R.id.update_task_date_text)).setText(date);
                    time = "";
                    ((TextView) findViewById(R.id.update_task_time_text)).setText("Choose a time");
                } else {
                    Toast.makeText(getApplicationContext(), "Seems like the date you have selected is already gone.", Toast.LENGTH_LONG).show();
                }
            }
        };

        findViewById(R.id.update_task_back_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.update_task_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((EditText) findViewById(R.id.update_task_label_text)).getText().toString().trim().isEmpty() &&
                        !((EditText) findViewById(R.id.update_task_desc_text)).getText().toString().trim().isEmpty() &&
                        !time.isEmpty() &&
                        !date.isEmpty()) {
                    updateTask();
                }else{
                    if(((EditText) findViewById(R.id.update_task_label_text)).getText().toString().trim().isEmpty()){
                        ((EditText) findViewById(R.id.update_task_label_text)).setError("This field cannot be left empty!");
                    }
                    if(((EditText) findViewById(R.id.update_task_desc_text)).getText().toString().trim().isEmpty()){
                        ((EditText) findViewById(R.id.update_task_desc_text)).setError("This field cannot be left empty!");
                    }
                    if(date.isEmpty()){
                        ((TextView)findViewById(R.id.update_task_date_text)).setError("Please choose a date first.");
                    }
                    if(time.isEmpty()){
                        ((TextView)findViewById(R.id.update_task_time_text)).setError("Please choose a time first.");
                    }
                }
            }
        });

        findViewById(R.id.update_task_date_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTaskActivity.this,
                        onDateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        findViewById(R.id.update_task_time_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select a date first.", Toast.LENGTH_LONG).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateTaskActivity.this,
                            onTimeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }
            }
        });
    }

    private boolean isAfterToday(int D, int M, int Y) {

        Calendar calendar = Calendar.getInstance();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Y, M, D);

        if (calendar1.getTime().compareTo(calendar.getTime()) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isAfterDelay(int H, int M) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(y, m, d, H, M);
        if (calendar1.getTimeInMillis() >= calendar.getTimeInMillis() + 1000 * 60 * 30) {
            return true;
        } else {
            return false;
        }
    }

    private void updateTask() {

        myTask.setLabel(((EditText) findViewById(R.id.update_task_label_text)).getText().toString().trim());
        myTask.setDescription(((EditText) findViewById(R.id.update_task_desc_text)).getText().toString().trim());
        myTask.setDate(date);
        myTask.setTime(time);

        Calendar calendar = Calendar.getInstance();
        calendar.set(y,m,d,hour,min);
        long timestamp = calendar.getTimeInMillis();

        myTask.setTimestamp(timestamp);

        DatabaseHelper db = new DatabaseHelper(UpdateTaskActivity.this);
        if(db.updateData(myTask)){
            Toast.makeText(getApplicationContext(),"Task updated successfully!",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Unable to update task!",Toast.LENGTH_SHORT).show();
        }
    }
}
