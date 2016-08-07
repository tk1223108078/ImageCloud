package site.taokai.imagecloud.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import site.taokai.imagecloud.R;
import site.taokai.imagecloud.base.ImageCloud;
import site.taokai.imagecloud.db.GetLocalImageCursorTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocalMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalMainFragment extends Fragment implements View.OnClickListener,GetLocalImageCursorTask.OnLoadImageListInterface{
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
        // 启动图片获取所有本机图片
        GetLocalImageCursorTask localImageCursorTask = new GetLocalImageCursorTask(ImageCloud.getmAppContext());
        localImageCursorTask.setOnGetImageListInterface(this);
        localImageCursorTask.execute();
        return view;
    }

    // 点击事件
    @Override
    public void onClick(View v) {
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

    // 加载图片
    @Override
    public void OnLoadImageList(ArrayList<Uri> ImageUriList){

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
