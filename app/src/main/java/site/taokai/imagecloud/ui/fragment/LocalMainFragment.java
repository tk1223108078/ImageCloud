package site.taokai.imagecloud.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;

import site.taokai.imagecloud.R;
import site.taokai.imagecloud.adapter.LocalImageListAdapter;
import site.taokai.imagecloud.base.ImageCloud;
import site.taokai.imagecloud.db.GetLocalImageCursorTask;
import site.taokai.imagecloud.ui.activity.MainActivity;
import site.taokai.imagecloud.utils.utillog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocalMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalMainFragment extends Fragment implements View.OnClickListener,GetLocalImageCursorTask.OnLoadImageListInterface,
        View.OnTouchListener, GestureDetector.OnGestureListener, PopupMenu.OnMenuItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // 接口
    private OnFragmentInteractionListener mListener;
    private GetLocalImageCursorTask.OnLoadImageListInterface mLoadImageList;

    // view
    private GridView mGridImageView;
    private com.facebook.drawee.view.SimpleDraweeView mImageView;
    private FloatingActionButton mFloateButton;

    // menu
    PopupMenu mPopupMenu;
    Menu mMenu;

    public LocalMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalMainFragment newInstance(String param1, String param2) {
        LocalMainFragment fragment = new LocalMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_main, container, false);
        mGridImageView = (GridView)view.findViewById(R.id.local_main_fragment_imageviewlist);
        mImageView = (SimpleDraweeView) view.findViewById(R.id.local_main_fragment_imageview);
        mFloateButton = (FloatingActionButton)view.findViewById(R.id.local_main_fragment_fab);
        mFloateButton.setOnClickListener(this);
        // 启动图片获取所有本机图片
        GetLocalImageCursorTask localImageCursorTask = new GetLocalImageCursorTask(ImageCloud.getmAppContext(), 0);
        localImageCursorTask.setOnGetImageListInterface(this);
        localImageCursorTask.execute();
        mPopupMenu = new PopupMenu(getActivity(), view.findViewById(R.id.local_main_fragment_fab));
        mMenu = mPopupMenu.getMenu();
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_local_popmenu, mMenu);
        mPopupMenu.setOnMenuItemClickListener(this);
        return view;
    }

    // 点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.local_main_fragment_fab:
                mPopupMenu.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.local_menu_upload:
                Toast.makeText(getActivity(), "upload", Toast.LENGTH_SHORT).show();
                break;
            case R.id.local_menu_delete:
                mList = mImageAdapter.GetSelectList();
                backgroundDeleteImageList();
                break;
        }
        return true;
    }
    private  ArrayList<Uri> mList;

    private Handler mHandler = new Handler();
    // 批量删除图片(包括删除数据库中的数据和实际文件)
    private void backgroundDeleteImageList(){
        Thread thread = new Thread(null, doBackgroundDeleteFileThreadPrrocessing, "BackgroudDeleteImage");
        thread.start();
    }

    private int nCurrent = 0;
    private int nTotal = 0;
    // 执行后台处理方法的Runnable.
    private Runnable doBackgroundDeleteFileThreadPrrocessing = new Runnable() {
        @Override
        public void run() {
            DeleteImageList(mList);
        }
    };

    // 删除image文件
    private void DeleteImageList(ArrayList<Uri> mList){
        nTotal = mList.size();
        for (int i = 0; i < mList.size(); i++){
            nCurrent = i;
            File file = new File(getRealFilePath(getActivity(), mList.get(i)));
            file.delete();
            mHandler.post(doUpdateUI);
        }
    }

    private Runnable doUpdateUI = new Runnable() {
        @Override
        public void run() {
            UpdateDeleteUI(nCurrent, nTotal);
        }
    };

    private void UpdateDeleteUI(int nCurrent, int nTotal){
        if (nCurrent == nTotal){
            Toast.makeText(getActivity(), "总共删除了"+nTotal, Toast.LENGTH_SHORT).show();
            GetLocalImageCursorTask localImageCursorTask = new GetLocalImageCursorTask(ImageCloud.getmAppContext(), 0);
            localImageCursorTask.setOnGetImageListInterface(this);
            localImageCursorTask.execute();
        }else {
            Toast.makeText(getActivity(), "当前删除了"+ nCurrent+",总共"+nTotal, Toast.LENGTH_SHORT).show();
        }
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLocalFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // gridview加载图片
    private LocalImageListAdapter mImageAdapter;
    void GridViewLoadImages(ArrayList<Uri> strImageUriList, ArrayList<String> ImageThumbliList){
        mImageAdapter = new LocalImageListAdapter(this.getActivity(), mGridImageView, strImageUriList, ImageThumbliList);
        mGridImageView.setAdapter(mImageAdapter);
    }
    // 加载图片
    @Override
    public void OnLoadImageList(ArrayList<Uri> ImageUriList, ArrayList<String> ImageThumbliList){
        // mImageView.setImageURI(ImageUriList.get(0));
        GridViewLoadImages(ImageUriList, ImageThumbliList);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        utillog.utilloginfo("touch");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        utillog.utilloginfo("下滑");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        utillog.utilloginfo("上滑");
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLocalFragmentInteraction(Uri uri);
    }
}
