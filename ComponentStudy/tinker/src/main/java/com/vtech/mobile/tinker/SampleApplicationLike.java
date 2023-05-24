package com.vtech.mobile.tinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.entry.DefaultApplicationLike;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.listener.PatchListener;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.reporter.LoadReporter;
import com.tencent.tinker.lib.reporter.PatchReporter;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.vtech.mobile.tinker.Log.MyLogImp;
import com.vtech.mobile.tinker.util.SampleApplicationContext;
import com.vtech.mobile.tinker.util.TinkerManager;

@DefaultLifeCycle(application = "com.vtech.mobile.tinker.SampleApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class SampleApplicationLike extends DefaultApplicationLike {
    private static final String TAG = "Tinker.SampleApplicationLike";

    private Tinker mTinker;

    public SampleApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                                 long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        SampleApplicationContext.application = getApplication();
        SampleApplicationContext.context = getApplication();
        TinkerManager.setTinkerApplicationLike(this);

        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //optional set logIml, or you can use default debug log
        TinkerInstaller.setLogIml(new MyLogImp());

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());

        // 分包需要的东西
//        MultiDex.install(base);
//
//        // LoadReporter类定义了Tinker在加载补丁时的一些回调
//        LoadReporter loadReporter = new DefaultLoadReporter(getApplication());
//        // PatchReporter类定义了Tinker在修复或者升级补丁时的一些回调
//        PatchReporter patchReporter = new DefaultPatchReporter(getApplication());
//        // PatchListener类是用来过滤Tinker收到的补丁包的修复、升级请求，也就是决定我们是不是真的要唤起:patch进程去尝试补丁合成。
//        PatchListener patchListener = new DefaultPatchListener(getApplication());
//        // UpgradePatch类是用来升级当前补丁包的处理类，一般来说你也不需要复写它。
//        AbstractPatch upgradePatchProcessor = new UpgradePatch();
//
//        TinkerInstaller.install(this,
//                loadReporter, patchReporter, patchListener,
//                DefaultTinkerResultService.class, upgradePatchProcessor);

//        MultiDex.install(base);
//
//        TinkerManager.setTinkerApplicationLike(this);
//        // 设置全局异常捕获
//        TinkerManager.initFastCrashProtect();
//        //开启升级重试功能（在安装Tinker之前设置）
//        TinkerManager.setUpgradeRetryEnable(true);
//        //设置Tinker日志输出类
//        TinkerInstaller.setLogIml(new MyLogImp());
//        //安装Tinker(在加载完multiDex之后，否则你需要将com.tencent.tinker.**手动放到main dex中)
//        TinkerManager.installTinker(this);
//        mTinker = Tinker.with(getApplication());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }
}


