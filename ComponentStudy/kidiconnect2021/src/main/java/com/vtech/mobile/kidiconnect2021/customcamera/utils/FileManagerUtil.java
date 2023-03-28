package com.vtech.mobile.kidiconnect2021.customcamera.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileManagerUtil {

    public static void copyAssetsFile(ClassLoader classLoader, String targetPath, String destDir, String newFileName) throws Exception {
        InputStream in = null;
        FileOutputStream fos = null;

        try {
            in = classLoader.getResourceAsStream("assets" + File.separator + targetPath);
            fos = new FileOutputStream(new File(destDir + newFileName));
            byte[] buffer = new byte[1024];

            int len;
            while((len = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
        } catch (Exception var11) {
            Log.e("FileManagerUtil", "read assets file error,isMemoryFull:"  + " e:", var11);

            throw new Exception("read assets file error");
        } finally {
            if (null != in) {
                in.close();
            }

            if (null != fos) {
                fos.close();
            }

        }
    }

}
