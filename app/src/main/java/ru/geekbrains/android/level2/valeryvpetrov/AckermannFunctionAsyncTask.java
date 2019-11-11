package ru.geekbrains.android.level2.valeryvpetrov;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class AckermannFunctionAsyncTask extends AsyncTask<Integer, Float, Integer> {

    private static final String LOG_TAG = AckermannFunctionAsyncTask.class.getSimpleName();

    @Nullable
    private TaskContract.TaskProducer contractTaskProducer; // Task calls Producer interface

    private int maxCalculatedResult;
    private int estimatedResult;

    public AckermannFunctionAsyncTask(@NonNull TaskContract.TaskProducer progressObserverView) {
        this.contractTaskProducer = progressObserverView;
    }

    @Override
    protected void onPreExecute() {
        Log.d(LOG_TAG, "onPreExecute()");
        if (contractTaskProducer != null) {
            contractTaskProducer.startProgress();
        }
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        Log.d(LOG_TAG, String.format("doInBackground(...[%d])", integers.length));
        if (integers.length == 2) { // determined only for 2 arguments
            int n = integers[0];
            int m = integers[1];
            Log.d(LOG_TAG, String.format("doInBackground(%d, %d)", n, m));
            estimatedResult = estimateResult(m, n);
            int calculatedResult = calculateAckermannFunction(m, n);
            publishProgress(1f);    // fully complete
            return calculatedResult;
        } else {
            return -1;
        }
    }

    private int estimateResult(int n, int m) {
        // https://studbooks.net/2195922/matematika_himiya_fizika/funktsiya_akkermana - формулы для построения контрольных примеров
        switch (n) {
            case 0:
                return m + 1;
            case 1:
                return m + 2;
            case 2:
                return 2 * m + 3;
            case 3:
                return (int) (Math.pow(2, m + 3) - 3);
            case 4:
                return (int) (Math.pow(2, estimateResultPow(m)) - 3);
            default:
                return -1;  // hard to estimate
        }
    }

    private int estimateResultPow(int m) {
        if (m == 0)
            return 4;
        else
            return (int) Math.pow(2, estimateResultPow(m - 1));
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        Log.d(LOG_TAG, String.format("onProgressUpdate(%f)", values[0]));
        if (contractTaskProducer != null) {
            contractTaskProducer.updateProgress(values[0]);
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer result) {
        Log.d(LOG_TAG, String.format("onPostExecute(%d)", result));
        if (contractTaskProducer != null) {
            contractTaskProducer.postResult(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled(Integer result) {
        String logMessage;
        if (result != null) {
            logMessage = String.format("onCancelled(%d)", result);
        } else {
            logMessage = "onCancelled(null)";
            result = -1;
        }
        Log.d(LOG_TAG, logMessage);
        if (contractTaskProducer != null) {
            contractTaskProducer.cancelProgress(result);
        }
        super.onCancelled(result);
    }

    private int calculateAckermannFunction(int n, int m) {
        if (n < 0 && m < 0) {
            return -1;  // not supported for negative numbers
        } else {
            calculateAndPublishProgress(n, m);
            if (n == 0)
                return m + 1;
            else {
                if (m == 0)
                    return calculateAckermannFunction(n - 1, 1);
                else    // n != 0 && m != 0
                    return calculateAckermannFunction(n - 1, calculateAckermannFunction(n, m - 1));
            }
        }
    }

    private void calculateAndPublishProgress(int n, int m) {
        if (m > maxCalculatedResult) {  // second argument accumulates while calculating
            maxCalculatedResult = m;
            float progressValue = calculateProgress(maxCalculatedResult);
            publishProgress(progressValue);
        }
    }

    private float calculateProgress(int actualProgress) {
        // progress determines portion of work done. 0 - nothing done, 1 - all done
        if (estimatedResult == -1)
            return -1; // it is not known when calculation ends
        else
            return (float) actualProgress / estimatedResult;
    }
}
