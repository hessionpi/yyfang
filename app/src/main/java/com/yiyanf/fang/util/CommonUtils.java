package com.yiyanf.fang.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Hition on 2017/10/25.
 */

public class CommonUtils {

    // 字符串截断
    public static String getLimitString(String source, int length){
        if (null!=source && source.length()>length){
            return source.substring(0, length)+"...";
        }
        return source;
    }

    /**
     * 单位换算，把byte换算成KB、MB
     * @param byteSize
     */
    public static String formatSize(long byteSize) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) byteSize / (float) (1024 * 1024));
        if (f < 1.0) {
            float f2 = ((float) byteSize / (float) (1024));
            return df.format(Float.valueOf(f2).doubleValue()) + "KB";
        } else {
            return df.format(Float.valueOf(f).doubleValue()) + "M";
        }
    }

    /**
     * 获取缓存文件大小
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除缓存文件
     * @param context
     */
    public static boolean clearAllCache(Context context) {
        boolean internalCache = deleteDir(context.getCacheDir());
        boolean externalCache = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            externalCache = deleteDir(context.getExternalCacheDir());
        }

        return internalCache && externalCache;
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     */
    public static String getFormatSize(long size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "TB";
    }

    /**
     * 根据比例转化实际数值为相对值
     * @param gear 档位
     * @param max 最大值
     * @param curr 当前值
     * @return 相对值
     */
    public static int filtNumber(int gear, int max, int curr) {
        return curr / (max / gear);
    }

}
