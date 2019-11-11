package ru.geekbrains.android.level2.valeryvpetrov;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaskFragment extends Fragment
        implements TaskContract.ConsumerProducer<Integer[], Integer>, TaskContract.TaskProducer<Float, Integer> {

    @Nullable
    private TaskContract.ProducerConsumer<Float, Integer> contractProducerConsumer; // Producer calls Consumer interface
    @Nullable
    private AckermannFunctionAsyncTask ackermannFunctionAsyncTask;

    private boolean isCalculating;  // value is gotten from task state to be used by consumer
    @Nullable
    private Integer result; // value is gotten from task to be used by consumer

    // Begin TaskContract.ConsumerProducer implementation
    @Override
    public boolean isCalculating() {
        return isCalculating;
    }

    @Override
    @Nullable
    public Integer getResultIfReady() {
        if (result != null) return result;
        else return null;
    }

    @Override
    public void startCalculation(@NonNull Integer[] args) {
        // Create and execute the background task.
        ackermannFunctionAsyncTask = new AckermannFunctionAsyncTask(this);
        ackermannFunctionAsyncTask.execute(args);
    }

    @Override
    public void stopCalculation() {
        if (ackermannFunctionAsyncTask != null) {
            ackermannFunctionAsyncTask.cancel(true);
        }
    }
    // End TaskContract.ConsumerProducer implementation

    // Begin TaskContract.TaskProducer implementation
    @Override
    public void startProgress() {
        isCalculating = true;
        if (contractProducerConsumer != null) {
            contractProducerConsumer.startProgress();
        }
    }

    @Override
    public void updateProgress(@NonNull Float progress) {
        if (contractProducerConsumer != null) {
            contractProducerConsumer.updateProgress(progress);
        }
    }

    @Override
    public void postResult(@NonNull Integer result) {
        this.result = result;
        isCalculating = false;
        if (contractProducerConsumer != null) {
            contractProducerConsumer.postResult(result);
        }
    }

    @Override
    public void cancelProgress(@NonNull Integer result) {
        this.result = result;
        isCalculating = false;
        if (contractProducerConsumer != null) {
            contractProducerConsumer.cancelProgress(result);
        }
    }
    // End TaskContract.TaskProducer implementation

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contractProducerConsumer = ((TaskContract.ProducerConsumer<Float, Integer>) context);
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        isCalculating = false;
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        contractProducerConsumer = null;
    }
}
