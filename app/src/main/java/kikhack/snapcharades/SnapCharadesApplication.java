package kikhack.snapcharades;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by bryancho on 2015-01-31.
 */
public class SnapCharadesApplication extends Application {

    @Override
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        super.onCreate();
        Parse.initialize(this, "IOfeRPPkdbi4ytoaX3tp8PBxxGUAnrgybawnA9KE", "kCZS7GWxX8XNjFEKnvylcSwKqN1Pnsm33Kiavp2P");

    }
}
