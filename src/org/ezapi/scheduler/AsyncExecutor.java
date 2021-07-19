package org.ezapi.scheduler;

import org.ezapi.function.NonReturn;
import org.ezapi.function.OneReturn;

import java.util.concurrent.*;

public final class AsyncExecutor {

    public static void newTask(NonReturn nonReturn) {
        Thread thread = new Thread(nonReturn::apply);
        thread.start();
        thread.interrupt();
    }

    public static <T> Future<T> newTask(OneReturn<T> oneReturn) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<T> callable = oneReturn::apply;
        Future<T> future = executorService.submit(callable);
        executorService.shutdown();
        return future;
    }

}
