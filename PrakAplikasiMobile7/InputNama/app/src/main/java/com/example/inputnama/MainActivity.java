package com.example.inputnama;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonInput, buttonTampilkan;
    TextView textViewResult;
    String namaInput = ""; // Menyimpan nama setelah tombol Input diklik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        buttonInput = findViewById(R.id.buttonInput);
        buttonTampilkan = findViewById(R.id.buttonTampilkan);
        textViewResult = findViewById(R.id.textViewResult);

        buttonInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                namaInput = editTextName.getText().toString();
            }
        });

        buttonTampilkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!namaInput.isEmpty()) {
                    textViewResult.setText("Halo, " + namaInput + "!");
                } else {
                    textViewResult.setText("Nama belum diinput!");
                }
            }
        });
    }
}
