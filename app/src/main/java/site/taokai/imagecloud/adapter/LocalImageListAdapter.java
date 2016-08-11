package site.taokai.imagecloud.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import site.taokai.imagecloud.R;
import site.taokai.imagecloud.base.define;
import site.taokai.imagecloud.ui.activity.DetialImageActivity;
import site.taokai.imagecloud.utils.utillog;

/**
 * Created by 95 on 2016/8/7.
 */
public class LocalImageListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Uri> mImageUriList = new ArrayList<>();
    private ArrayList<String> mImageThumbliList = new ArrayList<>();
    private GridView mGridView;
    private View mView;

    public ArrayList<Uri> GetSelectList(){
        ArrayList<Uri> list = new ArrayList<>();
        for (int i = 0; i < mImageUriList.size(); i++){
            if (mSelectImageList.get(i) == true){
                list.add(mImageUriList.get(i));
            }
        }
        return list;
    }

    // 记录选中的项
    private ArrayList<Boolean> mSelectImageList = new ArrayList<>();

    public LocalImageListAdapter(Context context, GridView gridView, ArrayList<Uri> imageUriList, ArrayList<String> imageThumbliList){
        this.mContext = context;
        this.mGridView = gridView;
        this.mImageUriList = imageUriList;
        this.mImageThumbliList = imageThumbliList;
        for (int i = 0; i < imageUriList.size(); i++){
            mSelectImageList.add(i, false);
        }
    }

    @Override
    public int getCount() {
        return mImageUriList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageUriList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_local_main_gridview_item, null);
        }
        CheckBox itemCheck = (CheckBox)convertView.findViewById(R.id.ItemCheckBox);
        itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 记录哪些项被选中了
                mSelectImageList.set(position, isChecked);
            }
        });
        SimpleDraweeView itemImageView = (SimpleDraweeView)convertView.findViewById(R.id.ItemImage);
        final Uri imageUri = mImageUriList.get(position);
        itemImageView.setImageURI(imageUri);
        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), DetialImageActivity.class);
                intent.putExtra(define.DEFINE_DETAIL_URI, imageUri.toString());
                mContext.startActivity(intent);
            }
        });
        return  convertView;
    }
}
