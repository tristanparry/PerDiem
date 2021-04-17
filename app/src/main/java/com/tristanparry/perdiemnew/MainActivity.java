package com.tristanparry.perdiemnew;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // VARIABLE INITIALIZATIONS
    private int currentHour;
    TextView day_text, date_text;
    ImageView time_image;
    String dayInformation, dateInformation;
    Date currentTime;
    SimpleDateFormat currentDay;
    FloatingActionButton action_button;
    private AlertDialog.Builder taskEnterwindow;
    private AlertDialog dialog;
    private EditText enter_task_title, enter_task_description;
    private Button cancel_button, save_button;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private ArrayList<TaskItem> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ACCESSING THE ELEMENTS IN THE .XML FILE
        day_text = findViewById(R.id.weekDayText);
        date_text = findViewById(R.id.dateText);
        time_image = findViewById(R.id.timeImage);
        action_button = findViewById(R.id.floatingActionButton);

        // CALCULATING TIME PROPERTIES
        currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        currentDay = new SimpleDateFormat("EEEE");
        currentTime = Calendar.getInstance().getTime();
        // SETTING + DISPLAYING THE DAY/DATE IN THE UPPER MENU
        dayInformation = currentDay.format(currentTime);
        dateInformation = DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime);
        day_text.setText(dayInformation);
        date_text.setText(dateInformation);
        // SETTING THE TIMEIMAGE APPROPRIATE TO THE CURRENT TIME
        if ((currentHour >= 0) && (currentHour <= 11)) {
            time_image.setImageResource(R.drawable.ic_morning);
        } else if ((currentHour >= 12) && (currentHour <= 19)) {
            time_image.setImageResource(R.drawable.ic_afternoon);
        } else if ((currentHour >= 20) && (currentHour <= 23)) {
            time_image.setImageResource(R.drawable.ic_night);
        }

        // PROGRAMMING THE FLOATINGACTIONBUTTON CLICK METHOD
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeTask();
            }
        });

        // RECYCLERVIEW INITIALIZATION
        loadTasks();
        showRecycler();
    }

    public void makeTask() {
        taskEnterwindow = new AlertDialog.Builder(this);
        final View makeTaskView = getLayoutInflater().inflate(R.layout.task_window, null);

        enter_task_title = (EditText) makeTaskView.findViewById(R.id.enterTaskTitle);
        enter_task_description = (EditText) makeTaskView.findViewById(R.id.enterTaskDescription);
        cancel_button = (Button) makeTaskView.findViewById(R.id.cancelButton);
        save_button = (Button) makeTaskView.findViewById(R.id.saveButton);

        taskEnterwindow.setView(makeTaskView);
        dialog = taskEnterwindow.create();
        dialog.show();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskList.add(new TaskItem (enter_task_title.getText().toString(), enter_task_description.getText().toString()));
                dialog.dismiss();
                myAdapter.notifyDataSetChanged();
                saveTasks();
            }
        });
    }

    public void updateTask(final int position) {
        taskEnterwindow = new AlertDialog.Builder(this);
        final View makeTaskView = getLayoutInflater().inflate(R.layout.task_window, null);

        enter_task_title = (EditText) makeTaskView.findViewById(R.id.enterTaskTitle);
        enter_task_description = (EditText) makeTaskView.findViewById(R.id.enterTaskDescription);
        cancel_button = (Button) makeTaskView.findViewById(R.id.cancelButton);
        save_button = (Button) makeTaskView.findViewById(R.id.saveButton);

        taskEnterwindow.setView(makeTaskView);
        dialog = taskEnterwindow.create();
        dialog.show();

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskList.set(position, new TaskItem(enter_task_title.getText().toString(), enter_task_description.getText().toString()));
                dialog.dismiss();
                myAdapter.notifyItemChanged(position);
                saveTasks();
            }
        });
    }

    public void showRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new MyAdapter(taskList);
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnTaskClickInterface(new MyAdapter.OnTaskClickInterface() {
            @Override
            public void onEdit(int position) {
                updateTask(position);
            }

            @Override
            public void onDelete(int position) {
                taskList.remove(position);
                myAdapter.notifyItemRemoved(position);
                saveTasks();
            }
        });
    }

    // SHAREDPREFERENCES SAVE/LOAD METHODS
    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonArrayList = gson.toJson(taskList);
        editor.putString("todo list", jsonArrayList);
        editor.apply();
    }
    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonArrayList = sharedPreferences.getString("todo list",null);
        Type type = new TypeToken<ArrayList<TaskItem>>() {}.getType();
        taskList = gson.fromJson(jsonArrayList, type);

        if (taskList == null) {
            taskList = new ArrayList<>();
        }
    }
}