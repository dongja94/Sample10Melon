package com.example.dongja94.samplemelon;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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

    private static final int MESSAGE_SUCCESS = 0;
    private static final int MESSAGE_FAILURE = 1;

    static class NetworkHandler extends  Handler {
        public NetworkHandler() {
            super();
        }

        public NetworkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            NetworkRequest r = (NetworkRequest)msg.obj;
            switch (msg.what) {
                case MESSAGE_SUCCESS :
                    r.sendSuccess();
                    break;
                case MESSAGE_FAILURE :
                    r.sendFailure();
                    break;
            }
        }
    }

    Handler mHandler = new NetworkHandler(Looper.getMainLooper());

    public void sendSuccess(NetworkRequest request) {
        Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, request);
        mHandler.sendMessage(msg);
    }

    public void sendFailure(NetworkRequest request) {
        Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, request);
        mHandler.sendMessage(msg);
    }

    public <T> void getNetworkData(NetworkRequest<T> request, OnResultListener<T> listener){
        request.setManager(this);
        request.setOnResultListener(listener);
        mExecutor.execute(request);
    }
}
