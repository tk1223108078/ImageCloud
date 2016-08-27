package site.taokai.imagecloud.network;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import site.taokai.imagecloud.base.CloudImageInfo;

/**
 * Created by 95 on 2016/8/27.
 */
public class GetCloudImageCursorTask extends AsyncTask<Object, Integer, Object> {
    // 成员变量
    private Context mContext;
    private ArrayList<CloudImageInfo> mCloudImageList = new ArrayList<>();

    // 构造函数了
    public GetCloudImageCursorTask(Context mContext){
        this.mContext = mContext;
    }

    @Override
    protected Object doInBackground(Object... params) {
        return null;
    }

    // 退出
    @Override
    protected void onPostExecute(Object o) {
        mOnGetCloudIamgeListInterface.OnLoadCloudImageList(mCloudImageList);
    }

    // 接口,完成查询工作通知UI获取信息
    public void setOnGetCloudImageListInterface(OnLoadCloudImageListInterface onLoadCloudImageList) {
        this.mOnGetCloudIamgeListInterface = onLoadCloudImageList;
    }

    private OnLoadCloudImageListInterface mOnGetCloudIamgeListInterface;
    // 接口
    public interface OnLoadCloudImageListInterface {
        public void OnLoadCloudImageList(ArrayList<CloudImageInfo> CloudImageList);
    }
}
