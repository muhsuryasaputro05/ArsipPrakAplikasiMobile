package com.example.kalkulator;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvResult, tvExpression;
    String currentInput = "";
    String operator = "";
    double firstNumber = 0;
    boolean isNewCalculation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set warna status bar dan poni sesuai background aplikasi
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBackground)); // ganti dengan warna background aplikasi kamu
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBackground));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
            final WindowInsetsController insetsController = window.getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
        }

        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);
    }

    public void onClick(View v) {
        Button btn = (Button) v;
        String text = btn.getText().toString();

        if (isNewCalculation && "0123456789.".contains(text)) {
            currentInput = "";
            tvExpression.setText("");
            isNewCalculation = false;
        }

        switch (text) {
            case "C":
                currentInput = "";
                operator = "";
                firstNumber = 0;
                tvResult.setText("0");
                tvExpression.setText("");
                break;

            case "←":
                if (currentInput.length() > 0) {
                    currentInput = currentInput.substring(0, currentInput.length() - 1);
                    tvResult.setText(currentInput.isEmpty() ? "0" : currentInput);
                }
                break;

            case "+":
            case "-":
            case "×":
            case "÷":
                if (!currentInput.isEmpty()) {
                    firstNumber = Double.parseDouble(currentInput);
                    operator = text;
                    tvExpression.setText(formatNumber(firstNumber) + " " + operator);
                    currentInput = "";
                    tvResult.setText("0");
                }
                break;

            case "=":
                if (!currentInput.isEmpty() && !operator.isEmpty()) {
                    double secondNumber = Double.parseDouble(currentInput);
                    double result = 0;
                    switch (operator) {
                        case "+":
                            result = firstNumber + secondNumber;
                            break;
                        case "-":
                            result = firstNumber - secondNumber;
                            break;
                        case "×":
                            result = firstNumber * secondNumber;
                            break;
                        case "÷":
                            result = (secondNumber != 0) ? firstNumber / secondNumber : 0;
                            break;
                    }
                    tvExpression.setText(formatNumber(firstNumber) + " " + operator + " " + formatNumber(secondNumber) + " =");
                    tvResult.setText(formatNumber(result));
                    currentInput = String.valueOf(result);
                    operator = "";
                    isNewCalculation = true;
                }
                break;

            default:
                if (text.equals(".") && currentInput.contains(".")) {
                    return;
                }
                currentInput += text;
                tvResult.setText(currentInput);
                break;
        }
    }

    private String formatNumber(double num) {
        if (num == (long) num) {
            return String.valueOf((long) num);
        } else {
            return String.valueOf(num);
        }
    }
}
