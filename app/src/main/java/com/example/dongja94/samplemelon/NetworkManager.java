package com.example.dongja94.samplemelon;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dongja94 on 2016-02-04.
 */
public class NetworkManager {
    private static NetworkManager instance;
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }
    ThreadPoolExecutor mExecutor;
    BlockingQueue<Runnable> mTaskQueue = new LinkedBlockingQueue<Runnable>();
    private NetworkManager() {
        mExecutor = new ThreadPoolExecutor(3, 64, 10, TimeUnit.SECONDS, mTaskQueue);
    }

    public interface OnResultListener<T> {
        public void onSuccess(NetworkRequest<T> request, T result);
        public void onFailure(NetworkRequest<T> request, int errorCode, int responseCode, String message, Throwable excepton);
    }

    public <T> void getNetworkData(NetworkRequest<T> request, OnResultListener<T> listener){
        mExecutor.execute(request);
    }
}
