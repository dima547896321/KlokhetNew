package nl.klokhet.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;
import nl.klokhet.app.data.network.NetworkModule;
import nl.klokhet.app.data.network.api.API;
import timber.log.Timber;

public class App extends Application {
    public static App sInstance;
    public static API sService;


    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sInstance = this;
        Stetho.initializeWithDefaults(this);
//        Fabric.with(this, new Crashlytics());
        sService = NetworkModule.getBackEndService();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


}
