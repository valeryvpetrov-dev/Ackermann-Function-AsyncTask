package ru.geekbrains.android.level2.valeryvpetrov;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, TaskContract.ProducerConsumer<Float, Integer> {

    private ProgressBar progressCalculation;
    private Button buttonCalculate;
    private TextView textResult;
    // Ackermann function arguments
    private EditText inputN;
    private EditText inputM;

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private TaskContract.ConsumerProducer<Integer[], Integer> contractProducerConsumer; // Consumer calls Producer interface

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressCalculation = findViewById(R.id.progress_calculation);
        progressCalculation.setMax(TaskContract.ProducerConsumer.PROGRESS_MAX_VALUE);

        buttonCalculate = findViewById(R.id.button_calculate);
        buttonCalculate.setOnClickListener(this);

        textResult = findViewById(R.id.text_result);

        inputN = findViewById(R.id.argument1);
        inputM = findViewById(R.id.argument2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        contractProducerConsumer = (TaskContract.ConsumerProducer<Integer[], Integer>)
                fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

        // if the fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (contractProducerConsumer == null) {
            contractProducerConsumer = new TaskFragment();
            fragmentManager.beginTransaction()
                    .add((Fragment) contractProducerConsumer, TAG_TASK_FRAGMENT)
                    .commit();
        }

        Integer result = contractProducerConsumer.getResultIfReady();
        if (result != null) {
            postResult(result);
        }
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
            int n = Integer.valueOf(inputN.getText().toString().trim());
            int m = Integer.valueOf(inputM.getText().toString().trim());

            if (!contractProducerConsumer.isCalculating()) {
                contractProducerConsumer.startCalculation(new Integer[]{n, m});
            } else {
                contractProducerConsumer.stopCalculation();
            }
        } catch (NumberFormatException ex) {
            showToast(getResources().getString(R.string.error_number_format));
        }
    }

    private void changeCalculationState() {
        textResult.setText("");
        progressCalculation.setProgress(0); // reset progress
        if (contractProducerConsumer.isCalculating()) {
            buttonCalculate.setText(getResources().getString(R.string.button_calculate_cancel));
        } else {
            buttonCalculate.setText(getResources().getString(R.string.button_calculate_start));
        }
    }

    @Override
    public void startProgress() {
        changeCalculationState();
        showToast(getResources().getString(R.string.info_start_calculation));
    }

    @Override
    public void updateProgress(@NonNull Float progress) {
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
    public int normalizeProgress(@NonNull Float progress) {
        return (int) (progress * PROGRESS_MAX_VALUE);   // convert to progress UI
    }

    @Override
    public void postResult(@NonNull Integer result) {
        changeCalculationState();
        textResult.setText(String.valueOf(result));
        showToast(getResources().getString(R.string.info_calculation_success));
    }

    @Override
    public void postError(@NonNull Throwable e) {
        changeCalculationState();
        if (e instanceof StackOverflowError) {
            showToast(getString(R.string.error_stack_overflow));
        } else if (e instanceof IllegalArgumentException) {
            showToast(getString(R.string.error_wrong_arguments));
        }
    }

    @Override
    public void cancelProgress(@NonNull Integer result) {
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
