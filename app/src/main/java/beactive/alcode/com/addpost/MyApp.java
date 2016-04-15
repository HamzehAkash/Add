package beactive.alcode.com.addpost;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Hamzeh on 4/14/2016.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
