package cn.suiseiseki.www.waterdream;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WaterDreamActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment()
    {
        return new WaterDreamFragment();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

}
