package site.taokai.imagecloud.base;

import android.app.Application;
import android.content.Context;
import android.text.NoCopySpan;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by 95 on 2016/8/6.
 */
public class ImageCloud extends Application{
    public static Context mAppContext;

    @Override
    public void onCreate(){
        super.onCreate();
        mAppContext = this;
        // 初始化
        Fresco.initialize(mAppContext);
    }

    public static Context getmAppContext(){
        return mAppContext;
    }
}
