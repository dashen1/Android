package com.vtech.mobile.kidiconnect2021.customcamera.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileManagerUtil {

    private static final String TAG = "FileManagerUtil";
    private static String externalFilePath = "";
    private static String externalStoragePublicDirectory;
    private static String dbPath;
    private static String filesDir;
    public static final String WEB_EFFECT_JSON_NAME = "FacialMask.json";
    public static final String LOCAL_EFFECT_JSON_NAME = "effects.json";
    public static final String THUMB_EXTENSION = ".thumb";
    private static boolean isInitExternalFilePath = false;
    private static boolean isMemoryApproachLimit = false;

    public FileManagerUtil() {
    }

    public static void initFilePath(Context context) {
        if (context == null) {
            Log.w("FileManagerUtil", "initFilePath, params is null.");
        } else {
            dbPath = context.getDatabasePath(".").getAbsolutePath() + File.separator;
            filesDir = context.getFilesDir().getAbsolutePath() + File.separator;
            initExternalStoragePath(context);
        }
    }

    private static void initExternalStoragePath(Context context) {
        if (context != null && !isInitExternalFilePath) {
            File file = new File(context.getExternalFilesDir(""), "");
            if (file.exists()) {
                externalFilePath = file.getPath() + File.separator;
                externalStoragePublicDirectory = externalFilePath.split("/Android/")[0] + File.separator;
                isInitExternalFilePath = true;
            } else {
                Log.w("FileManagerUtil", "initFilePath ExternalStorageState:" + Environment.getExternalStorageState());
                isInitExternalFilePath = false;
            }
        }
    }


    public static String getEffectJsonPath() {
        return filesDir + "/tillusory/mask/" + "effects.json";
    }

    public static String getWebEffectJson() {
        return filesDir + "/tillusory/mask/" + "FacialMask.json";
    }


    public static String getStickerPath() {
        return filesDir + "/tillusory/sticker/";
    }

    public static String getEffectIconPath() {
        return filesDir + "/tillusory/mask/icon/";
    }

    public static String getMaskPath() {
        return filesDir + "/tillusory/mask/";
    }

    public static String getTiDirPath() {
        return filesDir + "/tillusory/";
    }


    public static boolean isFileExist(String filePath) {
        return (new File(filePath)).exists();
    }

    public static boolean deleteFile(String filePath) {
        if (StringUtils.isStringEmptyOrNull(filePath)) {
            return false;
        } else {
            boolean bool = true;
            File file = new File(filePath);
            if (file.exists()) {
                bool = file.delete();
            } else if (file.isDirectory()) {
                bool = deleteDirectory(file);
            }
            return bool;
        }
    }

    private static boolean deleteDirectory(File file) {
        boolean bool = false;
        File[] files = file.listFiles();
        if (files != null) {
            File[] var3 = files;
            int var4 = files.length;

            for (int var5 = 0; var5 < var4; var5++) {
                File f = var3[var5];
                if (f.isFile()) {
                    bool = file.delete();
                } else if (f.isDirectory()) {
                    deleteDirectory(f);
                }
            }
            bool = bool && file.delete();
            Log.d("FileManagerUtil", "delete file:" + file.getPath() + ",bool:" + bool);
        }
        return bool;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                String[] var2 = children;
                int var3 = children.length;

                for (int var4 = 0; var4 < var3; var4++) {
                    String child = var2[var4];
                    boolean isDelete = deleteDir(new File(dir, child));
                    if (!isDelete) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    public static void copyAssetsFile(ClassLoader classLoader, String targetPath, String destDir, String newFileName) throws Exception {
        InputStream in = null;
        FileOutputStream fos = null;

        try {
            in = classLoader.getResourceAsStream("assets" + File.separator + targetPath);
            fos = new FileOutputStream(destDir + newFileName);
            byte[] buffer = new byte[1024];

            int len;
            while ((len = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
        } catch (Exception var11) {
            Log.e("FileManagerUtil", "read assets file error,isMemoryFull:" + " e:", var11);

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

    public static String readAssetsFileToString(ClassLoader classLoader, String targetPath) throws Exception {
        try {
            InputStream in = classLoader.getResourceAsStream("assets" + File.separator + targetPath);
            String var4;
            label45:
            {
                try {
                    byte[] buffer = new byte[in.available()];
                    if (in.read(buffer) != -1) {
                        var4 = new String(buffer);
                        break label45;// 可跳出任意多重循环
                    }
                } catch (Throwable var6) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }
                    throw var6;
                }
                if (in != null) {
                    in.close();
                }
                return null;
            }
            if (in != null) {
                in.close();
            }

            return var4;
        } catch (Exception var7) {
            throw new Exception("read assets file error");
        }
    }

    public static File saveFile(byte[] byt, String filePath) throws Exception {
        if (byt != null && byt.length != 0 && !TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            FileOutputStream output = null;

            try {
                boolean createFileSuccess = false;
                if (file.delete() || !file.exists()) {
                    createFileSuccess = file.createNewFile();
                }

                if (createFileSuccess) {
                    output = new FileOutputStream(file);
                    output.write(byt);
                    output.flush();
                }
            } catch (Exception var13) {
                boolean isMemoryFull = isMemoryFullException(var13);
                Log.e("FileManagerUtil", "saveFile error,isMemoryFull:" + isMemoryFull + " e:", var13);
                if (isMemoryFull) {
                    // ToDo
                }

                throw var13;
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException var12) {
                    Log.e("FileManagerUtil", "saveFile,", var12);
                }

            }

            return file;
        } else {
            return null;
        }
    }


    public static boolean isMemoryFullException(Exception exception) {
        if (exception != null) {
            if (exception.getMessage() != null && (exception.getMessage().contains("ENOSPC") || exception.getMessage().contains("EROFS"))) {
                return true;
            }

            if (exception instanceof IOException) {
                Log.d("FileManagerUtil", "check isInternalMemoryReachSize because it is IOException");
                return isInternalMemoryFull();
            }
        }

        return false;
    }

    public static boolean isInternalMemoryFull() {
        boolean isInternalMemoryFull = isMemoryReachLimitSize(10485760L);
        if (!isInternalMemoryFull) {
            isMemoryApproachLimit = isMemoryReachLimitSize(52428800L);
            Log.d("FileManagerUtil", "isInternalMemoryFull->isMemoryApproachLimit:" + isMemoryApproachLimit);
        }

        return isInternalMemoryFull;
    }
    private static boolean isMemoryReachLimitSize(long limitSize) {
        boolean isInternalMemoryFull = false;

        try {
            isInternalMemoryFull = getFreeMemory(Environment.getDataDirectory()) <= limitSize;
            isInternalMemoryFull = isInternalMemoryFull || getFreeMemory(new File(filesDir)) <= limitSize;
            Log.d("FileManagerUtil", "isInternalMemoryFull->internal:" + isInternalMemoryFull);
        } catch (Exception var4) {
            Log.e("FileManagerUtil", "isInternalMemoryFull->ex:" + var4.getMessage());
        }

        return isInternalMemoryFull;
    }

    private static long getFreeMemory(File path) {
        StatFs stats = new StatFs(path.getAbsolutePath());
        return stats.getAvailableBlocksLong() * stats.getBlockSizeLong();
    }



}
