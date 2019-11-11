package ru.geekbrains.android.level2.valeryvpetrov;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface TaskContract {

    /**
     * Producer uses this interface to interact with Consumer.
     */
    interface ProducerConsumer<Progress, Result> {

        int PROGRESS_MAX_VALUE = 100;
        int PROGRESS_DEFAULT_INCREMENT_VALUE = 1;

        // Consumer-Producer interface
        void startProgress();

        void updateProgress(@NonNull Progress progress);

        int normalizeProgress(@NonNull Progress progress);

        void postResult(@NonNull Result result);

        void postError(@NonNull Throwable e);

        void cancelProgress(@NonNull Result result);
    }

    /**
     * Consumer uses this interface to interact with Producer.
     */
    interface ConsumerProducer<Args, Result> {

        boolean isCalculating();

        @Nullable
        Result getResultIfReady();

        void startCalculation(@NonNull Args args);

        void stopCalculation();
    }

    /**
     * Task uses this interface to interact with Producer.
     */
    interface TaskProducer<Progress, Result> {

        void startProgress();

        void updateProgress(@NonNull Progress progress);

        void postResult(@NonNull Result result);

        void postError(@NonNull Throwable e);

        void cancelProgress(@NonNull Result result);
    }
}
