package com.example.basiccalculator;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;
    MaterialButton buttonXY, buttonExclamation, buttonRoot;
    WebView webView;

    double xValue = 0;
    boolean isXY = false;
    double yValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize WebView
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        resultTv = findViewById(R.id.result_tv);

        // Assigning IDs to buttons
        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_close_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equals);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);
        assignId(buttonXY, R.id.button_xy);
        assignId(buttonExclamation, R.id.button_exclamation);
        assignId(buttonRoot, R.id.button_root);
    }

    // Helper method to assign button IDs and set onClick listeners
    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    // Handle button click events
    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = resultTv.getText().toString();

        // Handle special button clicks
        if (buttonText.equals("AC")) {
            resultTv.setText("");
            xValue = 0;
            yValue = 0;
            isXY = false;
            return;
        }

        if (buttonText.equals("=")) {
            // Call getResult to calculate the result and update resultTv
            String result = getResult(dataToCalculate);
            resultTv.setText(result);
            return;
        }

        if (buttonText.equals("C")) {
            dataToCalculate = dataToCalculate.length() > 1 ? dataToCalculate.substring(0, dataToCalculate.length() - 1) : "";
        } else {
            dataToCalculate += buttonText;
        }

        resultTv.setText(dataToCalculate);

        // Handle XY button (X^Y)
        if (buttonText.equals("XY")) {
            xValue = Double.parseDouble(dataToCalculate);
            isXY = true;
            resultTv.setText("");
            return;
        }

        // Handle Exclamation button (Factorial)
        if (buttonText.equals("!")) {
            int number = Integer.parseInt(dataToCalculate);
            resultTv.setText(String.valueOf(factorial(number)));
            return;
        }

        // Handle Root button (Square Root)
        if (buttonText.equals("√")) {
            double number = Double.parseDouble(dataToCalculate);
            resultTv.setText(String.valueOf(Math.sqrt(number)));
            return;
        }

        // Handle XY operation
        if (isXY) {
            yValue = Double.parseDouble(dataToCalculate);
            resultTv.setText(String.valueOf(Math.pow(xValue, yValue)));
            isXY = false;
            return;
        }
    }

    // Method to calculate factorial
    private long factorial(int number) {
        if (number == 0 || number == 1) {
            return 1;
        }
        long result = 1;
        for (int i = 1; i <= number; i++) {
            result *= i;
        }
        return result;
    }

    // Method to evaluate the result using WebView and JavaScript
    public String getResult(String data) {
        final String[] result = new String[1];

        // Use WebView to evaluate the JavaScript expression
        webView.evaluateJavascript("javascript:(" + data + ")", value -> {
            result[0] = value;

            // Handle the result and remove trailing .0 if present
            if (result[0].endsWith(".0")) {
                result[0] = result[0].replace(".0", "");
            }
            // Update the TextView with the calculated result
            runOnUiThread(() -> resultTv.setText(result[0] != null ? result[0] : "Err"));
        });

        // Return a placeholder if the result is not available immediately
        return "Processing...";
    }
}
