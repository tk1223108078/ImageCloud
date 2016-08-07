package site.taokai.imagecloud.ui.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import site.taokai.imagecloud.R;
import site.taokai.imagecloud.ui.fragment.AccountMainFragment;
import site.taokai.imagecloud.ui.fragment.CloudMainFragment;
import site.taokai.imagecloud.ui.fragment.LocalMainFragment;

public class MainActivity extends AppCompatActivity implements AccountMainFragment.OnFragmentInteractionListener, CloudMainFragment.OnFragmentInteractionListener, LocalMainFragment.OnFragmentInteractionListener {
    // 界面
    private Toolbar mToolbar;
    private AHBottomNavigation mBottomNavigation;

    // fragment
    private List<Fragment> fragments = new ArrayList<>();
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inittoolbar();
        initfragment();
        initbottom();
    }

    // 再按一次退出
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 初始化toolbar
    private void inittoolbar(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
    }

    // 初始化fragment
    private void initfragment(){
        fragments.add(LocalMainFragment.newInstance("local", "main"));
        fragments.add(CloudMainFragment.newInstance("cloud", "main"));
        fragments.add(AccountMainFragment.newInstance("account", "main"));
        showFragment(fragments.get(0));
    }

    // 初始化bottom
    private void initbottom(){
        mBottomNavigation = (AHBottomNavigation)findViewById(R.id.bottom_navigation_main);

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab1_main_text, R.drawable.loacl_main, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab2_main_text, R.drawable.cloud_main, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab3_main_text, R.drawable.accout_main, R.color.colorPrimary);

        // Add items
        mBottomNavigation.addItem(item1);
        mBottomNavigation.addItem(item2);
        mBottomNavigation.addItem(item3);

        // Set background color
        mBottomNavigation.setBehaviorTranslationEnabled(true);
        mBottomNavigation.setCurrentItem(0);

        // Set listeners
        mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener(){
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (currentTabIndex != position) {
                    // 隐藏上次选择的界面
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(fragments.get(currentTabIndex));
                    // 没被添加需要再次添加
                    if (!fragments.get(position).isAdded()) {
                        fragmentTransaction.add(R.id.content_main, fragments.get(position));
                    }
                    fragmentTransaction.show(fragments.get(position)).commit();
                }
                // 设置现在选择的界面
                currentTabIndex = position;
                return true;
            }
        });
        mBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
            }
        });
    }

    // 显示fragment
    private void showFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
    }

    // fragment方法

    @Override
    public void onAccountFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCloudFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLocalFragmentInteraction(Uri uri) {

    }
}
