package com.example.listview;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText inputTask;
    Button btnAdd, btnShow;
    ListView listViewTasks;
    ArrayList<String> taskList;
    ArrayAdapter<String> adapter;
    boolean isListVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Inisialisasi komponen
        inputTask = findViewById(R.id.inputTask);
        btnAdd = findViewById(R.id.btnAdd);
        btnShow = findViewById(R.id.btnShow);
        listViewTasks = findViewById(R.id.listViewTasks);

        taskList = new ArrayList<>();

        // Buat adapter dengan override getView untuk set warna teks hitam
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK); // warna font hitam
                return tv;
            }
        };

        listViewTasks.setAdapter(adapter);
        listViewTasks.setVisibility(View.GONE); // ListView awalnya tersembunyi

        // Tambah tugas
        btnAdd.setOnClickListener(v -> {
            String task = inputTask.getText().toString().trim();
            if (!task.isEmpty()) {
                taskList.add(task);
                adapter.notifyDataSetChanged();
                inputTask.setText("");
                Toast.makeText(this, "Tugas ditambahkan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Isi tugas dulu", Toast.LENGTH_SHORT).show();
            }
        });

        // Tampilkan/sembunyikan daftar tugas
        btnShow.setOnClickListener(v -> {
            isListVisible = !isListVisible;
            listViewTasks.setVisibility(isListVisible ? View.VISIBLE : View.GONE);
            btnShow.setText(isListVisible ? "Sembunyikan Tugas" : "Tampilkan Tugas");
        });

        // Tampilkan dialog saat item diklik
        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTask = adapter.getItem(position);
            new AlertDialog.Builder(this)
                    .setTitle("Detail Tugas")
                    .setMessage(selectedTask)
                    .setPositiveButton("Tutup", null)
                    .show();
        });
    }
}
