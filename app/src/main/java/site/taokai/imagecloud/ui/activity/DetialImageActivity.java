package site.taokai.imagecloud.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import site.taokai.imagecloud.R;
import site.taokai.imagecloud.base.define;

public class DetialImageActivity extends AppCompatActivity {
    private Uri mUri;
    // View
    private Toolbar mToolbar;
    private SubsamplingScaleImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial_image);
        Intent intent = getIntent();
        String strUri = intent.getStringExtra(define.DEFINE_DETAIL_URI);
        mUri = Uri.parse(strUri);
        inittoolbar();
        initcontent(mUri);
    }
    private void inittoolbar(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar_detial_image_activity);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.returnback);
        mToolbar.setTitle("大图查看");
        // 设置返回按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initcontent(Uri uri){
        mImageView = (SubsamplingScaleImageView)findViewById(R.id.imageview_detial_image_activity);
        mImageView.setImage(ImageSource.uri(uri));
    }
}
