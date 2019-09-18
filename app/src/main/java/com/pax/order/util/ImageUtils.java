package com.pax.order.util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.pax.order.FinancialApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;


public class ImageUtils {

    //保存图片到本地路径
    public static File saveImage(Bitmap bmp, String path, String fileName) {
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * * @Title: getLoacalBitmap * @Description: 加载本地图片
     *
     * @param @param url 本地路径
     * @return * @return Bitmap
     */

    public static Bitmap getBitmapByFullPath(String path) {
        if (null == path) {
            return null;
        }


        File file = new File(path);
        FileInputStream in = null;

        if (!file.exists()) {
            return null;
        }

        int imageType = isImage(path);
        if ((1 == imageType || 2 == imageType) && !imageIsWhole(path)) {
            return null;
        }

        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in);
    }

    public static Bitmap bimapRound(Bitmap mBitmap, float index) {
        if (null == mBitmap) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        //设置矩形大小
        Rect rect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF rectf = new RectF(rect);

        // 相当于清屏
        canvas.drawARGB(0, 0, 0, 0);
        //画圆角
        canvas.drawRoundRect(rectf, index, index, paint);
        // 取两层绘制，显示上层
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 把原生的图片放到这个画布上，使之带有画布的效果
        canvas.drawBitmap(mBitmap, rect, rect, paint);
        return bitmap;

    }

    public static int isImage(String name) {
        int indexOf;
        if (null == name || name.isEmpty() || (indexOf = name.lastIndexOf('.')) == -1) {
            return 0;
        }

        String suffix = name.substring(indexOf);
        if (suffix.equalsIgnoreCase(".jpg") || suffix.equalsIgnoreCase(".jpeg")) {
            return 1;
        } else if (suffix.equalsIgnoreCase(".png")) {
            return 2;
        } else if (suffix.equalsIgnoreCase(".gif")) {
            return 3;
        }

        return 0;
    }

    /**
     * if the input is a whole image file
     *
     * @param fileName file full mPath including suffix
     * @return boolean
     */
    public static boolean imageIsWhole(String fileName) {
//        Log.e("imageIsWhole", "fileName = " + fileName);
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(fileName, "r");
            long fileLength = rf.length();
            long start = rf.getFilePointer(); // 返回此文件中的当前偏移量
            long readIndex = start + fileLength - 8;
            rf.seek(readIndex); // 设置偏移量为文件末尾
            byte[] outData = new byte[1024];
            int n = 0;
            while (readIndex > start) {
                n = rf.read(outData, 0, 8);
                if (-1 == n) {
                    break;
                }

                if (0 != n) {
                    if (isImage(fileName) == 2) {
                        if ("IEND".startsWith(new String(outData, 0, 4))) {
                            return true;
                        }
                    } else if (isImage(fileName) == 1) {
                        if ((outData[6] == (byte) 0xFF) && (outData[7] == (byte) 0xD9)) {
                            return true;
                        }
                    }
                }

                readIndex -= 8;
                rf.seek(readIndex);
                break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null) {
                    rf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = FinancialApplication.getApp().getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }
}