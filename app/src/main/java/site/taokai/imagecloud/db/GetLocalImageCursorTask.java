package site.taokai.imagecloud.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by 95 on 2016/8/7.
 */
public class GetLocalImageCursorTask extends AsyncTask<Object, Object, Object> {
    private Context mContext;
    private final ContentResolver mContentResolver;
    private boolean mExitTasksEarly = false;//退出任务线程的标志位
    private int Mode = 0;
    // 存放图片的Uri
    private ArrayList<Uri> ImageUriList = new ArrayList<Uri>();
    private ArrayList<String> ImageUrlThumbnailList = new ArrayList<String>();

    public GetLocalImageCursorTask(Context mContext, int mode){
        this.mContext = mContext;
        this.mContentResolver = mContext.getContentResolver();
        this.Mode = mode;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if (Mode == 0){
            GetImageList();
        }else if (Mode == 0){
            GetThumbliImageList();
        }else {

        }

        return null;
    }

    private void GetThumbliImageList(){
        Uri ext_uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        Cursor c = MediaStore.Images.Thumbnails.query(mContentResolver, ext_uri, null);
        // 遍历数据库
        int columnImageID = c.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
        int columnIndexImageID = c.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
        int columnIndexData = c.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
        while (c.moveToNext() && !mExitTasksEarly){
            long imagId = c.getLong(columnImageID);
            ImageUriList.add(Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, String.valueOf(imagId)));
        }
        c.close();
        // 早退出了清空列表
        if (mExitTasksEarly){
            ImageUriList.clear();
            ImageUrlThumbnailList.clear();
        }
    }

    private void GetImageList(){
        Uri ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c = MediaStore.Images.Media.query(mContentResolver, ext_uri, null);
        // 遍历数据库
        int columnIndexID = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int columnIndexSize = c.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        while (c.moveToNext() && !mExitTasksEarly){
            // 填充Uri列表
            long imagId = c.getLong(columnIndexID);
            ImageUriList.add(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imagId)));
        }
        c.close();
        // 早退出了清空列表
        if (mExitTasksEarly){
            ImageUriList.clear();
            ImageUrlThumbnailList.clear();
        }
    }

    // 退出
    @Override
    protected void onPostExecute(Object o) {
        if (mOnGetIamgeListInterface != null && !mExitTasksEarly) {
            /**
             * 查询完成之后，设置回调接口中的数据，把数据传递到Activity中
             */
            mOnGetIamgeListInterface.OnLoadImageList(ImageUriList, ImageUrlThumbnailList);
        }
    }

    // 取消
    @Override
    protected void onCancelled() {
        super.onCancelled();    //To change body of overridden methods use File | Settings | File Templates.
        mExitTasksEarly = true;
    }

//    private String GetThumbnailPath(long imgaId){
//        mContentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, )
//    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
    }

    public void setOnGetImageListInterface(OnLoadImageListInterface onLoadPhotoCursor) {
        this.mOnGetIamgeListInterface = onLoadPhotoCursor;
    }

    private OnLoadImageListInterface mOnGetIamgeListInterface;
    // 接口
    public interface OnLoadImageListInterface {
        public void OnLoadImageList(ArrayList<Uri> ImageUriList, ArrayList<String> ImagePathList);
    }
}
