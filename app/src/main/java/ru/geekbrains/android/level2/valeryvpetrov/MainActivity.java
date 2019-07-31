package ru.geekbrains.android.level2.valeryvpetrov;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, IProgressObserverView {

    private ProgressBar progressCalculation;
    private Button buttonCalculate;
    private TextView textResult;

    private AsyncTask ackermannFunctionAsyncTask;
    private boolean isCalculating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isCalculating = false;

        progressCalculation = findViewById(R.id.progress_calculation);
        progressCalculation.setMax(PROGRESS_MAX_VALUE);

        buttonCalculate = findViewById(R.id.button_calculate);
        buttonCalculate.setOnClickListener(this);

        textResult = findViewById(R.id.text_result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_calculate:
                calculate();
                break;
        }
    }

    private void calculate() {
        try {
            int n = Integer.valueOf(((EditText) findViewById(R.id.argument1)).getText().toString().trim());
            int m = Integer.valueOf(((EditText) findViewById(R.id.argument2)).getText().toString().trim());

            if (!isCalculating) {
                ackermannFunctionAsyncTask = new AckermannFunctionAsyncTask(this);  // cannot be reused, create another instance
                ackermannFunctionAsyncTask.execute(new Integer[]{n, m});
            } else {
                if (ackermannFunctionAsyncTask != null) {
                    ackermannFunctionAsyncTask.cancel(true);
                }
            }
        } catch (NumberFormatException ex) {
            showToast(getResources().getString(R.string.error_number_format));
        }
    }

    private void changeCalculationState() {
        isCalculating = !isCalculating; // switch state
        textResult.setText("");
        if (isCalculating) {
            progressCalculation.setProgress(0); // reset progress for new calculation
            buttonCalculate.setText(getResources().getString(R.string.button_calculate_cancel));
        } else {
            // remain last progress for user
            buttonCalculate.setText(getResources().getString(R.string.button_calculate_start));
        }
    }

    @Override
    public void startProgress() {
        changeCalculationState();
        showToast(getResources().getString(R.string.info_start_calculation));
    }

    @Override
    public void updateProgress(float progress) {
        // progress is passed as float between 0 and 1. 0 relates to no work done, 1 - all done
        if (progress == -1) {   // hard to estimate progress
            if (progressCalculation.getMax() < progressCalculation.getMax() + PROGRESS_DEFAULT_INCREMENT_VALUE)   // has reached the end
                progressCalculation.setProgress(0); // go to next lap
            else
                progressCalculation.incrementProgressBy(PROGRESS_DEFAULT_INCREMENT_VALUE);  // make a step
        } else {
            progressCalculation.setProgress(normalizeProgress(progress));
        }
    }

    @Override
    public int normalizeProgress(float progress) {
        return (int) (progress * PROGRESS_MAX_VALUE);   // convert to progress UI
    }

    @Override
    public void postResult(int result) {
        changeCalculationState();
        if (result == -1) { // wrong arguments
            showToast(getResources().getString(R.string.error_wrong_arguments));
        } else {
            textResult.setText(String.valueOf(result));
            showToast(getResources().getString(R.string.info_calculation_success));
        }
    }

    @Override
    public void cancelProgress(int result) {
        changeCalculationState();
        showToast(getResources().getString(R.string.info_calculation_cancel));
    }

    private void showToast(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}
