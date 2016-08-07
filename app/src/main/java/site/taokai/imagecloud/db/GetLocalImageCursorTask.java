package site.taokai.imagecloud.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by 95 on 2016/8/7.
 */
public class GetLocalImageCursorTask extends AsyncTask<Object, Object, Object> {
    private Context mContext;
    private final ContentResolver mContentResolver;
    private boolean mExitTasksEarly = false;//退出任务线程的标志位
    // 存放图片的Uri
    private ArrayList<Uri> ImageUriList = new ArrayList<Uri>();

    public GetLocalImageCursorTask(Context mContext){
        this.mContext = mContext;
        this.mContentResolver = mContext.getContentResolver();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        // String[] projection = {MediaStore.Images.Media.}
        Uri ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c = MediaStore.Images.Media.query(mContentResolver, ext_uri, null, null, null, null);
        // 遍历数据库
        int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        c.moveToFirst();
        while (c.moveToNext() && !mExitTasksEarly){
            // 填充Uri列表
            long origId = c.getLong(columnIndex);
            ImageUriList.add(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(origId)));
        }
        c.close();
        // 早退出了清空列表
        if (mExitTasksEarly){
            ImageUriList.clear();
        }
        return null;
    }

    // 退出
    @Override
    protected void onPostExecute(Object o) {
        if (mOnGetIamgeListInterface != null && !mExitTasksEarly) {
            /**
             * 查询完成之后，设置回调接口中的数据，把数据传递到Activity中
             */
            mOnGetIamgeListInterface.OnLoadImageList(ImageUriList);
        }
    }

    // 取消
    @Override
    protected void onCancelled() {
        super.onCancelled();    //To change body of overridden methods use File | Settings | File Templates.
        mExitTasksEarly = true;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
    }

    public void setOnGetImageListInterface(OnLoadImageListInterface onLoadPhotoCursor) {
        this.mOnGetIamgeListInterface = onLoadPhotoCursor;
    }

    private OnLoadImageListInterface mOnGetIamgeListInterface;
    // 接口
    public interface OnLoadImageListInterface {
        public void OnLoadImageList(ArrayList<Uri> ImageUriList);
    }
}
