package io.countryInfo.wiki.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class ConnectivityChangeReceiver extends BroadcastReceiver{

    private static Subject<Boolean> booleanSubject = PublishSubject.create();

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean isActive;
        isActive = networkInfo != null && networkInfo.isConnected();
        booleanSubject.onNext(isActive);
        Log.d("OnReceive->", isActive.toString());
    }

    public static Observable<Boolean> isConnected(){
        return booleanSubject;
    }

}
