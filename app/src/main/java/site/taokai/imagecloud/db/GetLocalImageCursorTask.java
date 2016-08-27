package site.taokai.imagecloud.db;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import site.taokai.imagecloud.base.ImageCloud;

/**
 * Created by 95 on 2016/8/7.
 */
public class GetLocalImageCursorTask extends AsyncTask<Object, Integer, Object> {
    private Context mContext;
    private final ContentResolver mContentResolver;
    private boolean mExitTasksEarly = false;//退出任务线程的标志位
    private int Mode = 0;
    // 存放图片的Uri
    private ArrayList<Uri> ImageUriList = new ArrayList<Uri>();
    private ArrayList<Uri> ImageUrlThumbnailList = new ArrayList<Uri>();

    public GetLocalImageCursorTask(Context mContext, int mode){
        this.mContext = mContext;
        this.mContentResolver = mContext.getContentResolver();
        this.Mode = mode;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if (Mode == 0){
            GetImageList();
        }
        return null;
    }

    // 获取图片列表
    private void GetImageList(){
        Uri ext_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c = MediaStore.Images.Media.query(mContentResolver, ext_uri, null);
        // 遍历数据库
        int columnIndexID = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int columnIndexSize = c.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        int i = 0;
        while (c.moveToNext() && !mExitTasksEarly){
            // 填充Uri列表
            long imagId = c.getLong(columnIndexID);
            Uri ThumbUri = getThumbnailUriByImageId(imagId);
            Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imagId));
            File file = new File(getRealFilePath(ImageCloud.getmAppContext(), uri));
            // 文件存在才插入
            if (file.exists()){
                // 缩略图的Uri获取失败的情况下还是向其中插入原图的Uri
                if (ThumbUri == null){
                    ImageUrlThumbnailList.add(uri);
                }else {
                    ImageUrlThumbnailList.add(ThumbUri);
                }
                ImageUriList.add(uri);
            }
            publishProgress(i);
            i++;
        }
        c.close();
        // 早退出了清空列表
        if (mExitTasksEarly){
            ImageUriList.clear();
            ImageUrlThumbnailList.clear();
        }
    }

    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    // 根据iamgeId获取图片缩略图的Uri
    private Uri getThumbnailUriByImageId(long id) {
        Uri uri = null;
        //获取大图的缩略图
        Cursor cursor = ImageCloud.getmAppContext().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""},
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int thumId = cursor.getInt(0);
            uri = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, String.valueOf(thumId));
        }
        cursor.close();
        return uri;
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
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

    @Override
    protected void onProgressUpdate(Integer... value) {
       //  Toast.makeText(ImageCloud.getmAppContext(), ""+value, Toast.LENGTH_SHORT).show();
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
        public void OnLoadImageList(ArrayList<Uri> ImageUriList, ArrayList<Uri> ImagePathList);
    }

    class GetLocalImageCursorTask1 extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }
}
