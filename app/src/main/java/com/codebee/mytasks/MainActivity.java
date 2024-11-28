package com.codebee.mytasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private RecyclerView today_recyclerView, tom_recyclerview, upcoming_recyclerview;
    private TaskAdapter tod_taskAdapter, tom_taskAdapter, up_taskAdapter;
    Button LogoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        today_recyclerView = findViewById(R.id.today_recyclerview);
        tom_recyclerview = findViewById(R.id.tomorrow_recyclerview);
        upcoming_recyclerview = findViewById(R.id.upcoming_recyclerview);

        LogoutBtn = findViewById(R.id.LogoutBtn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogOutFragment.class));
            }
        });


        loadTasks();

        findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        findViewById(R.id.today_text).setVisibility(View.GONE);
        findViewById(R.id.tomorrow_text).setVisibility(View.GONE);
        findViewById(R.id.upcoming_text).setVisibility(View.GONE);
        today_recyclerView.setVisibility(View.GONE);
        tom_recyclerview.setVisibility(View.GONE);
        upcoming_recyclerview.setVisibility(View.GONE);

        loadTasks();
    }

    public void loadTasks() {

        findViewById(R.id.today_text).setVisibility(View.GONE);
        findViewById(R.id.tomorrow_text).setVisibility(View.GONE);
        findViewById(R.id.upcoming_text).setVisibility(View.GONE);
        findViewById(R.id.no_tasks_text).setVisibility(View.GONE);
        today_recyclerView.setVisibility(View.GONE);
        tom_recyclerview.setVisibility(View.GONE);
        upcoming_recyclerview.setVisibility(View.GONE);

        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        ArrayList<MyTask> myTaskArrayList = db.getData();
        ArrayList<MyTask> todArrayList = new ArrayList<>();
        ArrayList<MyTask> tomArrayList = new ArrayList<>();
        ArrayList<MyTask> upArrayList = new ArrayList<>();

        if (!myTaskArrayList.isEmpty()) {

            Collections.sort(myTaskArrayList, new Comparator<MyTask>() {
                @Override
                public int compare(MyTask o1, MyTask o2) {
                    return String.valueOf(o1.getTimestamp()).compareTo(String.valueOf(o2.getTimestamp()));
                }
            });

            for (MyTask task : myTaskArrayList) {
                Calendar calendar = Calendar.getInstance();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(task.getTimestamp());

                if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    todArrayList.add(task);
                } else if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) + 1) {
                    tomArrayList.add(task);
                } else {
                    upArrayList.add(task);
                }
            }

            if (!todArrayList.isEmpty()) {
                findViewById(R.id.today_text).setVisibility(View.VISIBLE);
                today_recyclerView.setVisibility(View.VISIBLE);
                tod_taskAdapter = new TaskAdapter(todArrayList);
                today_recyclerView.setAdapter(tod_taskAdapter);
                today_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            if (!tomArrayList.isEmpty()) {
                findViewById(R.id.tomorrow_text).setVisibility(View.VISIBLE);
                tom_recyclerview.setVisibility(View.VISIBLE);
                tom_taskAdapter = new TaskAdapter(tomArrayList);
                tom_recyclerview.setAdapter(tom_taskAdapter);
                tom_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            if (!upArrayList.isEmpty()) {
                findViewById(R.id.upcoming_text).setVisibility(View.VISIBLE);
                upcoming_recyclerview.setVisibility(View.VISIBLE);
                up_taskAdapter = new TaskAdapter(upArrayList);
                upcoming_recyclerview.setAdapter(up_taskAdapter);
                upcoming_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        } else {
            findViewById(R.id.no_tasks_text).setVisibility(View.VISIBLE);
        }

    }
}
