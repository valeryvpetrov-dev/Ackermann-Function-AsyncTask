package ru.geekbrains.android.level2.valeryvpetrov;

public interface IProgressObserverView {
    int PROGRESS_MAX_VALUE = 100;
    int PROGRESS_DEFAULT_INCREMENT_VALUE = 1;

    void startProgress();

    void updateProgress(float progress);

    int normalizeProgress(float progress);

    void postResult(int result);

    void cancelProgress(int result);

}
