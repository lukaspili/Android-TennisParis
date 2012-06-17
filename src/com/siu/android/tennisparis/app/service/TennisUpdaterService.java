//package com.siu.android.tennisparis.app.service;
//
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.Handler;
//import android.os.IBinder;
//import android.preference.PreferenceManager;
//import android.support.v4.content.LocalBroadcastManager;
//import com.siu.android.tennisparis.app.activity.TennisMapActivity;
//import com.siu.android.tennisparis.task.TennisUpdateTask;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class TennisUpdaterService extends Service {
//
//    private Handler handler = new Handler();
//    private Runnable runnable;
//    private TennisUpdateTask tennisUpdateTask;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (null != tennisUpdateTask) {
//                    tennisUpdateTask.cancel(true);
//                }
//
//                tennisUpdateTask = new TennisUpdateTask(TennisUpdaterService.this);
//                tennisUpdateTask.execute();
//
//                handler.postDelayed(this, 1000 * 60 * 30);
//            }
//        };
//
//        handler.post(runnable);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        handler.removeCallbacks(runnable);
//
//        if (null != tennisUpdateTask) {
//            tennisUpdateTask.cancel(true);
//        }
//    }
//
//    public void onTennisUpdateFinish() {
//        sendBroadcast(new Intent(Intent.ACTION_EDIT));
//    }
//}
