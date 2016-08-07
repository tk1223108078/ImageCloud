package site.taokai.imagecloud.utils;

import android.util.Log;

/**
 * Created by 95 on 2016/8/7.
 */
public class utillog {
    private static String TAG = "TKTEST";
    private utillog(){
    }

    // info日志
    public static void utilloginfo(String strInfo){
        Log.i(TAG, "UtilLogInfo: "+ strInfo);
    }
}
