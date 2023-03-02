package com.example.advance_ui.transition_drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.advance_ui.R;
import com.example.advance_ui.Utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransitionActivity extends AppCompatActivity {

    @BindView(R.id.layout_container)
    FrameLayout layout_container;

    @BindView(R.id.iv_album)
    ImageView iv_album;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        ButterKnife.bind(this);
        // iv_album.setImageResource(R.drawable.transition);
        handlerTransition();
    }

    public void handlerTransition(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.transition);
        Bitmap resBlurBmp = ImageUtils.blurBitmap(this,bitmap,15f);
        // Drawable result = new BitmapDrawable(getResources(),resBlurBmp);
        Drawable result = ImageUtils.createBlurredImageFromBitmap(bitmap, this, 3);
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{result, result});
        td.setCrossFadeEnabled(true);
        td.startTransition(200);
        layout_container.setBackground(td);
    }
}
