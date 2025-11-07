package com.informatika.indradwiprabowo.todolist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.informatika.indradwiprabowo.todolist.model.DataManager;
import com.informatika.indradwiprabowo.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private TextInputEditText etTitle;
    private TextView tvDeadline;
    private Button btnSave;

    private int editIndex = -1; // -1 = mode tambah, >=0 = mode edit
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.et_title);
        tvDeadline = findViewById(R.id.tv_deadline);
        btnSave = findViewById(R.id.btn_save);

        tvDeadline.setOnClickListener(v -> showDatePickerDialog());

        // Cek apakah ini mode edit
        if (getIntent().hasExtra("edit_index")) {
            editIndex = getIntent().getIntExtra("edit_index", -1);
            Task task = DataManager.taskList.get(editIndex);
            etTitle.setText(task.title);
            tvDeadline.setText(task.deadline);
            btnSave.setText("Simpan Perubahan");
        }

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        new DatePickerDialog(AddTaskActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDeadline.setText(sdf.format(calendar.getTime()));
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String deadline = tvDeadline.getText().toString().trim();

        if (title.isEmpty() || deadline.isEmpty() || deadline.equals("Pilih tanggal...")) {
            Toast.makeText(this, "Judul dan deadline harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editIndex == -1) {
            // Mode tambah
            DataManager.taskList.add(new Task(title, deadline));
            Toast.makeText(this, "Tugas ditambahkan!", Toast.LENGTH_SHORT).show();
        } else {
            // Mode edit
            Task updated = new Task(title, deadline);
            DataManager.taskList.set(editIndex, updated);
            Toast.makeText(this, "Tugas diperbarui!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}