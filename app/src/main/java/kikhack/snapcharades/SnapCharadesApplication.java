package kikhack.snapcharades;

import android.app.Application;
<<<<<<< HEAD

import com.parse.Parse;
=======
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719

/**
 * Created by bryancho on 2015-01-31.
 */
public class SnapCharadesApplication extends Application {

    @Override
<<<<<<< HEAD
    public void onCreate() {
=======
    public void onCreate(){
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
        Parse.enableLocalDatastore(this);
        super.onCreate();
        Parse.initialize(this, "IOfeRPPkdbi4ytoaX3tp8PBxxGUAnrgybawnA9KE", "kCZS7GWxX8XNjFEKnvylcSwKqN1Pnsm33Kiavp2P");

    }
}
