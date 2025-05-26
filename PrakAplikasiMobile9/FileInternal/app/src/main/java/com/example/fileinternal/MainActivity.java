package com.example.fileinternal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextFileName, editTextContent;
    ListView listViewFiles;
    ArrayAdapter<String> fileAdapter;
    Button buttonToggleFiles;
    boolean isListVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextContent = findViewById(R.id.editTextContent);
        listViewFiles = findViewById(R.id.listViewFiles);
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonToggleFiles = findViewById(R.id.buttonToggleFiles);

        // Set text tombol hanya "Simpan File"
        buttonSave.setText("Simpan File");

        // Simpan file
        buttonSave.setOnClickListener(v -> {
            String fileName = editTextFileName.getText().toString().trim();
            String content = editTextContent.getText().toString();

            if (fileName.isEmpty()) {
                Toast.makeText(this, "Nama file tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                FileOutputStream fos = openFileOutput(fileName + ".txt", MODE_PRIVATE);
                fos.write(content.getBytes());
                fos.close();
                Toast.makeText(this, "File disimpan: " + fileName, Toast.LENGTH_SHORT).show();
                if (isListVisible) {
                    loadFileList(); // refresh daftar file jika sedang tampil
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan file", Toast.LENGTH_SHORT).show();
            }
        });

        // Toggle daftar file
        buttonToggleFiles.setOnClickListener(v -> {
            if (isListVisible) {
                listViewFiles.setVisibility(View.GONE);
                buttonToggleFiles.setText("Lihat Semua File");
                isListVisible = false;
            } else {
                loadFileList();
                listViewFiles.setVisibility(View.VISIBLE);
                buttonToggleFiles.setText("Sembunyikan File");
                isListVisible = true;
            }
        });

        // Klik file di listView, munculkan popup Ubah atau Hapus
        listViewFiles.setOnItemClickListener((parent, view, position, id) -> {
            String selectedFile = (String) parent.getItemAtPosition(position);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Pilih aksi untuk file")
                    .setMessage(selectedFile)
                    .setPositiveButton("Ubah", (dialog, which) -> {
                        // Muat isi file ke EditText dan set nama file tanpa ekstensi
                        String fileNameWithoutExt = selectedFile.replace(".txt", "");
                        editTextFileName.setText(fileNameWithoutExt);
                        loadFileContent(selectedFile);
                    })
                    .setNegativeButton("Hapus", (dialog, which) -> {
                        // Hapus file
                        boolean deleted = deleteFile(selectedFile);
                        if (deleted) {
                            Toast.makeText(MainActivity.this, "File dihapus: " + selectedFile, Toast.LENGTH_SHORT).show();
                            loadFileList(); // refresh list
                        } else {
                            Toast.makeText(MainActivity.this, "Gagal menghapus file", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("Batal", null)
                    .show();
        });
    }

    // Tampilkan daftar file .txt
    private void loadFileList() {
        String[] files = fileList();
        ArrayList<String> txtFiles = new ArrayList<>();

        for (String file : files) {
            if (file.endsWith(".txt")) {
                txtFiles.add(file);
            }
        }

        fileAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, txtFiles);
        listViewFiles.setAdapter(fileAdapter);
    }

    // Baca isi file
    private void loadFileContent(String fileName) {
        try {
            FileInputStream fis = openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            br.close();
            editTextContent.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            editTextContent.setText("Gagal membaca file.");
        }
    }
}
