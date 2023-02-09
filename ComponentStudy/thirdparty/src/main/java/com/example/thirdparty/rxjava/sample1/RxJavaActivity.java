package com.example.thirdparty.rxjava.sample1;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thirdparty.R;
import com.example.thirdparty.retrofit.sample1.RetrofitClientSample1;
import com.example.thirdparty.retrofit.sample1.RetrofitClientWithRxJava;
import com.example.thirdparty.retrofit.sample1.ServiceApiSample1;
import com.example.thirdparty.retrofit.sample1.UserLoginResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {

    private static final String TAG ="RxJavaActivity";

    @BindView(R.id.imageView)
    public ImageView imageView;

    Resources resources;

    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_rxjava)
    void rxJavaTest(){
        Observable.just("file")
                .map(new Function<String, Bitmap>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.funny);
                        return bitmap;
                    }
                })
                .map(o->{
                    Bitmap bitmap = createWaterMark(o, "RxJava Mark");
                    return bitmap;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    @OnClick(R.id.btn_custom_rxjava)
    void rxJavaCustomTest(){

    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_rxjava_retrofit)
    void rxJavaRetrofitTest(){
        RetrofitClientWithRxJava.getServiceApi().userLoginWithRxjava("coder123","coder123")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserLoginResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(UserLoginResult userLoginResult) {
                        Log.i(TAG,"success");
                        Log.i(TAG, userLoginResult.getData().getNickname());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,"failed");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Bitmap createWaterMark(Bitmap bitmap, String mark){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#c5576370"));
        paint.setTextSize(150);
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap,0,0,paint);
        canvas.drawText(mark,0,h/2.f,paint);
        canvas.save();
        canvas.restore();
        return bmp;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);

        ButterKnife.bind(this);
        resources = RxJavaActivity.this.getResources();
    }
}
